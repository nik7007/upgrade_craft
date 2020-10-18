package com.nik7.upgradecraft.container;

import com.nik7.upgradecraft.capabilities.AbstractMachineItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class MachineInputSlot extends SlotItemHandler {
    private final AbstractMachineItemHandler itemHandler;
    private final int index;

    public MachineInputSlot(AbstractMachineItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.itemHandler = itemHandler;
        this.index = index;
    }

    @Override
    public boolean canTakeStack(PlayerEntity playerIn) {
        return !this.itemHandler.extractItemForSlot(index, 1, true).isEmpty();
    }

    @Nonnull
    @Override
    public ItemStack decrStackSize(int amount) {
        return this.itemHandler.extractItemForSlot(index, amount, false);
    }
}
