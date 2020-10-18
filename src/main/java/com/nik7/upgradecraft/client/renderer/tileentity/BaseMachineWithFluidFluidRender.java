package com.nik7.upgradecraft.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nik7.upgradecraft.client.renderer.Cuboid;
import com.nik7.upgradecraft.tileentity.AbstractFluidMachineTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fluids.FluidStack;

public abstract class BaseMachineWithFluidFluidRender<T extends AbstractFluidMachineTileEntity<?>> extends BaseFluidHandlerRenderer<T> {

    public BaseMachineWithFluidFluidRender(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(T tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        FluidStack fluidStack = tileEntityIn.getFluid();
        if (!fluidStack.isEmpty()) {
            float fluidScale = getFluidScale(tileEntityIn, fluidStack);
            Cuboid cuboid = new Cuboid(0.19, 0.064, 0.20, 0.80, 0.45, 0.80);

            renderFluidCuboid(fluidStack, fluidScale, cuboid, matrixStackIn, bufferIn, combinedLightIn, null);
        }
    }
}
