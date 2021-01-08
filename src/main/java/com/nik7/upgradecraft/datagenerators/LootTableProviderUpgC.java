package com.nik7.upgradecraft.datagenerators;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.nik7.upgradecraft.init.RegisterBlocks;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.LootTableProvider;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.functions.CopyName;
import net.minecraft.loot.functions.CopyNbt;
import net.minecraft.loot.functions.SetContents;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class LootTableProviderUpgC extends LootTableProvider {
    private static final ILootCondition.IBuilder SILK_TOUCH = MatchTool.builder(ItemPredicate.Builder.create().enchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1))));

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
                if (block == RegisterBlocks.TERRACOTTA_FLUID_TANK_BLOCK.get() ||
                        block == RegisterBlocks.TERRACOTTA_FLUID_TANK_GLASSED_BLOCK.get()) {
                    registerTerracottaDrop(block);
                } else {
                    this.registerDropSelfLootTable(block);
                }
            }
        }

        private void registerTerracottaDrop(Block block) {
            LootPool.Builder builder = LootPool.builder()
                    .rolls(ConstantRange.of(1))
                    .addEntry(
                            ItemLootEntry.builder(block)
                                    .acceptFunction(CopyName.builder(CopyName.Source.BLOCK_ENTITY))
                                    .acceptFunction(CopyNbt.builder(CopyNbt.Source.BLOCK_ENTITY)
                                            .addOperation("FluidName", "BlockEntityTag.FluidName", CopyNbt.Action.REPLACE)
                                            .addOperation("Amount", "BlockEntityTag.Amount", CopyNbt.Action.REPLACE)
                                            .addOperation("Tag", "BlockEntityTag.Tag", CopyNbt.Action.REPLACE)
                                    )
                                    .acceptFunction(SetContents.builderIn()
                                            .addLootEntry(DynamicLootEntry.func_216162_a(new ResourceLocation("minecraft", "contents"))))
                                    .acceptCondition(SILK_TOUCH)
                                    .alternatively(
                                            ItemLootEntry.builder(block)
                                    )
                    );
            registerLootTable(block, blockIn -> LootTable.builder().addLootPool(builder));
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
                    RegisterBlocks.TERRACOTTA_FLUID_TANK_GLASSED_BLOCK.get(),
                    RegisterBlocks.CLAY_BRICKS_BLOCK.get(),
                    RegisterBlocks.CLAY_BRICKS_SLAB_BLOCK.get(),
                    RegisterBlocks.CLAY_BRICKS_WALL_BLOCK.get(),
                    RegisterBlocks.CLAY_BRICKS_STAIRS_BLOCK.get()
            );
        }
    }

}
