package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.init.RegisterBlocks;
import com.nik7.upgradecraft.init.RegisterItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

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

        registerSimpleTexture(RegisterBlocks.FUNNEL_BLOCK, "item/funnel");


        withExistingParent(Objects.requireNonNull(RegisterBlocks.FLUID_FURNACE_BLOCK.get().getRegistryName()).getPath(),
                new ResourceLocation(MOD_ID, "block/fluid_furnace_north_on"));

        withExistingParent(Objects.requireNonNull(RegisterBlocks.FLUID_INFUSER_BLOCK.get().getRegistryName()).getPath(),
                new ResourceLocation(MOD_ID, "block/fluid_infuser_north_on"));

        registerSimpleTexture(RegisterItems.CLAY_INGOT_ITEM, "item/clay_ingot");
    }

    private void registerSimpleTexture(RegistryObject<?> registryObject, String path) {
        singleTexture(Objects.requireNonNull(registryObject.get().getRegistryName()).getPath(), new ResourceLocation("item/handheld"),
                "layer0", new ResourceLocation(MOD_ID, path));
    }
}
