package com.nik7.upgradecraft.inventory

import net.minecraft.core.NonNullList
import net.minecraft.world.item.ItemStack
import net.minecraftforge.items.ItemStackHandler
import java.util.function.BiPredicate
import java.util.function.Consumer

class ModItemStackHandler(
    stacks: NonNullList<ItemStack>,
    private var validateInput: BiPredicate<Int, ItemStack>?,
    private var onChangeItemInSlot: Consumer<Int>?
) : ItemStackHandler(stacks) {

    init {
        if (validateInput == null) {
            validateInput =
                BiPredicate { _: Int?, _: ItemStack? -> true }
        }
        if (onChangeItemInSlot == null) {
            onChangeItemInSlot = Consumer {}
        }
    }

    constructor(
        size: Int,
        validateInput: BiPredicate<Int, ItemStack>?,
        onChangeItemInSlot: Consumer<Int>?
    ) : this(
        NonNullList.withSize<ItemStack>(
            size,
            ItemStack.EMPTY
        ), validateInput, onChangeItemInSlot
    )

    constructor(
        validateInput: BiPredicate<Int, ItemStack>?,
        onChangeItemInSlot: Consumer<Int>?
    ) : this(1, validateInput, onChangeItemInSlot)

    override fun isItemValid(slot: Int, stack: ItemStack): Boolean {
        return validateInput!!.test(slot, stack)
    }

    override fun onContentsChanged(slot: Int) {
        onChangeItemInSlot!!.accept(slot)
    }

}
