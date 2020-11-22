package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.blocks.FluidFurnaceBlock;
import com.nik7.upgradecraft.blocks.FunnelBlock;
import com.nik7.upgradecraft.init.RegisterBlocks;
import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder.PartialBlockstate;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;
import static com.nik7.upgradecraft.blocks.AbstractFluidTankBlock.MIXED;
import static com.nik7.upgradecraft.blocks.FunnelBlock.ENABLED;
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
                "double_wooden_fluid_tank_glassed_down",
                "double_wooden_fluid_tank_glassed_mixed"
        );

        createFunnel(RegisterBlocks.FUNNEL_BLOCK.get(), "funnel");
        createOrientableMachine(RegisterBlocks.FLUID_FURNACE_BLOCK.get(), "furnace_machine", "fluid_furnace");
        createOrientableMachine(RegisterBlocks.FLUID_INFUSER_BLOCK.get(), "fluid_infuser", "fluid_infuser");

        createTankModel(RegisterBlocks.CLAY_FLUID_TANK_BLOCK.get(),
                "clay_fluid_tank",
                "double_clay_fluid_tank");

        createTankModel(RegisterBlocks.CLAY_FLUID_TANK_GLASSED_BLOCK.get(),
                "clay_fluid_tank_glassed",
                "double_clay_fluid_tank_glassed_up",
                "double_clay_fluid_tank_glassed_down",
                "double_clay_fluid_tank_glassed_mixed"
        );

        createTankModel(RegisterBlocks.TERRACOTTA_FLUID_TANK_BLOCK.get(),
                "terracotta_fluid_tank",
                "double_terracotta_fluid_tank");

        createTankModel(RegisterBlocks.TERRACOTTA_FLUID_TANK_GLASSED_BLOCK.get(),
                "terracotta_fluid_tank_glassed",
                "double_terracotta_fluid_tank_glassed_up",
                "double_terracotta_fluid_tank_glassed_down",
                "double_terracotta_fluid_tank_glassed_mixed"
        );

    }

    private void createOrientableMachine(Block block, String modelName, String texture) {
        PartialBlockstate partialBlockstate = null;
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (partialBlockstate == null) {
                partialBlockstate = getVariantBuilder(block).partialState();
            } else {
                partialBlockstate = partialBlockstate.partialState();
            }
            partialBlockstate = partialBlockstate
                    .with(FluidFurnaceBlock.FACING, direction).with(FluidFurnaceBlock.LIT, false)
                    .addModels(createOrientableMachineModels(modelName, texture, direction, false));
            partialBlockstate = partialBlockstate.partialState()
                    .with(FluidFurnaceBlock.FACING, direction).with(FluidFurnaceBlock.LIT, true)
                    .addModels(createOrientableMachineModels(modelName, texture, direction, true));
        }

    }

    private ConfiguredModel[] createOrientableMachineModels(String modelName, String texture, Direction facing, boolean lit) {
        String active = lit ? "_on" : "_off";
        ModelFile model = models()
                .withExistingParent(texture + "_" + facing.name().toLowerCase() + active, modLoc("block/" + modelName))
                .texture("all", modLoc("block/" + texture))
                .texture("in", modLoc("block/" + texture + active));
        return ConfiguredModel.builder()
                .modelFile(model)
                .rotationY((int) (facing.getHorizontalAngle() + 180) % 360)
                .build();

    }

    private void createFunnel(Block block, String texture) {
        getVariantBuilder(block).forAllStatesExcept(state -> {
            Direction facing = state.get(FunnelBlock.FACING);
            String modelName;
            int rotationY;

            if (facing == Direction.DOWN) {
                modelName = "funnel";
                rotationY = 0;
            } else {
                modelName = "funnel_side";
                rotationY = (int) (facing.getHorizontalAngle() + 180) % 360;

            }

            ModelFile model = models()
                    .withExistingParent(texture + "_" + facing.name().toLowerCase(), modLoc("block/" + modelName))
                    .texture("all", modLoc("block/" + texture));
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(rotationY)
                    .build();
        }, ENABLED);
    }

    private void createTankModel(Block block,
                                 String singleTexture,
                                 String doubleTexture) {
        createTankModel(block, singleTexture, doubleTexture, doubleTexture, null);
    }

    private void createTankModel(Block block,
                                 String singleTexture,
                                 String doubleTopTexture,
                                 String doubleBottomTexture,
                                 @Nullable String doubleMixedTexture) {

        String topName;
        String bottomName;

        if (doubleTopTexture.equalsIgnoreCase(doubleBottomTexture)) {
            topName = doubleTopTexture + "_top";
            bottomName = doubleBottomTexture + "_bottom";
        } else {
            topName = doubleTopTexture;
            bottomName = doubleBottomTexture;
        }

        PartialBlockstate partialBlockstate = getVariantBuilder(block)
                .partialState().with(TYPE, TankType.SINGLE).addModels(ConfiguredModel.builder().modelFile(
                        models().withExistingParent(singleTexture, modLoc("block/fluid_tank"))
                                .texture("all", modLoc("block/" + singleTexture))
                ).build())
                .partialState().with(TYPE, TankType.BOTTOM);
        if (doubleMixedTexture != null) {
            partialBlockstate = partialBlockstate.with(MIXED, false);
        }
        partialBlockstate = partialBlockstate.addModels(
                ConfiguredModel.builder().modelFile(
                        models().withExistingParent(bottomName, modLoc("block/double_fluid_tank_down"))
                                .texture("all", modLoc("block/" + doubleBottomTexture))
                ).build())
                .partialState().with(TYPE, TankType.TOP);
        if (doubleMixedTexture != null) {
            partialBlockstate = partialBlockstate.with(MIXED, false);
        }
        partialBlockstate = partialBlockstate.addModels(
                ConfiguredModel.builder().modelFile(
                        models().withExistingParent(topName, modLoc("block/double_fluid_tank_up"))
                                .texture("all", modLoc("block/" + doubleTopTexture))
                ).build());

        if (doubleMixedTexture != null) {
            partialBlockstate
                    .partialState().with(TYPE, TankType.BOTTOM).with(MIXED, true).addModels(
                    ConfiguredModel.builder().modelFile(
                            models().withExistingParent(doubleMixedTexture + "_bottom", modLoc("block/double_fluid_tank_down"))
                                    .texture("all", modLoc("block/" + doubleMixedTexture))
                    ).build())
                    .partialState().with(TYPE, TankType.TOP).with(MIXED, true).addModels(
                    ConfiguredModel.builder().modelFile(
                            models().withExistingParent(doubleMixedTexture + "_top", modLoc("block/double_fluid_tank_up"))
                                    .texture("all", modLoc("block/" + doubleMixedTexture))
                    ).build());
        }

    }

}
