package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.client.FluidTankRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(RegisterBlocks.FLUID_FURNACE_BLOCK.get(), RenderType.getCutoutMipped());
        FluidTankRenderer.register();
    }
}
