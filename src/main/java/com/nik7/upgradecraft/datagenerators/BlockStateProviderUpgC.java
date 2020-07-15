package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.blocks.WoodenFluidTankBlock;
import com.nik7.upgradecraft.init.RegisterBlocks;
import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.fml.RegistryObject;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;
import static com.nik7.upgradecraft.blocks.WoodenFluidTankBlock.TYPE;

public class BlockStateProviderUpgC extends BlockStateProvider {
    public BlockStateProviderUpgC(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(RegisterBlocks.SLIMY_PLANKS_BLOCK.get(),
                models().cubeAll("slimy_planks_block", new ResourceLocation(MOD_ID, "block/slimy_planks_block")));

        createTankModel(RegisterBlocks.WOODEN_FLUID_TANK_BLOCK.get(),
                "wooden_fluid_tank",
                "double_wooden_fluid_tank");

        createTankModel(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get(),
                "wooden_fluid_tank_glassed",
                "double_wooden_fluid_tank_glassed_up",
                "double_wooden_fluid_tank_glassed_down"
        );

    }

    private void createTankModel(Block block,
                                 String singleTexture,
                                 String doubleTexture) {
        createTankModel(block, singleTexture, doubleTexture, doubleTexture);
    }

    private void createTankModel(Block block,
                                 String singleTexture,
                                 String doubleTopTexture,
                                 String doubleBottomTexture) {

        String topName;
        String bottomName;

        if (doubleTopTexture.equalsIgnoreCase(doubleBottomTexture)) {
            topName = doubleTopTexture + "_top";
            bottomName = doubleBottomTexture + "_bottom";
        } else {
            topName = doubleTopTexture;
            bottomName = doubleBottomTexture;
        }

        getVariantBuilder(block).forAllStatesExcept(state -> {
            TankType tankType = state.get(TYPE);
            ModelFile model = null;
            switch (tankType) {
                case SINGLE:
                    model = models().withExistingParent(singleTexture, modLoc("block/fluid_tank"))
                            .texture("all", modLoc("block/" + singleTexture));
                    break;
                case TOP:
                    model = models().withExistingParent(topName, modLoc("block/double_fluid_tank_up"))
                            .texture("all", modLoc("block/" + doubleTopTexture));
                    break;
                case BOTTOM:
                    model = models().withExistingParent(bottomName, modLoc("block/double_fluid_tank_down"))
                            .texture("all", modLoc("block/" + doubleBottomTexture));
                    break;
            }
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .build();
        }, WoodenFluidTankBlock.WATERLOGGED);
    }

}
