package com.nik7.upgradecraft.init

import com.nik7.upgradecraft.UpgradeCraft
import com.nik7.upgradecraft.item.BaseBlockItem
import net.minecraft.world.item.Item
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object ModItems {
    val REGISTRY: DeferredRegister<Item> = DeferredRegister.create(ForgeRegistries.ITEMS, UpgradeCraft.ID)

    val SLIMY_PLANKS_ITEM_BLOCK by REGISTRY.registerObject("slimy_planks_block") {
        BaseBlockItem(ModBlocks.SLIMY_PLANKS_BLOCK)
    }

    val FLUID_FURNACE_ITEM_BLOCK by REGISTRY.registerObject("fluid_furnace_block") {
        BaseBlockItem(ModBlocks.FLUID_FURNACE_BLOCK)
    }
}
