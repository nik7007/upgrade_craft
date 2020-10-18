package com.nik7.upgradecraft.client.renderer.tileentity;

import com.nik7.upgradecraft.tileentity.FluidFurnaceTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import static com.nik7.upgradecraft.init.RegisterTileEntity.FLUID_FURNACE_TILE_ENTITY_TYPE;

public class FluidFurnaceRenderer extends BaseMachineWithFluidFluidRender<FluidFurnaceTileEntity> {

    public FluidFurnaceRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(FLUID_FURNACE_TILE_ENTITY_TYPE.get(), FluidFurnaceRenderer::new);
    }
}
