package com.nik7.upgradecraft.datagenerators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherDataGenerators(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        if (event.includeServer()) {
            generator.addProvider(new RecipeProviderUpgC(generator));
            generator.addProvider(new LootTableProviderUpgC(generator));
            ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
            BlockTagsProviderUpgC blockTagsProvider = new BlockTagsProviderUpgC(generator, existingFileHelper);
            generator.addProvider(blockTagsProvider);
            generator.addProvider(new ItemTagsProviderUpgC(generator, blockTagsProvider, existingFileHelper));
        }
        if (event.includeClient()) {
            ExistingFileHelper helper = event.getExistingFileHelper();
            generator.addProvider(new BlockStateProviderUpgC(generator, helper));
            generator.addProvider(new ItemModelProviderUpgC(generator, helper));
            //lang
            generator.addProvider(new LanguageProviderUsUpgC(generator));
        }
    }
}
