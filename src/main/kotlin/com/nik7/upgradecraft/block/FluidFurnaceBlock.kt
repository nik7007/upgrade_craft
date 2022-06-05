package com.nik7.upgradecraft.block

import com.nik7.upgradecraft.blockentity.FluidFurnaceEntity
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.block.*
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.block.state.properties.BooleanProperty
import net.minecraft.world.level.block.state.properties.DirectionProperty
import net.minecraft.world.level.material.Material

class FluidFurnaceBlock : BaseEntityBlock(Properties.of(Material.STONE).noOcclusion()) {

    companion object {
        val FACING: DirectionProperty = HorizontalDirectionalBlock.FACING
        val LIT: BooleanProperty = BlockStateProperties.LIT
    }

    init {
        registerDefaultState(
            stateDefinition.any().setValue(FACING, Direction.NORTH)
                .setValue(LIT, false)
        )
    }


    override fun newBlockEntity(pPos: BlockPos, pState: BlockState): BlockEntity = FluidFurnaceEntity(pPos, pState)


    override fun getStateForPlacement(blockPlaceContext: BlockPlaceContext): BlockState? {
        return defaultBlockState().setValue(
            FACING,
            blockPlaceContext.horizontalDirection.opposite
        )
    }

    override fun getRenderShape(blockState: BlockState?): RenderShape {
        return RenderShape.MODEL
    }

    override fun rotate(blockState: BlockState, rotation: Rotation): BlockState? {
        return blockState.setValue(
            FACING,
            rotation.rotate(blockState.getValue(FACING))
        )
    }

    override fun mirror(blockState: BlockState, mirror: Mirror): BlockState? {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)))
    }

    override fun createBlockStateDefinition(blockStateBuilder: StateDefinition.Builder<Block?, BlockState?>) {
        blockStateBuilder.add(FACING, LIT)
    }
}
