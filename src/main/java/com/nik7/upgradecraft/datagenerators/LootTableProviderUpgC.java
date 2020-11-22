package com.nik7.upgradecraft.datagenerators;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.nik7.upgradecraft.init.RegisterBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.loot.*;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTableProviderUpgC extends LootTableProvider {

    public LootTableProviderUpgC(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootParameterSet>> getTables() {
        return ImmutableList.of(
                Pair.of(BlockLootTablesUpgC::new, LootParameterSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationtracker) {
        map.forEach((name, table) -> LootTableManager.validateLootTable(validationtracker, name, table));
    }

    private static class BlockLootTablesUpgC extends BlockLootTables {
        @Override
        protected void addTables() {
            for (Block block : this.getKnownBlocks()) {
                this.registerDropSelfLootTable(block);
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Arrays.asList(
                    RegisterBlocks.SLIMY_PLANKS_BLOCK.get(),
                    RegisterBlocks.WOODEN_FLUID_TANK_BLOCK.get(),
                    RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get(),
                    RegisterBlocks.FUNNEL_BLOCK.get(),
                    RegisterBlocks.FLUID_FURNACE_BLOCK.get(),
                    RegisterBlocks.FLUID_INFUSER_BLOCK.get(),
                    RegisterBlocks.CLAY_FLUID_TANK_BLOCK.get(),
                    RegisterBlocks.CLAY_FLUID_TANK_GLASSED_BLOCK.get(),
                    RegisterBlocks.TERRACOTTA_FLUID_TANK_BLOCK.get(),
                    RegisterBlocks.TERRACOTTA_FLUID_TANK_GLASSED_BLOCK.get()
            );
        }
    }

}
