package com.nik7.upgradecraft.item

import com.nik7.upgradecraft.CreativeTab
import net.minecraft.world.item.BlockItem
import net.minecraft.world.level.block.Block

class BaseBlockItem(pBlock: Block) : BlockItem(pBlock, Properties().tab(CreativeTab.ITEM_TAB)) {

}
