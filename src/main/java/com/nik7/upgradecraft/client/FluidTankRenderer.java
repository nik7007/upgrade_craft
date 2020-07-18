package com.nik7.upgradecraft.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nik7.upgradecraft.state.properties.TankType;
import com.nik7.upgradecraft.tileentity.WoodenFluidTankGlassedTileEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Direction.AxisDirection;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nonnull;
import java.util.Arrays;

import static com.nik7.upgradecraft.init.RegisterTileEntity.WOODEN_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE;
import static net.minecraft.util.ColorHelper.PackedColor.*;

public class FluidTankRenderer extends TileEntityRenderer<WoodenFluidTankGlassedTileEntity> {

    private static final int U_MIN = 0;
    private static final int U_MAX = 1;
    private static final int V_MIN = 2;
    private static final int V_MAX = 3;

    private static final Vector3f VEC_ZERO = new Vector3f(0, 0, 0);

    public FluidTankRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(WoodenFluidTankGlassedTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        float height = 1;
        TankType tankType = tileEntityIn.getTankType();
        if (tankType == TankType.TOP) {
            return;
        } else if (tankType == TankType.BOTTOM) {
            height = 2.25f;
        }

        FluidStack fluidStack = tileEntityIn.getFluid();
        if (!fluidStack.isEmpty()) {
            IVertexBuilder buffer = bufferIn.getBuffer(RenderTypeUpgC.resizableCuboid());

            Fluid fluid = fluidStack.getFluid();
            float fluidScale = fluidStack.getAmount() / (float) tileEntityIn.getCapacity();
            ResourceLocation stillTexture = fluid.getAttributes().getStillTexture(fluidStack);
            TextureAtlasSprite stillSprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(stillTexture);

            ResourceLocation flowingTexture = fluid.getAttributes().getFlowingTexture(fluidStack);
            TextureAtlasSprite flowingSprite = Minecraft.getInstance().getAtlasSpriteGetter(AtlasTexture.LOCATION_BLOCKS_TEXTURE).apply(flowingTexture);

            int light = calculateGlowLight(combinedLightIn, fluidStack);
            int argb = getColorARGB(fluidStack, fluidScale);

            float red = getRed(argb) / 255f;
            float green = getGreen(argb) / 255f;
            float blue = getBlue(argb) / 255f;
            float alpha = getAlpha(argb) / 255f;

            Vector3d size = new Vector3d(0.70, 0.80 * fluidScale * height, 0.70);

            matrixStackIn.push();
            matrixStackIn.translate(0.15, 0.10, 0.15); // min x, y, z
            Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();

            for (Direction face : Direction.values()) {
                if (face == Direction.DOWN) {
                    continue;
                }
                TextureAtlasSprite sprite = face == Direction.UP ? stillSprite : flowingSprite;

                Axis u = face.getAxis() == Axis.X ? Axis.Z : Axis.X;
                Axis v = face.getAxis() == Axis.Y ? Axis.Z : Axis.Y;
                float other = (face.getAxisDirection() == AxisDirection.POSITIVE) ? (float) getValue(size, face.getAxis()) : 0;

                //Swap the face if this is positive: the renderer returns indexes that ALWAYS are for the negative face, so light it properly this way
                face = face.getAxisDirection() == AxisDirection.NEGATIVE ? face : face.getOpposite();
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
    }

    private void renderPoint(Matrix4f matrix4f, IVertexBuilder buffer, Direction face, Axis u, Axis v, float other, float[] uv, float[] xyz, boolean minU, boolean minV,
                             float red, float green, float blue, float alpha, int light) {
        int U_ARRAY = minU ? U_MIN : U_MAX;
        int V_ARRAY = minV ? V_MIN : V_MAX;
        Vector3f vertex = withValue(VEC_ZERO, u, xyz[U_ARRAY]);
        vertex = withValue(vertex, v, xyz[V_ARRAY]);
        vertex = withValue(vertex, face.getAxis(), other);
        buffer.pos(matrix4f, vertex.getX(), vertex.getY(), vertex.getZ()).color(red, green, blue, alpha).tex(uv[U_ARRAY], uv[V_ARRAY]).lightmap(light).endVertex();
    }

    private static Vector3f withValue(Vector3f vector, Axis axis, float value) {
        if (axis == Axis.X) {
            return new Vector3f(value, vector.getY(), vector.getZ());
        } else if (axis == Axis.Y) {
            return new Vector3f(vector.getX(), value, vector.getZ());
        } else if (axis == Axis.Z) {
            return new Vector3f(vector.getX(), vector.getY(), value);
        }
        throw new RuntimeException("Was given a null axis! That was probably not intentional, consider this a bug! (Vector = " + vector + ")");
    }

    public static double getValue(Vector3d vector, Axis axis) {
        if (axis == Axis.X) {
            return vector.x;
        } else if (axis == Axis.Y) {
            return vector.y;
        } else if (axis == Axis.Z) {
            return vector.z;
        }
        throw new RuntimeException("Was given a null axis! That was probably not intentional, consider this a bug! (Vector = " + vector + ")");
    }

    private static int getColorARGB(@Nonnull FluidStack fluidStack, float fluidScale) {
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

    private static int calculateGlowLight(int light, @Nonnull FluidStack fluid) {
        return fluid.isEmpty() ? light : calculateGlowLight(light, fluid.getFluid().getAttributes().getLuminosity(fluid));
    }

    private static int calculateGlowLight(int light, int glow) {
        if (glow >= 15) {
            return 0xF000F0;
        }
        int blockLight = LightTexture.getLightBlock(light);
        int skyLight = LightTexture.getLightSky(light);
        return LightTexture.packLight(Math.max(blockLight, glow), Math.max(skyLight, glow));
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(WOODEN_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE.get(), FluidTankRenderer::new);
    }
}
