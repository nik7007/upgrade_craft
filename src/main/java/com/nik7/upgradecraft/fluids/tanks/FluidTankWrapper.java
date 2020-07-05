package com.nik7.upgradecraft.fluids.tanks;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class FluidTankWrapper<T extends FluidTank> extends FluidTank {

    private T internalTank;

    public FluidTankWrapper(T internalTank) {
        super(internalTank.getCapacity());
        this.internalTank = internalTank;
    }


    public T getInternalTank() {
        return internalTank;
    }

    public void setInternalTank(T internalTank) {
        this.internalTank = internalTank;
    }

    @Override
    public FluidTank setCapacity(int capacity) {
        return internalTank.setCapacity(capacity);
    }

    @Override
    public FluidTank setValidator(Predicate<FluidStack> validator) {
        return internalTank.setValidator(validator);
    }

    @Override
    public FluidTank readFromNBT(CompoundNBT nbt) {
        return internalTank.readFromNBT(nbt);
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT nbt) {
        return internalTank.writeToNBT(nbt);
    }


    @Nonnull
    @Override
    public FluidStack getFluid() {
        return internalTank.getFluid();
    }

    @Override
    public int getFluidAmount() {
        return internalTank.getFluidAmount();
    }

    @Override
    public int getCapacity() {
        return internalTank.getCapacity();
    }

    @Override
    public boolean isFluidValid(FluidStack stack) {
        return internalTank.isFluidValid(stack);
    }

    @Override
    public int getTanks() {
        return internalTank.getTanks();
    }

    @Nonnull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return internalTank.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return internalTank.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @Nonnull FluidStack stack) {
        return internalTank.isFluidValid(tank, stack);
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return internalTank.fill(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return internalTank.drain(resource, action);
    }

    @Nonnull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return internalTank.drain(maxDrain, action);
    }

    @Override
    public boolean isEmpty() {
        return internalTank.isEmpty();
    }

    @Override
    public int getSpace() {
        return internalTank.getSpace();
    }

}
