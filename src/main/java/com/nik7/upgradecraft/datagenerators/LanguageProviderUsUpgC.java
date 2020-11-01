package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.init.RegisterBlocks;
import com.nik7.upgradecraft.init.RegisterItems;
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
        addBlock(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK, "Wooden Fluid Tank");
        addBlock(RegisterBlocks.FUNNEL_BLOCK, "Funnel");
        addBlock(RegisterBlocks.FLUID_FURNACE_BLOCK, "Fluid Furnace");
        addBlock(RegisterBlocks.FLUID_INFUSER_BLOCK, "Fluid Infuser");
        addBlock(RegisterBlocks.CLAY_FLUID_TANK_BLOCK, "Clay Fluid Tank");
        addBlock(RegisterBlocks.CLAY_FLUID_TANK_GLASSED_BLOCK, "Clay Fluid Tank");

        //items
        addItem(RegisterItems.CLAY_INGOT_ITEM, "Clay Ingot");

        //tooltip
        add("tooltip.upgradecraft.tank.glassed", "Transparent");
    }
}
