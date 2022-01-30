package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.inventory.FluidFurnaceMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class RegisterContainerMenu {
    private static final DeferredRegister<MenuType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);

    public static void init() {
        REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<MenuType<FluidFurnaceMenu>> FLUID_FURNACE_CONTAINER_TYPE = REGISTER.register("fluid_furnace_menu",
            () -> IForgeMenuType.create(FluidFurnaceMenu::new));

}
