package com.nik7.upgradecraft.datagenerators;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.nik7.upgradecraft.init.RegisterBlocks;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;

import java.util.Collections;
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
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationTracker validationresults) {
        map.forEach((name, table) -> LootTableManager.func_227508_a_(validationresults, name, table));
    }

    private static class BlockLootTablesUpgC extends BlockLootTables {
        @Override
        protected void addTables() {
            this.registerDropSelfLootTable(RegisterBlocks.SLIMY_PLANKS_BLOCK.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return Collections.singletonList(RegisterBlocks.SLIMY_PLANKS_BLOCK.get());
        }
    }

}