package com.nik7.upgradecraft.block;

import com.nik7.upgradecraft.blockentity.AbstractEntityFluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractFluidContainerBlock extends BaseEntityBlock {
    public AbstractFluidContainerBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level worldIn, BlockPos pos) {
        BlockEntity blockEntity = worldIn.getBlockEntity(pos);
        if (blockEntity instanceof AbstractEntityFluidHandler) {
            int fluidAmount = ((AbstractEntityFluidHandler) blockEntity).getFluidAmount();
            int capacity = ((AbstractEntityFluidHandler) blockEntity).getCapacity();

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
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> entityType) {
        if (!level.isClientSide()) {
            return (Level level1, BlockPos blockPos, BlockState state, T entity) -> {
                if (entity instanceof AbstractEntityFluidHandler) {
                    ((AbstractEntityFluidHandler) entity).tick();
                }
            };
        }
        return null;
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState);
}
