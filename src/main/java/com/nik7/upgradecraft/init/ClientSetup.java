package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.client.gui.container.FluidFurnaceScreen;
import com.nik7.upgradecraft.client.renderer.tileentity.FluidFurnaceRenderer;
import com.nik7.upgradecraft.client.renderer.tileentity.FluidTankRenderer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.nik7.upgradecraft.init.RegisterContainers.FLUID_FURNACE_CONTAINER_TYPE;

public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(RegisterBlocks.FLUID_FURNACE_BLOCK.get(), RenderType.getCutoutMipped());

        FluidTankRenderer.register();
        FluidFurnaceRenderer.register();

        ScreenManager.registerFactory(FLUID_FURNACE_CONTAINER_TYPE.get(), FluidFurnaceScreen::new);
    }
}
