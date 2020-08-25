package com.nik7.upgradecraft.init;

import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class RegisterContainers {

    private static final DeferredRegister<ContainerType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);

    public static void init() {
        REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


}
