package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.crafting.TagUpgC;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;
import static com.nik7.upgradecraft.init.RegisterBlocks.WOODEN_FLUID_TANK_BLOCK;
import static com.nik7.upgradecraft.init.RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK;

public class BlockTagsProviderUpgC extends BlockTagsProvider {
    public BlockTagsProviderUpgC(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerTags() {
        getOrCreateBuilder(TagUpgC.Blocks.WOODEN_TANK)
                .add(WOODEN_FLUID_TANK_BLOCK.get(),
                        WOODEN_FLUID_TANK_GLASSED_BLOCK.get());
    }

    @Override
    public String getName() {
        return MOD_ID + " Block tags";
    }
}
