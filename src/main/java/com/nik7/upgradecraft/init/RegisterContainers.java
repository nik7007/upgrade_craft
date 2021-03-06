package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.container.FluidFurnaceContainer;
import com.nik7.upgradecraft.container.FluidInfuserContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class RegisterContainers {

    private static final DeferredRegister<ContainerType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);

    public static void init() {
        REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<ContainerType<FluidFurnaceContainer>> FLUID_FURNACE_CONTAINER_TYPE = REGISTER.register("fluid_furnace_container", () -> IForgeContainerType.create(FluidFurnaceContainer::new));

    public static final RegistryObject<ContainerType<FluidInfuserContainer>> FLUID_INFUSER_CONTAINER_TYPE = REGISTER.register("fluid_infuser_container", () -> IForgeContainerType.create(FluidInfuserContainer::new));

}
