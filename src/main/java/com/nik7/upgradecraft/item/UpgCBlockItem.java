package com.nik7.upgradecraft.item;

import com.nik7.upgradecraft.init.CreativeModeTabUpgC;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class UpgCBlockItem extends BlockItem {
    public UpgCBlockItem(Block block) {
        super(block, new Item.Properties().tab(CreativeModeTabUpgC.ITEM_GROUP));
    }
}
