package com.nik7.upgradecraft.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public interface MachineTileEntity {

    NonNullList<ItemStack> getItems();
}
