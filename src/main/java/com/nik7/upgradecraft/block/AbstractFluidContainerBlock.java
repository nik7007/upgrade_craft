package com.nik7.upgradecraft.block;

import com.nik7.upgradecraft.block.entity.AbstractEntityFluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractFluidContainerBlock extends BaseEntityBlock {
    protected AbstractFluidContainerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof AbstractEntityFluidHandler fluidHandler) {
            int fluidAmount = fluidHandler.getFluidAmount();
            int capacity = fluidHandler.getCapacity();

            float compareRaw = 15 * fluidAmount / (float) capacity;
            if (compareRaw > 0 && compareRaw < 1) {
                compareRaw = 1;
            }

            return (int) compareRaw;
        }
        return 0;
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);
}
