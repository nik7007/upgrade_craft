package com.nik7.upgradecraft.capabilities;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
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
    public List<ItemStack> getItemStack() {
        return stacks;
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

    @Override
    protected void onContentsChanged(int slot) {
        if (onSlotChanged != null) {
            onSlotChanged.accept(slot);
        }
    }
}
