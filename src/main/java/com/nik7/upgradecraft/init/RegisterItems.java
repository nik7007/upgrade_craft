package com.nik7.upgradecraft.init;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;
import static com.nik7.upgradecraft.init.RegisterBlocks.SLIMY_PLANKS_BLOCK;

public class RegisterItems {
    private static final DeferredRegister<Item> ITEMS_REGISTER = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static void init() {
        ITEMS_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<Item> SLIMY_PLANKS_ITEM_BLOCK = ITEMS_REGISTER.register("slimy_planks_block",
            () -> new BlockItem(SLIMY_PLANKS_BLOCK.get(), new Item.Properties().group(ItemGroupUpgC.ITEM_GROUP)));

}
