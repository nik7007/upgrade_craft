package com.nik7.upgradecraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Random;

import static com.nik7.upgradecraft.block.WoodenFluidTankBlock.BURNING;
import static com.nik7.upgradecraft.block.WoodenFluidTankBlock.WATERLOGGED;

public interface BaseWoodenFluidTankEntity {
    Random RANDOM = new Random();

    default AbstractFluidTankEntity self() {
        return (AbstractFluidTankEntity) this;
    }

    default void updateBurningState(AbstractFluidTankEntity fluidTankEntity) {
        Level level = fluidTankEntity.getLevel();
        if (level == null || level.isClientSide()) {
            return;
        }
        BlockPos pos = fluidTankEntity.getBlockPos();
        BlockState blockState = fluidTankEntity.getBlockState();
        boolean isFluidHot = fluidTankEntity.isFluidHot();
        boolean burning = blockState.getValue(BURNING);
        if (burning != isFluidHot) {
            level.setBlock(pos, blockState.setValue(BURNING, isFluidHot), Block.UPDATE_ALL_IMMEDIATE);
        }
    }

    default void setBlockInFire(int tickNumber) {
        var blockEntity = self();
        Level level = blockEntity.getLevel();
        if (level == null || level.isClientSide()) {
            return;
        }
        boolean waterlogged = blockEntity.getBlockState().getValue(WATERLOGGED);
        if (!waterlogged && tickNumber < 6 && blockEntity.isFluidHot()) {
            if (RANDOM.nextFloat() < 0.15f) {
                setInFire(level, blockEntity.getBlockPos().offset(Direction.values()[tickNumber].getNormal()));
            }
        }
    }

    default void setInFire(Level level, BlockPos pos) {
        if (BaseFireBlock.canBePlacedAt(level, pos, Direction.DOWN)) {
            BlockState fireBlockState = BaseFireBlock.getState(level, pos);
            level.setBlockAndUpdate(pos, fireBlockState);
        }
    }
}
