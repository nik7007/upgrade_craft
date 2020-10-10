package com.nik7.upgradecraft.capabilities;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiPredicate;
import java.util.function.Consumer;


public class FluidFurnaceItemHandler extends AbstractMachineItemHandler {

    public static final int INPUT = 0;
    public static final int OUTPUT = 1;

    public FluidFurnaceItemHandler(Consumer<Integer> onSlotChanged) {
        this(onSlotChanged, null);
    }

    public FluidFurnaceItemHandler(@Nullable Consumer<Integer> onSlotChanged,
                                   @Nullable BiPredicate<Integer, ItemStack> validator) {
        super(2, onSlotChanged, validator);
    }

    @Override
    protected int[] getInputSlots() {
        return new int[]{INPUT};
    }

    @Override
    protected int[] getOutputSlots() {
        return new int[]{OUTPUT};
    }

    @Nonnull
    public ItemStack getInputItemStack() {
        return stacks.get(INPUT);
    }

    @Nonnull
    public ItemStack getOutputItemStack() {
        return stacks.get(OUTPUT);
    }

}
