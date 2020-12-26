package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.client.renderer.item.FluidTankItemStackRender;
import com.nik7.upgradecraft.items.ClayIngotItem;
import com.nik7.upgradecraft.items.TerracottaFluidTankBlockItem;
import com.nik7.upgradecraft.items.UpgCBlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;
import static com.nik7.upgradecraft.init.RegisterBlocks.*;

public class RegisterItems {
    private static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static void init() {
        REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Item> SLIMY_PLANKS_ITEM_BLOCK = REGISTER.register("slimy_planks_block",
            () -> new UpgCBlockItem(SLIMY_PLANKS_BLOCK.get()));

    public static final RegistryObject<Item> WOODEN_FLUID_TANK_ITEM_BLOCK = REGISTER.register("wooden_fluid_tank_block",
            () -> new UpgCBlockItem(WOODEN_FLUID_TANK_BLOCK.get()));

    public static final RegistryObject<Item> WOODEN_FLUID_TANK_GLASSED_ITEM_BLOCK = REGISTER.register("wooden_fluid_tank_glassed_block",
            () -> new UpgCBlockItem(WOODEN_FLUID_TANK_GLASSED_BLOCK.get()));

    public static final RegistryObject<Item> FUNNEL_ITEM_BLOCK = REGISTER.register("funnel_block",
            () -> new UpgCBlockItem(FUNNEL_BLOCK.get()));

    public static final RegistryObject<Item> FLUID_FURNACE_ITEM_BLOCK = REGISTER.register("fluid_furnace_block",
            () -> new UpgCBlockItem(FLUID_FURNACE_BLOCK.get()));

    public static final RegistryObject<Item> FLUID_INFUSER_ITEM_BLOCK = REGISTER.register("fluid_infuser_block",
            () -> new UpgCBlockItem(FLUID_INFUSER_BLOCK.get()));

    public static final RegistryObject<Item> CLAY_INGOT_ITEM = REGISTER.register("clay_ingot_item", ClayIngotItem::new);

    public static final RegistryObject<Item> CLAY_FLUID_TANK_ITEM_BLOCK = REGISTER.register("clay_fluid_tank_block",
            () -> new UpgCBlockItem(CLAY_FLUID_TANK_BLOCK.get()));

    public static final RegistryObject<Item> CLAY_FLUID_TANK_GLASSED_ITEM_BLOCK = REGISTER.register("clay_fluid_tank_glassed_block",
            () -> new UpgCBlockItem(CLAY_FLUID_TANK_GLASSED_BLOCK.get()));


    public static final RegistryObject<Item> TERRACOTTA_FLUID_TANK_ITEM_BLOCK = REGISTER.register("terracotta_fluid_tank_block",
            () -> new TerracottaFluidTankBlockItem(TERRACOTTA_FLUID_TANK_BLOCK.get()));

    public static final RegistryObject<Item> TERRACOTTA_FLUID_TANK_GLASSED_ITEM_BLOCK = REGISTER.register("terracotta_fluid_tank_glassed_block",
            () -> new TerracottaFluidTankBlockItem(TERRACOTTA_FLUID_TANK_GLASSED_BLOCK.get(), () -> FluidTankItemStackRender::new));

}
