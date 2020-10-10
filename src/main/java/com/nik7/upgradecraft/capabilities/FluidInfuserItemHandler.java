package com.nik7.upgradecraft.capabilities;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class FluidInfuserItemHandler extends AbstractMachineItemHandler {

    public static final int DISSOLVE = 0;
    public static final int INFUSE = 1;
    public static final int OUTPUT = 2;

    public FluidInfuserItemHandler(Consumer<Integer> onSlotChanged) {
        this(onSlotChanged, null);
    }

    public FluidInfuserItemHandler(@Nullable Consumer<Integer> onSlotChanged,
                                   @Nullable BiPredicate<Integer, ItemStack> validator) {
        super(3, onSlotChanged, validator);
    }

    @Override
    protected int[] getInputSlots() {
        return new int[]{DISSOLVE, INFUSE};
    }

    @Override
    protected int[] getOutputSlots() {
        return new int[]{OUTPUT};
    }

    public ItemStack getDissolveItemStack() {
        return stacks.get(DISSOLVE);
    }

    public ItemStack getInfuseItemStack() {
        return stacks.get(INFUSE);
    }

    @Nonnull
    public ItemStack getOutputItemStack() {
        return stacks.get(OUTPUT);
    }
}
