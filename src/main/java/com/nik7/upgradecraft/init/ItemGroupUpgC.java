package com.nik7.upgradecraft.init;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import static com.nik7.upgradecraft.init.RegisterBlocks.SLIMY_PLANKS_BLOCK;

public class ItemGroupUpgC {

    public static final ItemGroup ITEM_GROUP = new ItemGroup("upgrade_craft") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(SLIMY_PLANKS_BLOCK.get());
        }
    };
}
