package com.nik7.upgradecraft.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.nik7.upgradecraft.init.RegisterBlocks.SLIMY_PLANKS_BLOCK;


public class CreativeModeTabUpgC {
    public static final CreativeModeTab ITEM_GROUP = new CreativeModeTab("upgrade_craft") {
        @Override
        public @NotNull ItemStack makeIcon() {
            return new ItemStack(SLIMY_PLANKS_BLOCK.get());
        }

    };
}
