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
        } else if (originalItemStack.getCount() > 1 && player.getInventory().add(newItem)) {
            originalItemStack.shrink(1);
        } else {
            player.drop(newItem, false, true);
            originalItemStack.shrink(1);
        }
    }
}
