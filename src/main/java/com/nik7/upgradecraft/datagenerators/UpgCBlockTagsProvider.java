package com.nik7.upgradecraft.datagenerators;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.nik7.upgradecraft.UpgradeCraft.MODID;
import static com.nik7.upgradecraft.init.RegisterBlock.*;

public class UpgCBlockTagsProvider extends BlockTagsProvider {
    public UpgCBlockTagsProvider(DataGenerator dataGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(dataGenerator, MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        FenceBlock slimyFenceBlock = SLIMY_PLANKS_FENCE_BLOCK.get();
        tag(BlockTags.FENCES)
                .add(slimyFenceBlock);
        tag(BlockTags.WOODEN_FENCES)
                .add(slimyFenceBlock);
        tag(BlockTags.STAIRS)
                .add(SLIMY_PLANKS_STAIRS_BLOCK.get());
        tag(BlockTags.SLABS)
                .add(SLIMY_PLANKS_SLAB_BLOCK.get());
        tag(BlockTags.DOORS)
                .add(SLIMY_PLANKS_DOOR_BLOCK.get());
        tag(BlockTags.WOODEN_DOORS)
                .add(SLIMY_PLANKS_DOOR_BLOCK.get());
        tag(BlockTags.TRAPDOORS)
                .add(SLIMY_PLANKS_TRAP_DOOR_BLOCK.get());
    }

    @Override
    public @NotNull String getName() {
        return MODID + " Block tags";
    }
}
