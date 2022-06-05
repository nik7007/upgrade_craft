@file:Mod.EventBusSubscriber(modid = UpgradeCraft.ID, bus = Mod.EventBusSubscriber.Bus.MOD)

package com.nik7.upgradecraft.datagenerators

import com.nik7.upgradecraft.UpgradeCraft
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent


@SubscribeEvent
fun gatherDataGenerators(event: GatherDataEvent) {
    val generator = event.generator
    val helper = event.existingFileHelper

    if (event.includeServer()) {
        generator.addProvider(ModRecipeProvider(generator))
        generator.addProvider(ModLootTableProvider(generator))
    }

    if (event.includeClient()) {
        generator.addProvider(ModBlockStateProvider(generator, helper))
        generator.addProvider(ModItemModelProvider(generator, helper))
        generator.addProvider(ModLanguageProvider.UsLanguageProvider(generator))
    }

}
