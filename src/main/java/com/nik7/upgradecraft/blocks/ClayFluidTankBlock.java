package com.nik7.upgradecraft.blocks;

import com.nik7.upgradecraft.tileentity.ClayFluidTankTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class ClayFluidTankBlock extends AbstractFluidTankBlock {
    public ClayFluidTankBlock() {
        super(Properties.create(Material.CLAY).notSolid().
                hardnessAndResistance(0.6F).sound(SoundType.GROUND));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ClayFluidTankTileEntity();
    }
}
