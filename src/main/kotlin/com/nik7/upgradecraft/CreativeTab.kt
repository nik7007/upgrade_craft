package com.nik7.upgradecraft

import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items

object CreativeTab {
    val ITEM_TAB = object : CreativeModeTab("upgrade_craft") {
        override fun makeIcon(): ItemStack {
            return ItemStack(Items.STICK)
        }
    }
}
