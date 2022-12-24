package com.nik7.upgradecraft.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidHandlerBlockEntity;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractEntityFluidHandler extends FluidHandlerBlockEntity {

    public AbstractEntityFluidHandler(@NotNull BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state) {
        super(blockEntityType, pos, state);
    }

    public FluidStack getFluid() {
        return this.tank.getFluid().copy();
    }

    public int getFluidAmount() {
        return this.tank.getFluidAmount();
    }

    public int getCapacity() {
        return this.tank.getCapacity();
    }

    protected void updateComparator() {
        updateComparator(this);
    }

    protected static void updateComparator(BlockEntity entity) {
        if (entity.getLevel() == null) {
            return;
        }
        entity.getLevel().updateNeighbourForOutputSignal(entity.getBlockPos(), entity.getBlockState().getBlock());
    }
}
