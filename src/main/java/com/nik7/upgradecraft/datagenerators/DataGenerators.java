package com.nik7.upgradecraft.datagenerators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.nik7.upgradecraft.UpgradeCraft.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherDataGenerators(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();


        generator.addProvider(event.includeServer(), new UpgCLootTableProvider(generator));
        generator.addProvider(event.includeServer(), new UpgCRecipeProvider(generator));
        generator.addProvider(event.includeServer(), new UpgCBlockTagsProvider(generator, event.getExistingFileHelper()));
        // generator.addProvider(new ItemTagsProviderUpgC(generator, blockTagsProvider, existingFileHelper));

        generator.addProvider(event.includeClient(), new UpgCBlockStateProvider(generator, event.getExistingFileHelper()));
        generator.addProvider(event.includeClient(), new UpgCItemModelProvider(generator, event.getExistingFileHelper()));
        //lang
        generator.addProvider(event.includeClient(), new UpgCLanguageProvider.US(generator));

    }
}
