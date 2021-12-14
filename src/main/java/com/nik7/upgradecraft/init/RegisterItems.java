package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.item.UpgCBlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;
import static com.nik7.upgradecraft.init.RegisterBlocks.*;

public class RegisterItems {
    private static final DeferredRegister<Item> REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<Item> SLIMY_PLANKS_ITEM_BLOCK = REGISTER.register("slimy_planks_block",
            () -> new UpgCBlockItem(SLIMY_PLANKS_BLOCK.get()));
    public static final RegistryObject<Item> WOODEN_FLUID_TANK_ITEM_BLOCK = REGISTER.register("wooden_fluid_tank",
            () -> new UpgCBlockItem(WOODEN_FLUID_TANK_BLOCK.get()));
    public static final RegistryObject<Item> WOODEN_FLUID_GLASSED_TANK_ITEM_BLOCK = REGISTER.register("wooden_fluid_glassed_tank",
            () -> new UpgCBlockItem(WOODEN_FLUID_TANK_GLASSED_BLOCK.get()));

    public static final RegistryObject<Item> FUNNEL_ITEM_BLOCK = REGISTER.register("funnel_block",
            () -> new UpgCBlockItem(FUNNEL_BLOCK.get()));

    public static void init() {
        REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
