package com.nik7.upgradecraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractEntityFluidHandler extends TileFluidHandler {

    public AbstractEntityFluidHandler(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        this.tank.writeToNBT(compoundTag);
    }

    protected void updateComparator() {
        updateComparator(this);
    }

    protected void updateComparator(BlockEntity entity) {
        if (entity.getLevel() == null) {
            return;
        }
        entity.getLevel().updateNeighbourForOutputSignal(entity.getBlockPos(), entity.getBlockState().getBlock());
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

    public void tick() {

    }

}
