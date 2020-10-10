package com.nik7.upgradecraft.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

public abstract class AbstractMachineItemHandler extends ItemStackHandler {

    @Nullable
    protected final Consumer<Integer> onSlotChanged;
    @Nullable
    protected final BiPredicate<Integer, ItemStack> validator;

    private final int[] inputSlots;
    private final int[] outputSlots;


    public AbstractMachineItemHandler(int size,
                                      @Nullable Consumer<Integer> onSlotChanged,
                                      @Nullable BiPredicate<Integer, ItemStack> validator) {
        super(size);
        this.onSlotChanged = onSlotChanged;
        this.validator = validator;
        this.inputSlots = getInputSlots();
        this.outputSlots = getOutputSlots();
    }

    protected abstract int[] getInputSlots();

    protected abstract int[] getOutputSlots();

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return Arrays.stream(inputSlots).anyMatch(input -> input == slot)
                && (validator == null || validator.test(slot, stack));
    }

    @Nonnull
    public NonNullList<ItemStack> getItemStack() {
        return stacks;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (Arrays.stream(outputSlots).noneMatch(input -> input == slot)) {
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
