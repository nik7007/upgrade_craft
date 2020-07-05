package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.blocks.WoodenFluidTankBlock;
import com.nik7.upgradecraft.init.RegisterBlocks;
import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.data.DataGenerator;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.client.model.generators.ModelFile;

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

        getVariantBuilder(RegisterBlocks.WOODEN_FLUID_TANK_BLOCK.get()).forAllStatesExcept(state -> {
            TankType tankType = state.get(TYPE);
            ModelFile model = null;
            switch (tankType) {
                case SINGLE:
                    model = models().withExistingParent("wooden_fluid_tank", modLoc("block/fluid_tank"))
                            .texture("all", modLoc("block/wooden_fluid_tank"));
                    break;
                case TOP:
                    model = models().withExistingParent("wooden_double_top_fluid_tank", modLoc("block/double_fluid_tank_up"))
                            .texture("all", modLoc("block/double_wooden_fluid_tank"));
                    break;
                case BOTTOM:
                    model = models().withExistingParent("wooden_double_bottom_fluid_tank", modLoc("block/double_fluid_tank_down"))
                            .texture("all", modLoc("block/double_wooden_fluid_tank"));
                    break;
            }
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .build();
        }, WoodenFluidTankBlock.WATERLOGGED);
    }
}
