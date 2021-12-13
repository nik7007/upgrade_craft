package com.nik7.upgradecraft.client.render.utils;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import com.nik7.upgradecraft.client.render.Cuboid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.minecraft.util.FastColor.ARGB32.*;


public final class RenderHelper {

    private static final int U_MIN = 0;
    private static final int U_MAX = 1;
    private static final int V_MIN = 2;
    private static final int V_MAX = 3;

    private static final Vector3f VEC_ZERO = new Vector3f(0, 0, 0);

    private RenderHelper() {
    }


    public static int getColorARGB(@Nonnull FluidStack fluidStack, float fluidScale) {
        if (fluidStack.isEmpty()) {
            return -1;
        }
        int color = fluidStack.getFluid().getAttributes().getColor(fluidStack);
        if (fluidStack.getFluid().getAttributes().isGaseous(fluidStack)) {
            float alpha = Math.min(1, fluidScale + 0.2F);
            if (alpha < 0) {
                alpha = 0;
            } else if (alpha > 1) {
                alpha = 1;
            }
            return color((int) alpha, red(color), green(color), blue(color));
        }
        return color;
    }

    public static TextureAtlasSprite locationToSprite(ResourceLocation textureLocation) {
        return Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(textureLocation);
    }

    public static void renderCuboid(Cuboid cuboid,
                                    PoseStack poseStack,
                                    VertexConsumer buffer,
                                    int light,
                                    int argb,
                                    Function<Direction, TextureAtlasSprite> getSprite,
                                    Consumer<PoseStack> preRenderOperation) {
        float red = red(argb) / 255f;
        float green = green(argb) / 255f;
        float blue = blue(argb) / 255f;
        float alpha = alpha(argb) / 255f;

        Vector3d size = new Vector3d(cuboid.getX(), cuboid.getY(), cuboid.getZ());

        poseStack.pushPose();
        poseStack.translate(cuboid.getMinX(), cuboid.getMinY(), cuboid.getMinZ());
        if (preRenderOperation != null) {
            preRenderOperation.accept(poseStack);
        }

        Matrix4f matrix4f = poseStack.last().pose();

        for (Direction face : Direction.values()) {
            if (face == Direction.DOWN) {
                continue;
            }
            TextureAtlasSprite sprite = getSprite.apply(face);

            Direction.Axis u = face.getAxis() == Direction.Axis.X ? Direction.Axis.Z : Direction.Axis.X;
            Direction.Axis v = face.getAxis() == Direction.Axis.Y ? Direction.Axis.Z : Direction.Axis.Y;
            float other = (face.getAxisDirection() == Direction.AxisDirection.POSITIVE) ? (float) getValue(size, face.getAxis()) : 0;

            //Swap the face if this is positive: the renderer returns indexes that ALWAYS are for the negative face, so light it properly this way
            face = face.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? face : face.getOpposite();
            Direction opposite = face.getOpposite();

            float minU = sprite.getU0();
            float maxU = sprite.getU1();
            //Flip the v
            float minV = sprite.getV1();
            float maxV = sprite.getV0();
            double sizeU = getValue(size, u);
            double sizeV = getValue(size, v);


            for (int uIndex = 0; uIndex < sizeU; uIndex++) {
                float[] baseUV = new float[]{minU, maxU, minV, maxV};
                double addU = 1;
                // If the size of the texture is greater than the cuboid goes on for then make sure the texture positions are lowered
                if (uIndex + addU > sizeU) {
                    addU = sizeU - uIndex;
                    baseUV[U_MAX] = baseUV[U_MIN] + (baseUV[U_MAX] - baseUV[U_MIN]) * (float) addU;
                }
                for (int vIndex = 0; vIndex < sizeV; vIndex++) {
                    float[] uv = Arrays.copyOf(baseUV, 4);
                    double addV = 1;
                    if (vIndex + addV > sizeV) {
                        addV = sizeV - vIndex;
                        uv[V_MAX] = uv[V_MIN] + (uv[V_MAX] - uv[V_MIN]) * (float) addV;
                    }
                    float[] xyz = new float[]{uIndex, (float) (uIndex + addU), vIndex, (float) (vIndex + addV)};

                    renderPoint(matrix4f, buffer, face, poseStack, u, v, other, uv, xyz, true, false, red, green, blue, alpha, light);
                    renderPoint(matrix4f, buffer, face, poseStack, u, v, other, uv, xyz, true, true, red, green, blue, alpha, light);
                    renderPoint(matrix4f, buffer, face, poseStack, u, v, other, uv, xyz, false, true, red, green, blue, alpha, light);
                    renderPoint(matrix4f, buffer, face, poseStack, u, v, other, uv, xyz, false, false, red, green, blue, alpha, light);

                    renderPoint(matrix4f, buffer, opposite, poseStack, u, v, other, uv, xyz, false, false, red, green, blue, alpha, light);
                    renderPoint(matrix4f, buffer, opposite, poseStack, u, v, other, uv, xyz, false, true, red, green, blue, alpha, light);
                    renderPoint(matrix4f, buffer, opposite, poseStack, u, v, other, uv, xyz, true, true, red, green, blue, alpha, light);
                    renderPoint(matrix4f, buffer, opposite, poseStack, u, v, other, uv, xyz, true, false, red, green, blue, alpha, light);
                }
            }


        }
        poseStack.popPose();
    }

    private static void renderPoint(Matrix4f matrix4f, VertexConsumer buffer, Direction face, PoseStack poseStack, Direction.Axis u, Direction.Axis v, float other, float[] uv, float[] xyz, boolean minU, boolean minV,
                                    float red, float green, float blue, float alpha, int light) {
        int U_ARRAY = minU ? U_MIN : U_MAX;
        int V_ARRAY = minV ? V_MIN : V_MAX;
        Vector3f vertex = withValue(VEC_ZERO, u, xyz[U_ARRAY]);
        vertex = withValue(vertex, v, xyz[V_ARRAY]);
        vertex = withValue(vertex, face.getAxis(), other);
        PoseStack.Pose peek = poseStack.last();
        Vec3i normal = face.getNormal();
        buffer.vertex(matrix4f, vertex.x(), vertex.y(), vertex.z())
                .color(red, green, blue, alpha)
                .uv(uv[U_ARRAY], uv[V_ARRAY])
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(peek.normal(), normal.getX(), normal.getY(), normal.getZ())
                .endVertex();
    }

    private static Vector3f withValue(Vector3f vector, Direction.Axis axis, float value) {
        if (axis == Direction.Axis.X) {
            return new Vector3f(value, vector.y(), vector.z());
        } else if (axis == Direction.Axis.Y) {
            return new Vector3f(vector.x(), value, vector.z());
        } else if (axis == Direction.Axis.Z) {
            return new Vector3f(vector.x(), vector.y(), value);
        }
        throw new RuntimeException("Was given a null axis! That was probably not intentional, consider this a bug! (Vector = " + vector + ")");
    }

    private static double getValue(Vector3d vector, Direction.Axis axis) {
        if (axis == Direction.Axis.X) {
            return vector.x;
        } else if (axis == Direction.Axis.Y) {
            return vector.y;
        } else if (axis == Direction.Axis.Z) {
            return vector.z;
        }
        throw new RuntimeException("Was given a null axis! That was probably not intentional, consider this a bug! (Vector = " + vector + ")");
    }

    public static int calculateGlowLight(int light, int glow) {
        if (glow >= 15) {
            return 0xF000F0;
        }
        int blockLight = LightTexture.block(light);
        int skyLight = LightTexture.sky(light);
        return LightTexture.pack(Math.max(blockLight, glow), Math.max(skyLight, glow));
    }

    public static int calculateFluidGlowLight(int light, @Nonnull FluidStack fluid) {
        return fluid.isEmpty() ? light : calculateGlowLight(light, fluid.getFluid().getAttributes().getLuminosity(fluid));
    }
}
