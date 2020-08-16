package com.nik7.upgradecraft.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import static com.nik7.upgradecraft.blocks.BasicWoodenFluidTank.BURNING;

public interface BaseWoodenFluidTankTileEntity {

    default void updateBurningState(World world, BlockPos pos, BlockState blockState, boolean isFluidHot) {
        boolean burning = blockState.get(BURNING);
        if (burning != isFluidHot) {
            world.setBlockState(pos, blockState.with(BURNING, isFluidHot),
                    Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.RERENDER_MAIN_THREAD);
        }
    }
}
