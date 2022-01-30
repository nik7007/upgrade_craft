package com.nik7.upgradecraft.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.nik7.upgradecraft.blockentity.AbstractMachineEntity;
import com.nik7.upgradecraft.client.render.Cuboid;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraftforge.fluids.FluidStack;

public class BaseMachineWithFluidRender<T extends AbstractMachineEntity> extends BaseFluidHandlerRenderer<T> {
    protected BaseMachineWithFluidRender(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(T pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {
        FluidStack fluidStack = pBlockEntity.getFluid();
        if (!fluidStack.isEmpty()) {
            float fluidScale = getFluidScale(pBlockEntity, fluidStack);
            Cuboid cuboid = new Cuboid(0.19, 0.064, 0.20, 0.80, 0.45, 0.80);

            renderFluidCuboid(fluidStack, fluidScale, cuboid, pPoseStack, pBufferSource, pPackedLight, null);
        }
    }
}
