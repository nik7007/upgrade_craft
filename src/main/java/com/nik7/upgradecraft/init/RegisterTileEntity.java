package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.tileentity.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class RegisterTileEntity {
    private static final DeferredRegister<TileEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);

    public static void init() {
        REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<TileEntityType<WoodenFluidTankTileEntity>> WOODEN_FLUID_TANK_TILE_ENTITY_TYPE = REGISTER.register("wooden_fluid_tank_block",
            () -> TileEntityType.Builder.create(WoodenFluidTankTileEntity::new, RegisterBlocks.WOODEN_FLUID_TANK_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<WoodenFluidTankGlassedTileEntity>> WOODEN_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE = REGISTER.register("wooden_fluid_tank_glassed_block",
            () -> TileEntityType.Builder.create(WoodenFluidTankGlassedTileEntity::new, RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<FunnelTileEntity>> FUNNEL_TILE_ENTITY_TYPE = REGISTER.register("funnel_block",
            () -> TileEntityType.Builder.create(FunnelTileEntity::new, RegisterBlocks.FUNNEL_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<FluidFurnaceTileEntity>> FLUID_FURNACE_TILE_ENTITY_TYPE = REGISTER.register("fluid_furnace_block",
            () -> TileEntityType.Builder.create(FluidFurnaceTileEntity::new, RegisterBlocks.FLUID_FURNACE_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<FluidInfuserTileEntity>> FLUID_INFUSER_TILE_ENTITY_TYPE = REGISTER.register("fluid_infuser_block",
            () -> TileEntityType.Builder.create(FluidInfuserTileEntity::new, RegisterBlocks.FLUID_INFUSER_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<ClayFluidTankTileEntity>> CLAY_FLUID_TANK_TILE_ENTITY_TYPE = REGISTER.register("clay_fluid_tank_block",
            () -> TileEntityType.Builder.create(ClayFluidTankTileEntity::new, RegisterBlocks.CLAY_FLUID_TANK_BLOCK.get()).build(null));
    public static final RegistryObject<TileEntityType<ClayFluidTankGlassedTileEntity>> CLAY_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE = REGISTER.register("clay_fluid_tank_glassed_block",
            () -> TileEntityType.Builder.create(ClayFluidTankGlassedTileEntity::new, RegisterBlocks.CLAY_FLUID_TANK_GLASSED_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<TerracottaFluidTankTileEntity>> TERRACOTTA_FLUID_TANK_TILE_ENTITY_TYPE = REGISTER.register("terracotta_fluid_tank_block",
            () -> TileEntityType.Builder.create(TerracottaFluidTankTileEntity::new, RegisterBlocks.TERRACOTTA_FLUID_TANK_BLOCK.get()).build(null));

    public static final RegistryObject<TileEntityType<TerracottaFluidTankGlassedTileEntity>> TERRACOTTA_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE = REGISTER.register("terracotta_fluid_tank_glassed_block",
            () -> TileEntityType.Builder.create(TerracottaFluidTankGlassedTileEntity::new, RegisterBlocks.TERRACOTTA_FLUID_TANK_GLASSED_BLOCK.get()).build(null));

}
