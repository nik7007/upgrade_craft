package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.UpgradeCraft;
import com.nik7.upgradecraft.client.render.blockentity.FluidTankRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = UpgradeCraft.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientOnlyEventHandler {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers registerRenderers) {
        registerRenderers.registerBlockEntityRenderer(RegisterBlockEntity.WOODEN_GLASSED_FLUID_TANK_ENTITY_TYPE.get(), FluidTankRenderer::new);
    }
}
