package com.nik7.upgradecraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class AbstractEntityFluidHandler extends BlockEntity {
    protected FluidTank tank = new FluidTank(FluidAttributes.BUCKET_VOLUME);
    protected final LazyOptional<IFluidHandler> holder = LazyOptional.of(() -> tank);

    public AbstractEntityFluidHandler(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        tank.readFromNBT(tag);
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

    @Override
    public void setRemoved() {
        super.setRemoved();
        this.holder.invalidate();
    }

    public void tick() {

    }

    @Override
    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return holder.cast();
        return super.getCapability(capability, facing);
    }

}
