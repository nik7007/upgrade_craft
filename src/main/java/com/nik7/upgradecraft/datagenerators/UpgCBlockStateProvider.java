package com.nik7.upgradecraft.datagenerators;


import com.nik7.upgradecraft.block.tank.AbstractFluidTankBlock;
import com.nik7.upgradecraft.init.RegisterBlock;
import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.data.DataGenerator;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.VariantBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.nik7.upgradecraft.UpgradeCraft.MODID;

public class UpgCBlockStateProvider extends BlockStateProvider {

    private final static String BASE_TANK_MODEL_FOLDER = "block/fluid_tank";
    public UpgCBlockStateProvider(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(RegisterBlock.SLIMY_PLANKS_BLOCK.get());
        stairsBlock(RegisterBlock.SLIMY_PLANKS_STAIRS_BLOCK.get(), modLoc("block/slimy_planks_block"));
        slabBlock(RegisterBlock.SLIMY_PLANKS_SLAB_BLOCK.get(), modLoc("block/slimy_planks_block"), modLoc("block/slimy_planks_block"));
        fenceBlock(RegisterBlock.SLIMY_PLANKS_FENCE_BLOCK.get(), modLoc("block/slimy_planks_block"));
        fenceGateBlock(RegisterBlock.SLIMY_PLANKS_FENCE_GATE_BLOCK.get(), modLoc("block/slimy_planks_block"));
        doorBlockWithRenderType(RegisterBlock.SLIMY_PLANKS_DOOR_BLOCK.get(),  modLoc("block/slimy_door_bottom"),  modLoc("block/slimy_door_top"), "cutout");
        trapdoorBlockWithRenderType(RegisterBlock.SLIMY_PLANKS_TRAP_DOOR_BLOCK.get(), modLoc("block/slimy_trapdoor"), true, "cutout");

        tankBlock(RegisterBlock.WOODEN_FLUID_TANK_BLOCK.get(), "wooden_fluid_tank", false);
        tankBlock(RegisterBlock.WOODEN_GLASSED_FLUID_TANK_BLOCK.get(), "wooden_fluid_tank", true);
    }

    private void tankBlock(Block block, String typeName, boolean glassed) {
        typeName = "block/" + typeName;
        final String SINGLE_TEXTURE = typeName + "/" + (glassed ? "fluid_tank_glassed" : "fluid_tank");
        VariantBlockStateBuilder.PartialBlockstate partialBlockstate = getVariantBuilder(block)
                .partialState().with(AbstractFluidTankBlock.TYPE, TankType.SINGLE)
                .addModels(ConfiguredModel.builder()
                        .modelFile(
                                models().withExistingParent(SINGLE_TEXTURE, modLoc(BASE_TANK_MODEL_FOLDER + "/fluid_tank"))
                                        .texture("all", modLoc(SINGLE_TEXTURE))
                        ).build());
        if (!glassed) {
            final String DOUBLE_TEXTURE = typeName + "/double_fluid_tank";
            doubleTankBlock(partialBlockstate.partialState(), TankType.BOTTOM, DOUBLE_TEXTURE, true);
            doubleTankBlock(partialBlockstate.partialState(), TankType.TOP, DOUBLE_TEXTURE, true);
        } else {
            final String MIXED_DOUBLE_TEXTURE = typeName + "/double_fluid_tank_glassed_mixed";
            partialBlockstate = partialBlockstate.partialState().with(AbstractFluidTankBlock.MIXED, true);
            doubleTankBlock(partialBlockstate, TankType.BOTTOM, MIXED_DOUBLE_TEXTURE, true);

            partialBlockstate = partialBlockstate.partialState().with(AbstractFluidTankBlock.MIXED, true);
            doubleTankBlock(partialBlockstate, TankType.TOP, MIXED_DOUBLE_TEXTURE, true);

            partialBlockstate = partialBlockstate.partialState().with(AbstractFluidTankBlock.MIXED, false);
            doubleTankBlock(partialBlockstate, typeName, TankType.BOTTOM);

            partialBlockstate = partialBlockstate.partialState().with(AbstractFluidTankBlock.MIXED, false);
            doubleTankBlock(partialBlockstate, typeName, TankType.TOP);
        }

    }

    public void doubleTankBlock(VariantBlockStateBuilder.PartialBlockstate partialBlockstate,
                                String typeName,
                                TankType tankType) {
        final String DOUBLE_TEXTURE = typeName + "/double_fluid_tank_glassed_" + tankType.getSerializedName();
        doubleTankBlock(partialBlockstate, tankType, DOUBLE_TEXTURE, false);
    }

    public void doubleTankBlock(VariantBlockStateBuilder.PartialBlockstate partialBlockstate,
                                TankType tankType,
                                String doubleTexture,
                                boolean createUniqueModel) {
        final String MODEL_PATH = BASE_TANK_MODEL_FOLDER + "/double_fluid_tank_" + tankType.getSerializedName();
        final String MODEL_NAME = createUniqueModel ? doubleTexture + "_" + tankType.getSerializedName() : doubleTexture;
        partialBlockstate.with(AbstractFluidTankBlock.TYPE, tankType)
                .addModels(ConfiguredModel.builder()
                        .modelFile(
                                models().withExistingParent(MODEL_NAME, modLoc(MODEL_PATH))
                                        .texture("all", modLoc(doubleTexture))
                        ).build()
                );
    }
}
