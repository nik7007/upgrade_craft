package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.client.render.blockentity.FluidFurnaceRenderer;
import com.nik7.upgradecraft.client.render.blockentity.FluidTankRenderer;
import com.nik7.upgradecraft.client.screen.FluidFurnaceScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import static com.nik7.upgradecraft.init.RegisterContainerMenu.FLUID_FURNACE_CONTAINER_TYPE;


//@Mod.EventBusSubscriber(modid = UpgradeCraft.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientSetup {

    public static void init(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemBlockRenderTypes.setRenderLayer(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get(), RenderType.cutoutMipped());
            ItemBlockRenderTypes.setRenderLayer(RegisterBlocks.FLUID_FURNACE_BLOCK.get(), RenderType.cutoutMipped());
            // RenderTypeLookup.setRenderLayer(RegisterBlocks.FLUID_INFUSER_BLOCK.get(), RenderType.getCutoutMipped());
            // RenderTypeLookup.setRenderLayer(RegisterBlocks.CLAY_FLUID_TANK_GLASSED_BLOCK.get(), RenderType.getCutoutMipped());
            // RenderTypeLookup.setRenderLayer(RegisterBlocks.TERRACOTTA_FLUID_TANK_GLASSED_BLOCK.get(), RenderType.getCutoutMipped());

            FluidTankRenderer.register();
            FluidFurnaceRenderer.register();
            // FluidInfuserRender.register();

            MenuScreens.register(FLUID_FURNACE_CONTAINER_TYPE.get(), FluidFurnaceScreen::new);
            // ScreenManager.registerFactory(FLUID_INFUSER_CONTAINER_TYPE.get(), FluidInfuserScreen::new);
        });
    }

    // @SubscribeEvent
    // public static void onModelBake(final ModelBakeEvent event) {
    //     FluidTankItemStackRender.registerModelProxy(event.getModelRegistry(), RegisterItems.TERRACOTTA_FLUID_TANK_GLASSED_ITEM_BLOCK.get().getRegistryName());
    // }
}
