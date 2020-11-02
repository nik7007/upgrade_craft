package com.nik7.upgradecraft.tileentity;

import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Random;

import static com.nik7.upgradecraft.blocks.AbstractFluidTankBlock.WATERLOGGED;
import static com.nik7.upgradecraft.blocks.BasicWoodenFluidTank.BURNING;

public interface BaseWoodenFluidTankTileEntity {

    Random RANDOM = new Random();


    default void updateBurningState(World world, BlockPos pos, BlockState blockState, boolean isFluidHot) {
        boolean burning = blockState.get(BURNING);
        if (burning != isFluidHot) {
            world.setBlockState(pos, blockState.with(BURNING, isFluidHot), Constants.BlockFlags.DEFAULT_AND_RERENDER);
        }
    }

    default void setBlockInFire(AbstractFluidTankTileEntity tileEntity, int tickNumber) {
        boolean waterlogged = tileEntity.getBlockState().get(WATERLOGGED);
        if (!waterlogged && tickNumber < 6 && tileEntity.isFluidHot()) {
            if (RANDOM.nextFloat() < 0.015f) {
                setInFire(tileEntity.getWorld(), tileEntity.getPos().offset(Direction.values()[tickNumber]));
            }
        }
    }

    default void setInFire(World world, BlockPos pos) {
        if (AbstractFireBlock.canLightBlock(world, pos, Direction.DOWN)) {
            BlockState fireBlockState = AbstractFireBlock.getFireForPlacement(world, pos);
            world.setBlockState(pos, fireBlockState);
        }
    }
}
