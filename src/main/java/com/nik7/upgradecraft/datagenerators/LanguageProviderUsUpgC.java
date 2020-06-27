package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.init.RegisterBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.Locale;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class LanguageProviderUsUpgC extends LanguageProvider {
    public LanguageProviderUsUpgC(DataGenerator gen) {
        super(gen, MOD_ID, Locale.US.toString().toLowerCase());
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.upgrade_craft", "UpgradeCraft");

        //block
        addBlock(RegisterBlocks.SLIMY_PLANKS_BLOCK, "Slimy Planks");
    }
}
