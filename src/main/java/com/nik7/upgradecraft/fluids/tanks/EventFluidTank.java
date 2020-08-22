package com.nik7.upgradecraft.fluids.tanks;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.Predicate;


public class EventFluidTank extends FluidTank {

    @Nullable
    private final Consumer<Void> onChange;

    public EventFluidTank(int capacity, Consumer<Void> onChange) {
        super(capacity);
        this.onChange = onChange;
    }

    public EventFluidTank(int capacity, Consumer<Void> onChange, Predicate<FluidStack> validator) {
        this(capacity, onChange);
        this.validator = validator;
    }


    @Override
    protected void onContentsChanged() {
        if (onChange != null) {
            onChange.accept(null);
        }
    }
}
