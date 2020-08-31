package com.nik7.upgradecraft.client.renderer.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nik7.upgradecraft.client.renderer.Cuboid;
import com.nik7.upgradecraft.tileentity.FluidFurnaceTileEntity;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import static com.nik7.upgradecraft.init.RegisterTileEntity.FLUID_FURNACE_TILE_ENTITY_TYPE;

public class FluidFurnaceRenderer extends BaseFluidHandlerRenderer<FluidFurnaceTileEntity> {

    public FluidFurnaceRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(FLUID_FURNACE_TILE_ENTITY_TYPE.get(), FluidFurnaceRenderer::new);
    }

    @Override
    public void render(FluidFurnaceTileEntity tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        FluidStack fluidStack = tileEntityIn.getFluid();
        if (!fluidStack.isEmpty()) {
            float fluidScale = getFluidScale(tileEntityIn, fluidStack);
            Cuboid cuboid = new Cuboid(0.19, 0.064, 0.20, 0.80, 0.45, 0.80);

            renderFluidCuboid(fluidStack, fluidScale, cuboid, matrixStackIn, bufferIn, combinedLightIn, null);
        }
    }
}
