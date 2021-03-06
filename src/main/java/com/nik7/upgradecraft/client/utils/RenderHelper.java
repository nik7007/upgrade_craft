package com.nik7.upgradecraft.client.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nik7.upgradecraft.client.renderer.Cuboid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

import static net.minecraft.util.ColorHelper.PackedColor.*;

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
            return packColor((int) alpha, getRed(color), getGreen(color), getBlue(color));
        }
        return color;
    }

    public static TextureAtlasSprite locationToSprite(ResourceLocation textureLocation) {
        return Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(textureLocation);
    }

    public static void renderCuboid(Cuboid cuboid,
                                    MatrixStack matrixStackIn,
                                    IVertexBuilder buffer,
                                    int light,
                                    int argb,
                                    Function<Direction, TextureAtlasSprite> getSprite,
                                    Consumer<MatrixStack> preRenderOperation) {
        float red = getRed(argb) / 255f;
        float green = getGreen(argb) / 255f;
        float blue = getBlue(argb) / 255f;
        float alpha = getAlpha(argb) / 255f;

        Vector3d size = new Vector3d(cuboid.getX(), cuboid.getY(), cuboid.getZ());

        matrixStackIn.push();
        matrixStackIn.translate(cuboid.getMinX(), cuboid.getMinY(), cuboid.getMinZ());
        if (preRenderOperation != null) {
            preRenderOperation.accept(matrixStackIn);
        }

        Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();

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

            float minU = sprite.getMinU();
            float maxU = sprite.getMaxU();
            //Flip the v
            float minV = sprite.getMaxV();
            float maxV = sprite.getMinV();
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

                    renderPoint(matrix4f, buffer, face, u, v, other, uv, xyz, true, false, red, green, blue, alpha, light);
                    renderPoint(matrix4f, buffer, face, u, v, other, uv, xyz, true, true, red, green, blue, alpha, light);
                    renderPoint(matrix4f, buffer, face, u, v, other, uv, xyz, false, true, red, green, blue, alpha, light);
                    renderPoint(matrix4f, buffer, face, u, v, other, uv, xyz, false, false, red, green, blue, alpha, light);

                    renderPoint(matrix4f, buffer, opposite, u, v, other, uv, xyz, false, false, red, green, blue, alpha, light);
                    renderPoint(matrix4f, buffer, opposite, u, v, other, uv, xyz, false, true, red, green, blue, alpha, light);
                    renderPoint(matrix4f, buffer, opposite, u, v, other, uv, xyz, true, true, red, green, blue, alpha, light);
                    renderPoint(matrix4f, buffer, opposite, u, v, other, uv, xyz, true, false, red, green, blue, alpha, light);
                }
            }


        }
        matrixStackIn.pop();
    }

    private static void renderPoint(Matrix4f matrix4f, IVertexBuilder buffer, Direction face, Direction.Axis u, Direction.Axis v, float other, float[] uv, float[] xyz, boolean minU, boolean minV,
                                    float red, float green, float blue, float alpha, int light) {
        int U_ARRAY = minU ? U_MIN : U_MAX;
        int V_ARRAY = minV ? V_MIN : V_MAX;
        Vector3f vertex = withValue(VEC_ZERO, u, xyz[U_ARRAY]);
        vertex = withValue(vertex, v, xyz[V_ARRAY]);
        vertex = withValue(vertex, face.getAxis(), other);
        buffer.pos(matrix4f, vertex.getX(), vertex.getY(), vertex.getZ()).color(red, green, blue, alpha).tex(uv[U_ARRAY], uv[V_ARRAY]).lightmap(light).endVertex();
    }

    private static Vector3f withValue(Vector3f vector, Direction.Axis axis, float value) {
        if (axis == Direction.Axis.X) {
            return new Vector3f(value, vector.getY(), vector.getZ());
        } else if (axis == Direction.Axis.Y) {
            return new Vector3f(vector.getX(), value, vector.getZ());
        } else if (axis == Direction.Axis.Z) {
            return new Vector3f(vector.getX(), vector.getY(), value);
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
        int blockLight = LightTexture.getLightBlock(light);
        int skyLight = LightTexture.getLightSky(light);
        return LightTexture.packLight(Math.max(blockLight, glow), Math.max(skyLight, glow));
    }

    public static int calculateFluidGlowLight(int light, @Nonnull FluidStack fluid) {
        return fluid.isEmpty() ? light : calculateGlowLight(light, fluid.getFluid().getAttributes().getLuminosity(fluid));
    }
}
