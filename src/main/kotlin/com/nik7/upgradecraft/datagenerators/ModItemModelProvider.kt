package com.nik7.upgradecraft.datagenerators

import com.nik7.upgradecraft.UpgradeCraft
import com.nik7.upgradecraft.init.ModBlocks
import net.minecraft.data.DataGenerator
import net.minecraftforge.client.model.generators.ItemModelProvider
import net.minecraftforge.common.data.ExistingFileHelper

class ModItemModelProvider(generator: DataGenerator?, existingFileHelper: ExistingFileHelper?) :
    ItemModelProvider(generator, UpgradeCraft.ID, existingFileHelper) {

    override fun registerModels() {
        withExistingParent(
            ModBlocks.SLIMY_PLANKS_BLOCK.registryName!!.path,
            modLoc("block/slimy_planks_block")
        )

        withExistingParent(
            ModBlocks.FLUID_FURNACE_BLOCK.registryName!!.path,
            modLoc("block/fluid_furnace_north_on")
        )
    }
}
