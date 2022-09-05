package com.nik7.upgradecraft.item;

import com.nik7.upgradecraft.init.ModCreativeModeTab;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class ModBlockItem extends BlockItem {
    public ModBlockItem(Block block) {
        super(block, new Properties().tab(ModCreativeModeTab.ITEM_GROUP));
    }
}
