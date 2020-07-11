package com.nik7.upgradecraft.blocks;

import com.nik7.upgradecraft.tileentity.WoodenFluidTankTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class WoodenFluidTankBlock extends AbstractFluidTankBlock {

    public WoodenFluidTankBlock() {
        super(Properties.create(Material.WOOD, MaterialColor.GREEN)
                .notSolid()
                .hardnessAndResistance(2.5F)
                .sound(SoundType.WOOD));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new WoodenFluidTankTileEntity();
    }
}
