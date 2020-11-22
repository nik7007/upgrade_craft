package com.nik7.upgradecraft.blocks;

import com.nik7.upgradecraft.tileentity.TerracottaFluidTankTileEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

public class TerracottaFluidTankBlock extends AbstractFluidTankBlock {

    public TerracottaFluidTankBlock() {
        super(Properties.create(Material.ROCK, MaterialColor.ADOBE)
                .setRequiresTool()
                .hardnessAndResistance(1.25F, 4.2F));
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TerracottaFluidTankTileEntity();
    }
}
