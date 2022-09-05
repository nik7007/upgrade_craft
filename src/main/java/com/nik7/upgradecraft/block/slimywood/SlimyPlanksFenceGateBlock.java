package com.nik7.upgradecraft.block.slimywood;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import static com.nik7.upgradecraft.block.slimywood.SlimyPlanksBlock.FIRE_SPREAD_SPEED;
import static com.nik7.upgradecraft.block.slimywood.SlimyPlanksBlock.FLAMMABILITY;

public class SlimyPlanksFenceGateBlock extends FenceGateBlock {
    public SlimyPlanksFenceGateBlock(Block block) {
        super(BlockBehaviour.Properties.copy(block));
    }
    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return FLAMMABILITY;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return FIRE_SPREAD_SPEED;
    }
}
