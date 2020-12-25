package com.nik7.upgradecraft.items;

import com.nik7.upgradecraft.init.ItemGroupUpgC;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class UpgCBlockItem extends BlockItem {
    public UpgCBlockItem(Block blockIn) {
        super(blockIn, new Item.Properties().group(ItemGroupUpgC.ITEM_GROUP));
    }
}
