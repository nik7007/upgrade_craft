package com.nik7.upgradecraft.init

import com.nik7.upgradecraft.UpgradeCraft
import com.nik7.upgradecraft.block.FluidFurnaceBlock
import com.nik7.upgradecraft.block.SlimyPlanksBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.minecraft.world.level.material.Material
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import thedarkcolour.kotlinforforge.forge.registerObject

object ModBlocks {
    val REGISTRY: DeferredRegister<Block> = DeferredRegister.create(ForgeRegistries.BLOCKS, UpgradeCraft.ID)

    // the returned ObjectHolderDelegate can be used as a property delegate
    // this is automatically registered by the deferred registry at the correct times
    val SLIMY_PLANKS_BLOCK by REGISTRY.registerObject("slimy_planks_block") {
        SlimyPlanksBlock()
    }

    val FLUID_FURNACE_BLOCK by REGISTRY.registerObject("fluid_furnace_block") {
        FluidFurnaceBlock()
    }
}
