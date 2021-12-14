package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.crafting.TagUpgC;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class ItemTagsProviderUpgC extends ItemTagsProvider {

    public ItemTagsProviderUpgC(DataGenerator dataGenerator, BlockTagsProvider blockTagProvider, ExistingFileHelper existingFileHelper) {
        super(dataGenerator, blockTagProvider, MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        copy(TagUpgC.Blocks.WOODEN_TANK, TagUpgC.Items.WOODEN_TANK);
    }

    @Override
    public String getName() {
        return MOD_ID + " Item Tags";
    }
}
