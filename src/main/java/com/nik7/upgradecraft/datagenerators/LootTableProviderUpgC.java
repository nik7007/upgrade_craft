package com.nik7.upgradecraft.datagenerators;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.nik7.upgradecraft.init.RegisterBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
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

public class LootTableProviderUpgC extends LootTableProvider {

    public LootTableProviderUpgC(DataGenerator dataGeneratorIn) {
        super(dataGeneratorIn);
    }

    @Override
    protected void validate(@NotNull Map<ResourceLocation, LootTable> map, @NotNull ValidationContext validationtracker) {
        // Nope
    }

    @Override
    protected @NotNull List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(
                Pair.of(BlockLootTablesUpgC::new, LootContextParamSets.BLOCK)
        );
    }

    private static class BlockLootTablesUpgC extends BlockLoot {
        private Iterable<Block> cacheBlocks = null;

        @Override
        protected void addTables() {
            for (Block block : this.getKnownBlocks()) {
                this.dropSelf(block);
            }
        }

        @Override
        protected @NotNull Iterable<Block> getKnownBlocks() {
            if (cacheBlocks == null) {
                cacheBlocks = Arrays.stream(RegisterBlocks.class.getDeclaredFields())
                        .filter(field -> {
                            int modifiers = field.getModifiers();
                            return Modifier.isPublic(modifiers) && Modifier.isStatic(modifiers);
                        }).map(field -> {
                            try {
                                var fieldObj = field.get(null);
                                if (fieldObj instanceof RegistryObject) {
                                    return (Block) ((RegistryObject<?>) fieldObj).get();
                                }
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                            throw new RuntimeException("Impossible to read Block object");
                        }).collect(Collectors.toList());
            }
            return cacheBlocks;
        }
    }
}
