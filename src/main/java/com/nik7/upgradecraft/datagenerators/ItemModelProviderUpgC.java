package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.init.RegisterBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class ItemModelProviderUpgC extends ItemModelProvider {
    public ItemModelProviderUpgC(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(Objects.requireNonNull(RegisterBlocks.SLIMY_PLANKS_BLOCK.get().getRegistryName()).getPath(),
                modLoc("block/slimy_planks_block"));

        withExistingTankModel(RegisterBlocks.WOODEN_FLUID_TANK_BLOCK, "wooden_fluid_tank", false);
        withExistingTankModel(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK, "wooden_fluid_tank", true);

        registerSimpleTexture(RegisterBlocks.FUNNEL_BLOCK, "item/funnel");
    }

    private void withExistingTankModel(RegistryObject<? extends Block> registryObject, String typeName, boolean glassed) {
        withExistingParent(Objects.requireNonNull(registryObject.get().getRegistryName()).getPath(),
                modLoc("block/" + typeName + (glassed ? "/fluid_tank_glassed" : "/fluid_tank")));
    }

    private void registerSimpleTexture(RegistryObject<?> registryObject, String path) {
        singleTexture(Objects.requireNonNull(registryObject.get().getRegistryName()).getPath(), mcLoc("item/generated"),
                "layer0", modLoc(path));
    }
}
