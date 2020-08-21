package com.nik7.upgradecraft.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nik7.upgradecraft.state.properties.TankType;
import com.nik7.upgradecraft.tileentity.WoodenFluidTankGlassedTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import static com.nik7.upgradecraft.init.RegisterTileEntity.WOODEN_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE;

public class FluidTankRenderer extends BaseFluidHandlerRenderer<WoodenFluidTankGlassedTileEntity> {

    private static final float DOUBLE_HEIGHT = 2.15f;

    public FluidTankRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }


    @Override
    public void render(WoodenFluidTankGlassedTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        float height = 1;
        TankType tankType = tileEntityIn.getTankType();
        boolean mixed = tileEntityIn.isTankMixed();
        if (tankType == TankType.TOP) {
            if (!mixed) {
                return;
            }
            height = DOUBLE_HEIGHT;
        } else if (tankType == TankType.BOTTOM) {
            height = DOUBLE_HEIGHT;
        }

        FluidStack fluidStack = tileEntityIn.getFluid();
        if (!fluidStack.isEmpty()) {
            float fluidScale = getFluidScale(tileEntityIn, fluidStack);

            Cuboid cuboid = new Cuboid(0.15, 0.10, 0.15, 0.85, 0.90 * height, 0.85);

            int translateY = tankType == TankType.TOP ? -1 : 0;
            renderFluidCuboid(fluidStack, fluidScale, cuboid, matrixStackIn, bufferIn, combinedLightIn, matrix -> matrix.translate(0, translateY, 0));
        }
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(WOODEN_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE.get(), FluidTankRenderer::new);
    }
}
