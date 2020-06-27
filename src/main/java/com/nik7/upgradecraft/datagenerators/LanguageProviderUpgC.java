package com.nik7.upgradecraft.datagenerators;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.Locale;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public abstract class LanguageProviderUpgC extends LanguageProvider {
    public LanguageProviderUpgC(DataGenerator gen,  Locale locale) {
        super(gen, MOD_ID, locale.toString().toLowerCase());
    }
}
