package com.nik7.upgradecraft.items;

import com.nik7.upgradecraft.init.ItemGroupUpgC;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

import java.util.concurrent.Callable;
import java.util.function.Supplier;

public class UpgCBlockItem extends BlockItem {
    public UpgCBlockItem(Block blockIn) {
        super(blockIn, new Item.Properties().group(ItemGroupUpgC.ITEM_GROUP));
    }

    public UpgCBlockItem(Block blockIn, Supplier<Callable<ItemStackTileEntityRenderer>> supplier) {
        super(blockIn, new Item.Properties().group(ItemGroupUpgC.ITEM_GROUP).setISTER(supplier));
    }
}
