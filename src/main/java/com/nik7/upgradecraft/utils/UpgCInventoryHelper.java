package com.nik7.upgradecraft.utils;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

public final class UpgCInventoryHelper {
    private UpgCInventoryHelper() {
    }

    public static void substituteItemToStack(ItemStack originalItemStack,
                                             ItemStack newItem,
                                             PlayerEntity player,
                                             Hand handIn) {

        if (originalItemStack.getCount() == 1) {
            player.setHeldItem(handIn, newItem);
        } else if (originalItemStack.getCount() > 1 && player.inventory.addItemStackToInventory(newItem)) {
            originalItemStack.shrink(1);
        } else {
            player.dropItem(newItem, false, true);
            originalItemStack.shrink(1);
        }
    }
}
