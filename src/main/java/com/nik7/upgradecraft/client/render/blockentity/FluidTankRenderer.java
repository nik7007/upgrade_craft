package com.nik7.upgradecraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nik7.upgradecraft.block.tank.entity.AbstractFluidTankEntityBlock;
import com.nik7.upgradecraft.client.render.Cuboid;
import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;

@OnlyIn(Dist.CLIENT)
public class FluidTankRenderer extends BaseFluidHandlerRenderer<AbstractFluidTankEntityBlock> {
    private static final float DOUBLE_HEIGHT = 2.15f;

    public FluidTankRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(AbstractFluidTankEntityBlock blockEntityIn,
                       float partialTicks,
                       PoseStack poseStack,
                       MultiBufferSource bufferIn,
                       int combinedLightIn,
                       int combinedOverlayIn) {
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
