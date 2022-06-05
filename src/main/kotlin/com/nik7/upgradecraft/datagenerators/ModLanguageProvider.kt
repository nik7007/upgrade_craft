package com.nik7.upgradecraft.datagenerators

import com.nik7.upgradecraft.UpgradeCraft
import com.nik7.upgradecraft.init.ModBlocks
import net.minecraft.data.DataGenerator
import net.minecraftforge.common.data.LanguageProvider
import java.util.*

abstract class ModLanguageProvider(gen: DataGenerator, locale: Locale) : LanguageProvider(gen, UpgradeCraft.ID,
    locale.toString().lowercase(Locale.getDefault())
) {

    class UsLanguageProvider(gen: DataGenerator) : ModLanguageProvider(gen, Locale.US) {
        override fun addTranslations() {
            add("itemGroup.upgrade_craft", "UpgradeCraft")
            add(ModBlocks.SLIMY_PLANKS_BLOCK, "Slimy Planks")
            add(ModBlocks.FLUID_FURNACE_BLOCK, "Fluid Furnace")
        }

    }

}
