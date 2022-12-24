package com.nik7.upgradecraft.fluids.capabillity;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class EventFluidTank extends FluidTank {
    @Nullable
    protected Consumer<FluidStack> onChange;

    public EventFluidTank(int capacity) {
        this(capacity, null);
    }

    public EventFluidTank(int capacity, @Nullable Consumer<FluidStack> onChange) {
        super(capacity);
        this.onChange = onChange;
    }

    public EventFluidTank(int capacity, @Nullable Consumer<FluidStack> onChange, Predicate<FluidStack> validator) {
        this(capacity, onChange);
        this.validator = validator;
    }

    @Override
    public FluidTank readFromNBT(CompoundTag nbt) {

        FluidStack fluid = FluidStack.loadFluidStackFromNBT(nbt);
        this.capacity = nbt.getInt("capacity");
        setFluid(fluid);
        return this;
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt) {

        fluid.writeToNBT(nbt);
        nbt.putInt("capacity", capacity);
        return nbt;
    }

    public EventFluidTank setOnChange(Consumer<FluidStack> onChange) {
        this.onChange = onChange;
        return this;
    }

    @Override
    protected void onContentsChanged() {
        if (onChange != null) {
            onChange.accept(new FluidStack(this.fluid, this.fluid.getAmount()));
        }
    }

}
