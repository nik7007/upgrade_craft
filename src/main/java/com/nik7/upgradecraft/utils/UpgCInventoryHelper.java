package com.nik7.upgradecraft.utils;


import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class UpgCInventoryHelper {
    private UpgCInventoryHelper() {
    }

    public static void substituteItemToStack(ItemStack originalItemStack,
                                             ItemStack newItem,
                                             Player player,
                                             InteractionHand handIn) {

        if (originalItemStack.getCount() == 1) {
            player.setItemInHand(handIn, newItem);
        } else if (originalItemStack.getCount() > 1) {
            ItemStack copy = originalItemStack.copy();
            if (!player.getInventory().add(newItem)) {
                player.drop(newItem, false, true);
            }
            copy.shrink(1);
            player.setItemInHand(handIn, copy);
        }
    }
}
