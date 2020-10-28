package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.items.ClayIngotItem;
import net.minecraft.item.BlockItem;
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
            () -> new BlockItem(SLIMY_PLANKS_BLOCK.get(), new Item.Properties().group(ItemGroupUpgC.ITEM_GROUP)));

    public static final RegistryObject<Item> WOODEN_FLUID_TANK_ITEM_BLOCK = REGISTER.register("wooden_fluid_tank_block",
            () -> new BlockItem(WOODEN_FLUID_TANK_BLOCK.get(), new Item.Properties().group(ItemGroupUpgC.ITEM_GROUP)));

    public static final RegistryObject<Item> WOODEN_FLUID_TANK_GLASSED_ITEM_BLOCK = REGISTER.register("wooden_fluid_tank_glassed_block",
            () -> new BlockItem(WOODEN_FLUID_TANK_GLASSED_BLOCK.get(), new Item.Properties().group(ItemGroupUpgC.ITEM_GROUP)));

    public static final RegistryObject<Item> FUNNEL_ITEM_BLOCK = REGISTER.register("funnel_block",
            () -> new BlockItem(FUNNEL_BLOCK.get(), new Item.Properties().group(ItemGroupUpgC.ITEM_GROUP)));

    public static final RegistryObject<Item> FLUID_FURNACE_ITEM_BLOCK = REGISTER.register("fluid_furnace_block",
            () -> new BlockItem(FLUID_FURNACE_BLOCK.get(), new Item.Properties().group(ItemGroupUpgC.ITEM_GROUP)));

    public static final RegistryObject<Item> FLUID_INFUSER_ITEM_BLOCK = REGISTER.register("fluid_infuser_block",
            () -> new BlockItem(FLUID_INFUSER_BLOCK.get(), new Item.Properties().group(ItemGroupUpgC.ITEM_GROUP)));

    public static final RegistryObject<Item> CLAY_INGOT_ITEM = REGISTER.register("clay_ingot_item", ClayIngotItem::new);

}
