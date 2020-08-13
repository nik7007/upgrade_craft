package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.crafting.TagUpgC;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class ItemTagsProviderUpgC extends ItemTagsProvider {

    public ItemTagsProviderUpgC(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider) {
        super(dataGenerator, blockTagProvider);
    }

    @Override
    protected void registerTags() {
        copy(TagUpgC.Blocks.WOODEN_TANK, TagUpgC.Items.WOODEN_TANK);
    }

    @Override
    public String getName() {
        return MOD_ID + " Item Tags";
    }
}
