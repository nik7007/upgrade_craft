package com.nik7.upgradecraft.datagenerators;


import com.nik7.upgradecraft.init.RegisterBlock;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.nik7.upgradecraft.UpgradeCraft.MODID;

public class UpgCBlockStateProvider extends BlockStateProvider {
    public UpgCBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(RegisterBlock.SLIMY_PLANKS_BLOCK.get());
        stairsBlock(RegisterBlock.SLIMY_PLANKS_STAIRS_BLOCK.get(), modLoc("block/slimy_planks_block"));
        slabBlock(RegisterBlock.SLIMY_PLANKS_SLAB_BLOCK.get(), modLoc("block/slimy_planks_block"), modLoc("block/slimy_planks_block"));
        fenceBlock(RegisterBlock.SLIMY_PLANKS_FENCE_BLOCK.get(), modLoc("block/slimy_planks_block"));
        fenceGateBlock(RegisterBlock.SLIMY_PLANKS_FENCE_GATE_BLOCK.get(), modLoc("block/slimy_planks_block"));
        doorBlockWithRenderType(RegisterBlock.SLIMY_PLANKS_DOOR_BLOCK.get(),  modLoc("block/slimy_door_bottom"),  modLoc("block/slimy_door_top"), "cutout");
        trapdoorBlockWithRenderType(RegisterBlock.SLIMY_PLANKS_TRAP_DOOR_BLOCK.get(), modLoc("block/slimy_trapdoor"), true, "cutout");
    }
}
