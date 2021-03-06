package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.blocks.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class RegisterBlocks {
    private static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static void init() {
        REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SlimyPlanksBlock> SLIMY_PLANKS_BLOCK = REGISTER.register("slimy_planks_block", SlimyPlanksBlock::new);

    public static final RegistryObject<WoodenFluidTankBlock> WOODEN_FLUID_TANK_BLOCK = REGISTER.register("wooden_fluid_tank_block", WoodenFluidTankBlock::new);
    public static final RegistryObject<WoodenFluidTankGlassedBlock> WOODEN_FLUID_TANK_GLASSED_BLOCK = REGISTER.register("wooden_fluid_tank_glassed_block", WoodenFluidTankGlassedBlock::new);

    public static final RegistryObject<FunnelBlock> FUNNEL_BLOCK = REGISTER.register("funnel_block", FunnelBlock::new);

    public static final RegistryObject<FluidFurnaceBlock> FLUID_FURNACE_BLOCK = REGISTER.register("fluid_furnace_block", FluidFurnaceBlock::new);

    public static final RegistryObject<FluidInfuserBlock> FLUID_INFUSER_BLOCK = REGISTER.register("fluid_infuser_block", FluidInfuserBlock::new);

    public static final RegistryObject<ClayFluidTankBlock> CLAY_FLUID_TANK_BLOCK = REGISTER.register("clay_fluid_tank_block", ClayFluidTankBlock::new);
    public static final RegistryObject<ClayFluidTankGlassedBlock> CLAY_FLUID_TANK_GLASSED_BLOCK = REGISTER.register("clay_fluid_tank_glassed_block", ClayFluidTankGlassedBlock::new);

    public static final RegistryObject<TerracottaFluidTankBlock> TERRACOTTA_FLUID_TANK_BLOCK = REGISTER.register("terracotta_fluid_tank_block", TerracottaFluidTankBlock::new);
    public static final RegistryObject<TerracottaFluidTankGlassedBlock> TERRACOTTA_FLUID_TANK_GLASSED_BLOCK = REGISTER.register("terracotta_fluid_tank_glassed_block", TerracottaFluidTankGlassedBlock::new);

    public static final RegistryObject<Block> CLAY_BRICKS_BLOCK = REGISTER.register("clay_bricks_block", () -> new Block(AbstractBlock.Properties.create(Material.CLAY).hardnessAndResistance(0.8F).sound(SoundType.GROUND)));
    public static final RegistryObject<SlabBlock> CLAY_BRICKS_SLAB_BLOCK = REGISTER.register("clay_bricks_slab_block", () -> new SlabBlock(AbstractBlock.Properties.from(CLAY_BRICKS_BLOCK.get())));
    public static final RegistryObject<WallBlock> CLAY_BRICKS_WALL_BLOCK = REGISTER.register("clay_bricks_wall_block", () -> new WallBlock(AbstractBlock.Properties.from(CLAY_BRICKS_BLOCK.get())));
    public static final RegistryObject<StairsBlock> CLAY_BRICKS_STAIRS_BLOCK = REGISTER.register("clay_bricks_stairs_block", () -> new StairsBlock(() -> CLAY_BRICKS_BLOCK.get().getDefaultState(), AbstractBlock.Properties.from(CLAY_BRICKS_BLOCK.get())));

}
