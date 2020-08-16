package com.nik7.upgradecraft.blocks;

import com.nik7.upgradecraft.tileentity.WoodenFluidTankTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class WoodenFluidTankBlock extends BasicWoodenFluidTank {

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WoodenFluidTankTileEntity();
    }
}
