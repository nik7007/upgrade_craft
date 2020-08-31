package com.nik7.upgradecraft.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiPredicate;
import java.util.function.Consumer;


public class FluidFurnaceItemHandler extends ItemStackHandler {

    public static final int INPUT = 0;
    public static final int OUTPUT = 1;

    @Nullable
    private final BiPredicate<Integer, ItemStack> validator;
    @Nullable
    private final Consumer<Integer> onSlotChanged;

    public FluidFurnaceItemHandler(Consumer<Integer> onSlotChanged) {
        this(onSlotChanged, null);
    }

    public FluidFurnaceItemHandler(Consumer<Integer> onSlotChanged, BiPredicate<Integer, ItemStack> validator) {
        super(2);
        this.onSlotChanged = onSlotChanged;
        this.validator = validator;
    }

    @Nonnull
    public NonNullList<ItemStack> getItemStack() {
        return stacks;
    }

    @Nonnull
    public ItemStack getInputItemStack() {
        return stacks.get(INPUT);
    }

    @Nonnull
    public ItemStack getOutputItemStack() {
        return stacks.get(OUTPUT);
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return slot == INPUT && (validator == null || validator.test(slot, stack));
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot != OUTPUT) {
            return ItemStack.EMPTY;
        }
        return super.extractItem(slot, amount, simulate);
    }

    @Nonnull
    public ItemStack extractItemForSlot(int slot, int amount, boolean simulate) {
        return super.extractItem(slot, amount, simulate);
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (onSlotChanged != null) {
            onSlotChanged.accept(slot);
        }
    }
}
