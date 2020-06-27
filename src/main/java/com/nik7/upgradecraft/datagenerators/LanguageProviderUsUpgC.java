package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.init.RegisterBlocks;
import net.minecraft.data.DataGenerator;

import java.util.Locale;

public class LanguageProviderUsUpgC extends LanguageProviderUpgC {
    public LanguageProviderUsUpgC(DataGenerator gen) {
        super(gen, Locale.US);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.upgrade_craft", "UpgradeCraft");

        //block
        addBlock(RegisterBlocks.SLIMY_PLANKS_BLOCK, "Slimy Planks");
        addBlock(RegisterBlocks.WOODEN_FLUID_TANK_BLOCK, "Wooden Fluid Tank");
    }
}
