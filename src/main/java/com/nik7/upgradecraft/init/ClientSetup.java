package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.UpgradeCraft;
import com.nik7.upgradecraft.client.gui.container.FluidFurnaceScreen;
import com.nik7.upgradecraft.client.gui.container.FluidInfuserScreen;
import com.nik7.upgradecraft.client.renderer.item.FluidTankItemStackRender;
import com.nik7.upgradecraft.client.renderer.tileentity.FluidFurnaceRenderer;
import com.nik7.upgradecraft.client.renderer.tileentity.FluidInfuserRender;
import com.nik7.upgradecraft.client.renderer.tileentity.FluidTankRenderer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.nik7.upgradecraft.init.RegisterContainers.FLUID_FURNACE_CONTAINER_TYPE;
import static com.nik7.upgradecraft.init.RegisterContainers.FLUID_INFUSER_CONTAINER_TYPE;

@Mod.EventBusSubscriber(modid = UpgradeCraft.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(RegisterBlocks.FLUID_FURNACE_BLOCK.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(RegisterBlocks.FLUID_INFUSER_BLOCK.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(RegisterBlocks.CLAY_FLUID_TANK_GLASSED_BLOCK.get(), RenderType.getCutoutMipped());
        RenderTypeLookup.setRenderLayer(RegisterBlocks.TERRACOTTA_FLUID_TANK_GLASSED_BLOCK.get(), RenderType.getCutoutMipped());

        FluidTankRenderer.register();
        FluidFurnaceRenderer.register();
        FluidInfuserRender.register();

        ScreenManager.registerFactory(FLUID_FURNACE_CONTAINER_TYPE.get(), FluidFurnaceScreen::new);
        ScreenManager.registerFactory(FLUID_INFUSER_CONTAINER_TYPE.get(), FluidInfuserScreen::new);
    }

    @SubscribeEvent
    public static void onModelBake(final ModelBakeEvent event) {
        FluidTankItemStackRender.registerModelProxy(event.getModelRegistry(), RegisterItems.TERRACOTTA_FLUID_TANK_GLASSED_ITEM_BLOCK.get().getRegistryName());
    }
}
