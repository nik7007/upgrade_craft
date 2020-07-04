package com.nik7.upgradecraft.fluids.tanks;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nonnull;

public class FluidTankWrapper<T extends IFluidHandler & IFluidTank> extends FluidTank {

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
}
