package com.nik7.upgradecraft.crafting;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public final class TagUpgC {
    private TagUpgC() {
    }

    public static final class Blocks {
        public static final Tag.Named<Block> WOODEN_TANK = tag("tank/wooden");

        private Blocks() {
        }

        private static Tag.Named<Block> tag(String name) {
            return BlockTags.bind(MOD_ID + ":" + name);
        }
    }

    public static final class Items {
        public static final Tag.Named<Item> WOODEN_TANK = tag("tank/wooden");

        private Items() {
        }

        private static Tag.Named<Item> tag(String name) {
            return ItemTags.bind(MOD_ID + ":" + name);
        }
    }
}
