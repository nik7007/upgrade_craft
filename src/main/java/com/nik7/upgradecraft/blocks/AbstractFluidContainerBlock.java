package com.nik7.upgradecraft.blocks;

import com.nik7.upgradecraft.tileentity.AbstractTileFluidHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractFluidContainerBlock extends Block {
    public AbstractFluidContainerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasComparatorInputOverride(BlockState state) {
        return true;
    }

    @Override
    public int getComparatorInputOverride(BlockState blockState, World worldIn, BlockPos pos) {
        TileEntity tileEntity = worldIn.getTileEntity(pos);
        if (tileEntity instanceof AbstractTileFluidHandler) {
            int fluidAmount = ((AbstractTileFluidHandler) tileEntity).getFluidAmount();
            int capacity = ((AbstractTileFluidHandler) tileEntity).getCapacity();

            float compareRaw = 15 * fluidAmount / (float) capacity;
            if (compareRaw > 0 && compareRaw < 1) {
                compareRaw = 1;
            }

            return (int) compareRaw;
        }
        return 0;
    }
}
