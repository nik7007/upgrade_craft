package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.init.RegisterBlock;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

import java.util.Locale;

import static com.nik7.upgradecraft.UpgradeCraft.MODID;

public abstract class UpgCLanguageProvider extends LanguageProvider {
    public UpgCLanguageProvider(DataGenerator gen, Locale locale) {
        super(gen, MODID, locale.toString().toLowerCase());
    }

    public static class US extends UpgCLanguageProvider {

        public US(DataGenerator gen) {
            super(gen, Locale.US);
        }

        @Override
        protected void addTranslations() {
            add("itemGroup.upgrade_craft", "UpgradeCraft");
            addBlock(RegisterBlock.SLIMY_PLANKS_BLOCK, "Slimy Planks");
            addBlock(RegisterBlock.SLIMY_PLANKS_STAIRS_BLOCK, "Slimy Stairs");
            addBlock(RegisterBlock.SLIMY_PLANKS_SLAB_BLOCK, "Slimy Slab");
            addBlock(RegisterBlock.SLIMY_PLANKS_FENCE_BLOCK, "Slimy Fence");
            addBlock(RegisterBlock.SLIMY_PLANKS_FENCE_GATE_BLOCK, "Slimy Fence Gate");
            addBlock(RegisterBlock.SLIMY_PLANKS_DOOR_BLOCK, "Slimy Door");
            addBlock(RegisterBlock.SLIMY_PLANKS_TRAP_DOOR_BLOCK, "Slimy Trapdoor");

            add("tooltip.upgradecraft.tank.glassed", "Glassed");
            addBlock(RegisterBlock.WOODEN_FLUID_TANK_BLOCK, "Wooden fluid tank");
            addBlock(RegisterBlock.WOODEN_GLASSED_FLUID_TANK_BLOCK, "Wooden fluid tank");
        }
    }
}
