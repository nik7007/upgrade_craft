package com.nik7.upgradecraft.client.render.blockentity;

import com.nik7.upgradecraft.blockentity.FluidFurnaceEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;

import static com.nik7.upgradecraft.init.RegisterBlockEntity.FLUID_FURNACE_ENTITY_TYPE;

public class FluidFurnaceRenderer extends BaseMachineWithFluidRender<FluidFurnaceEntity>{
    protected FluidFurnaceRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }
    public static void register() {
        BlockEntityRenderers.register(FLUID_FURNACE_ENTITY_TYPE.get(), FluidFurnaceRenderer::new);
    }
}
