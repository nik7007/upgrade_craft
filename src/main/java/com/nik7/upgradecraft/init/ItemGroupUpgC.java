package com.nik7.upgradecraft.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import static com.nik7.upgradecraft.init.RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK;

public class ItemGroupUpgC {

    public static final ItemGroup ITEM_GROUP = new ItemGroup("upgrade_craft") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(WOODEN_FLUID_TANK_GLASSED_BLOCK.get());
        }
    };
}
