package com.nik7.upgradecraft.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ModCreativeModeTab {

    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab("upgrade_craft") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(RegisterBlock.SLIMY_PLANKS_BLOCK.get());
        }

    };
}
