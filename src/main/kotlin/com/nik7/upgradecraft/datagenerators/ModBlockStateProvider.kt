package com.nik7.upgradecraft.datagenerators

import com.nik7.upgradecraft.UpgradeCraft
import com.nik7.upgradecraft.block.FluidFurnaceBlock
import com.nik7.upgradecraft.init.ModBlocks
import net.minecraft.core.Direction
import net.minecraft.data.DataGenerator
import net.minecraft.world.level.block.Block
import net.minecraftforge.client.model.generators.BlockStateProvider
import net.minecraftforge.client.model.generators.ConfiguredModel
import net.minecraftforge.client.model.generators.ModelFile
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder.PartialBlockstate
import net.minecraftforge.common.data.ExistingFileHelper
import java.util.*

class ModBlockStateProvider(gen: DataGenerator?, exFileHelper: ExistingFileHelper?) :
    BlockStateProvider(gen, UpgradeCraft.ID, exFileHelper) {

    override fun registerStatesAndModels() {
        simpleBlock(ModBlocks.SLIMY_PLANKS_BLOCK)
        machineBlock(ModBlocks.FLUID_FURNACE_BLOCK, "furnace_machine", "fluid_furnace")
    }


    private fun machineBlock(block: Block, modelName: String, texture: String) {
        var partialBlockstate: PartialBlockstate? = null
        for (direction in Direction.Plane.HORIZONTAL.iterator()) {
            partialBlockstate = if (partialBlockstate == null) {
                getVariantBuilder(block).partialState()
            } else {
                partialBlockstate.partialState()
            }
            partialBlockstate = partialBlockstate
                .with(FluidFurnaceBlock.FACING, direction).with(FluidFurnaceBlock.LIT, false)
                .addModels(*createOrientableMachineModels(modelName, texture, direction, false))
            partialBlockstate = partialBlockstate.partialState()
                .with(FluidFurnaceBlock.FACING, direction).with(FluidFurnaceBlock.LIT, true)
                .addModels(*createOrientableMachineModels(modelName, texture, direction, true))
        }
    }

    private fun createOrientableMachineModels(
        modelName: String,
        texture: String,
        facing: Direction,
        lit: Boolean
    ): Array<ConfiguredModel?> {
        val active = if (lit) "_on" else "_off"
        val model: ModelFile = models()
            .withExistingParent(
                texture + "_" + facing.name.lowercase(Locale.getDefault()) + active,
                modLoc("block/$modelName")
            )
            .texture("all", modLoc("block/$texture"))
            .texture("in", modLoc("block/$texture$active"))
        return ConfiguredModel.builder()
            .modelFile(model)
            .rotationY((facing.toYRot() + 180).toInt() % 360)
            .build()
    }
}
