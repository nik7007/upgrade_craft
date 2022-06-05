package com.nik7.upgradecraft.datagenerators

import com.mojang.datafixers.util.Pair
import com.nik7.upgradecraft.init.ModBlocks
import net.minecraft.data.DataGenerator
import net.minecraft.data.loot.BlockLoot
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.LootTables
import net.minecraft.world.level.storage.loot.ValidationContext
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import java.util.function.BiConsumer
import java.util.function.Consumer
import java.util.function.Supplier
import kotlin.reflect.full.memberProperties

class ModLootTableProvider(pGenerator: DataGenerator) : LootTableProvider(pGenerator) {
    override fun validate(map: Map<ResourceLocation, LootTable>, validationtracker: ValidationContext) {
        map.forEach { (name, table) -> LootTables.validate(validationtracker, name, table) }
    }

    override fun getTables(): List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> {
        return listOf(
            Pair.of(
                Supplier { BlockLootTablesUpgC() },
                LootContextParamSets.BLOCK
            )
        )
    }

    private class BlockLootTablesUpgC : BlockLoot() {

        private var cacheBlocks: Iterable<Block>? = null

        override fun addTables() {
            for (block in this.knownBlocks) {
                if (block != null) {
                    dropSelf(block)
                }
            }
        }

        override fun getKnownBlocks(): Iterable<Block?> {
            if (cacheBlocks == null) {
                cacheBlocks = ModBlocks::class.memberProperties
                    .map { it.get(ModBlocks) }
                    .filterIsInstance<Block>()
            }
            return cacheBlocks!!
        }

    }
}
