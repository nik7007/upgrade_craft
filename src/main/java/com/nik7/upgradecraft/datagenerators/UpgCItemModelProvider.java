package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.init.RegisterBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import static com.nik7.upgradecraft.UpgradeCraft.MODID;

public class UpgCItemModelProvider extends ItemModelProvider {
    public UpgCItemModelProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(RegisterBlock.SLIMY_PLANKS_BLOCK.getId().getPath(), modLoc("block/slimy_planks_block"));
        stairs(RegisterBlock.SLIMY_PLANKS_STAIRS_BLOCK.getId().getPath(),
                modLoc("block/slimy_planks_block"),
                modLoc("block/slimy_planks_block"),
                modLoc("block/slimy_planks_block")
        );
        slab(RegisterBlock.SLIMY_PLANKS_SLAB_BLOCK.getId().getPath(),
                modLoc("block/slimy_planks_block"),
                modLoc("block/slimy_planks_block"),
                modLoc("block/slimy_planks_block"));

        fenceInventory(RegisterBlock.SLIMY_PLANKS_FENCE_BLOCK.getId().getPath(), modLoc("block/slimy_planks_block"));
        fenceGate(RegisterBlock.SLIMY_PLANKS_FENCE_GATE_BLOCK.getId().getPath(), modLoc("block/slimy_planks_block"));
        registerSimpleTexture(RegisterBlock.SLIMY_PLANKS_DOOR_BLOCK, "item/slimy_door");
        trapdoorOrientableBottom(RegisterBlock.SLIMY_PLANKS_TRAP_DOOR_BLOCK.getId().getPath(), modLoc("block/slimy_trapdoor"));

        withExistingTankModel(RegisterBlock.WOODEN_FLUID_TANK_BLOCK, "wooden_fluid_tank", false);
        withExistingTankModel(RegisterBlock.WOODEN_GLASSED_FLUID_TANK_BLOCK, "wooden_fluid_tank", true);

    }

    private void registerSimpleTexture(RegistryObject<?> registryObject, String path) {
        singleTexture(registryObject.getId().getPath(), mcLoc("item/generated"),
                "layer0", modLoc(path));
    }

    private void withExistingTankModel(RegistryObject<? extends Block> registryObject, String typeName, boolean glassed) {
        withExistingParent(registryObject.getId().getPath(),
                modLoc("block/" + typeName + (glassed ? "/fluid_tank_glassed" : "/fluid_tank")));
    }
}
