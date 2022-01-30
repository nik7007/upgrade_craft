package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.init.RegisterBlocks;
import net.minecraft.data.DataGenerator;

import java.util.Locale;

public class LanguageProviderUsUpgC extends AbstractLanguageProviderUpgC {
    public LanguageProviderUsUpgC(DataGenerator gen) {
        super(gen, Locale.US);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.upgrade_craft", "UpgradeCraft");

        addBlock(RegisterBlocks.SLIMY_PLANKS_BLOCK, "Slimy Planks");
        addBlock(RegisterBlocks.WOODEN_FLUID_TANK_BLOCK, "Wooden Fluid Tank");
        addBlock(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK, "Wooden Fluid Tank");

        addBlock(RegisterBlocks.FUNNEL_BLOCK, "Funnel");
        addBlock(RegisterBlocks.FLUID_FURNACE_BLOCK, "Fluid Furnace");

        //tooltip
        add("tooltip.upgradecraft.tank.glassed", "Transparent");
    }
}
