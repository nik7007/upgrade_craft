package com.nik7.upgradecraft.datagenerators;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.nik7.upgradecraft.init.RegisterBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class UpgCLootTableProvider extends LootTableProvider {
    public UpgCLootTableProvider(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void validate(@NotNull Map<ResourceLocation, LootTable> map, @NotNull ValidationContext validationtracker) {
        // Nope
    }

    @Override
    protected @NotNull List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(
                Pair.of(UpgCBlockLootTables::new, LootContextParamSets.BLOCK)
        );
    }

    private static class UpgCBlockLootTables extends BlockLoot {
        private Iterable<? extends Block> cacheBlocks = null;

        @Override
        protected void addTables() {
            Iterable<? extends Block> blocks = this.getBlocksInternal();
            for (Block block : blocks) {
                if (block instanceof DoorBlock) {
                    this.add(block, BlockLoot::createDoorTable);
                } else
                    this.dropSelf(block);
            }
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            return getBlocksInternal();
        }

        @SuppressWarnings("unchecked")
        private <T extends Block> Iterable<T> getBlocksInternal() {

            if (cacheBlocks == null) {
                cacheBlocks = Arrays.stream(RegisterBlock.class.getDeclaredFields())
                        .filter(field -> {
                            int modifiers = field.getModifiers();
                            return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers);
                        }).map(field -> {
                            try {
                                var fieldObj = field.get(null);
                                if (fieldObj instanceof RegistryObject<?> registryObject) {
                                    return (T) registryObject.get();
                                }
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                            throw new RuntimeException("Impossible to read Block object");
                        }).collect(Collectors.toList());
            }

            return (Iterable<T>) cacheBlocks;
        }
    }
}
