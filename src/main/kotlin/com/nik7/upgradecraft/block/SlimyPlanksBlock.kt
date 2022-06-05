package com.nik7.upgradecraft.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.material.Material
import net.minecraft.world.level.material.MaterialColor

class SlimyPlanksBlock: Block(
    Properties.of(Material.WOOD, MaterialColor.DIRT)
        .strength(2.0f, 3.0f)
        .friction(1.05f)
        .sound(SoundType.WOOD)) {

    override fun getFlammability(state: BlockState?, world: BlockGetter?, pos: BlockPos?, face: Direction?): Int {
        return (300 * 0.25).toInt()
    }

    override fun getFireSpreadSpeed(state: BlockState?, world: BlockGetter?, pos: BlockPos?, face: Direction?): Int {
        return 60
    }
}
