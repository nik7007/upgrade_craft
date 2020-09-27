package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.init.RegisterBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Objects;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class ItemModelProviderUpgC extends ItemModelProvider {
    public ItemModelProviderUpgC(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(Objects.requireNonNull(RegisterBlocks.SLIMY_PLANKS_BLOCK.get().getRegistryName()).getPath(),
                new ResourceLocation(MOD_ID, "block/slimy_planks_block"));

        withExistingParent(Objects.requireNonNull(RegisterBlocks.WOODEN_FLUID_TANK_BLOCK.get().getRegistryName()).getPath(),
                new ResourceLocation(MOD_ID, "block/wooden_fluid_tank"));

        withExistingParent(Objects.requireNonNull(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get().getRegistryName()).getPath(),
                new ResourceLocation(MOD_ID, "block/wooden_fluid_tank_glassed"));

        singleTexture(Objects.requireNonNull(RegisterBlocks.FUNNEL_BLOCK.get().getRegistryName()).getPath(), new ResourceLocation("item/handheld"),
                "layer0", new ResourceLocation(MOD_ID, "item/funnel"));

        withExistingParent(Objects.requireNonNull(RegisterBlocks.FLUID_FURNACE_BLOCK.get().getRegistryName()).getPath(),
                new ResourceLocation(MOD_ID, "block/fluid_furnace_north_on"));
    }
}
