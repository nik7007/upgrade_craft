package com.nik7.upgradecraft.client.render.blockentity;


import com.mojang.blaze3d.vertex.PoseStack;
import com.nik7.upgradecraft.blockentity.AbstractFluidTankEntity;
import com.nik7.upgradecraft.client.render.Cuboid;
import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.fluids.FluidStack;

import static com.nik7.upgradecraft.init.RegisterBlockEntity.WOODEN_FLUID_TANK_GLASS_ENTITY_TYPE;

public class FluidTankRenderer extends BaseFluidHandlerRenderer<AbstractFluidTankEntity> {

    private static final float DOUBLE_HEIGHT = 2.15f;

   /* public FluidTankRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }*/

    public FluidTankRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    public static void register() {
        BlockEntityRenderers.register(WOODEN_FLUID_TANK_GLASS_ENTITY_TYPE.get(), FluidTankRenderer::new);
        // ClientRegistry.bindTileEntityRenderer(CLAY_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE.get(), FluidTankRenderer::new);
        // ClientRegistry.bindTileEntityRenderer(TERRACOTTA_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE.get(), FluidTankRenderer::new);
    }

    @Override
    public void render(AbstractFluidTankEntity blockEntityIn, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        float height = 1;
        TankType tankType = blockEntityIn.getTankType();
        boolean mixed = blockEntityIn.isTankMixed();
        if (tankType == TankType.TOP) {
            if (!mixed) {
                return;
            }
            height = DOUBLE_HEIGHT;
        } else if (tankType == TankType.BOTTOM) {
            height = DOUBLE_HEIGHT;
        }

        FluidStack fluidStack = blockEntityIn.getFluid();
        if (!fluidStack.isEmpty()) {
            float fluidScale = getFluidScale(blockEntityIn, fluidStack);

            Cuboid cuboid = new Cuboid(0.15, 0.10, 0.15, 0.85, 0.90 * height, 0.85);

            int translateY = tankType == TankType.TOP ? -1 : 0;
            renderFluidCuboid(fluidStack, fluidScale, cuboid, poseStack, bufferIn, combinedLightIn, matrix -> matrix.translate(0, translateY, 0));
        }
    }
}
