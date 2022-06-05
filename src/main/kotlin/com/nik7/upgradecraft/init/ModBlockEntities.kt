package com.nik7.upgradecraft.init

import com.nik7.upgradecraft.UpgradeCraft
import com.nik7.upgradecraft.blockentity.FluidFurnaceEntity
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModBlockEntities {
    val REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, UpgradeCraft.ID)

    val FLUID_FURNACE_ENTITY_TYPE: RegistryObject<BlockEntityType<FluidFurnaceEntity>> = REGISTRY.register("fluid_furnace"
    ) {
        BlockEntityType.Builder.of({ pWorldPosition: BlockPos, pBlockState: BlockState ->
            FluidFurnaceEntity(
                pWorldPosition,
                pBlockState
            )
        }, ModBlocks.FLUID_FURNACE_BLOCK).build(null)
    }
}

