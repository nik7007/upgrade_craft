package com.nik7.upgradecraft.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

public class ItemStackHandlerUpgC extends ItemStackHandler {
    private BiPredicate<Integer, ItemStack> validateInput = (integer, itemStack) -> true;
    private Consumer<Integer> onChangeItemInSlot = integer -> {
    };


    public ItemStackHandlerUpgC(BiPredicate<Integer, ItemStack> validateInput,
                                Consumer<Integer> onChangeItemInSlot) {
        this(1, validateInput, onChangeItemInSlot);
    }

    public ItemStackHandlerUpgC(int size,
                                BiPredicate<Integer, ItemStack> validateInput,
                                Consumer<Integer> onChangeItemInSlot) {
        this(NonNullList.withSize(size, ItemStack.EMPTY), validateInput, onChangeItemInSlot);
    }

    public ItemStackHandlerUpgC(NonNullList<ItemStack> stacks,
                                BiPredicate<Integer, ItemStack> validateInput,
                                Consumer<Integer> onChangeItemInSlot) {
        super(stacks);
        if (validateInput != null) {
            this.validateInput = validateInput;
        }
        if (onChangeItemInSlot != null) {
            this.onChangeItemInSlot = onChangeItemInSlot;
        }
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return validateInput.test(slot, stack);
    }

    @Override
    protected void onContentsChanged(int slot) {
        onChangeItemInSlot.accept(slot);
    }
}
