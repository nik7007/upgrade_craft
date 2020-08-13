package com.nik7.upgradecraft.crafting;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public final class TagUpgC {
    private TagUpgC() {
    }

    public static final class Blocks {
        public static final ITag.INamedTag<Block> WOODEN_TANK = tag("tank/wooden");

        private Blocks() {
        }

        private static ITag.INamedTag<Block> tag(String name) {
            return BlockTags.makeWrapperTag(MOD_ID + ":" + name);
        }
    }

    public static final class Items {
        public static final ITag.INamedTag<Item> WOODEN_TANK = tag("tank/wooden");

        private Items() {
        }

        private static ITag.INamedTag<Item> tag(String name) {
            return ItemTags.makeWrapperTag(MOD_ID + ":" + name);
        }
    }
}
