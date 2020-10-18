package com.nik7.upgradecraft.client.renderer.tileentity;

import com.nik7.upgradecraft.tileentity.FluidInfuserTileEntity;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import static com.nik7.upgradecraft.init.RegisterTileEntity.FLUID_INFUSER_TILE_ENTITY_TYPE;

public class FluidInfuserRender extends BaseMachineWithFluidFluidRender<FluidInfuserTileEntity> {

    public FluidInfuserRender(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    public static void register() {
        ClientRegistry.bindTileEntityRenderer(FLUID_INFUSER_TILE_ENTITY_TYPE.get(), FluidInfuserRender::new);
    }
}
