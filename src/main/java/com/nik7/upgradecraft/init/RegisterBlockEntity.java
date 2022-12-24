package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.block.tank.entity.WoodenFluidTankEntity;
import com.nik7.upgradecraft.block.tank.entity.WoodenGlassedFluidTankEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.nik7.upgradecraft.UpgradeCraft.MODID;

public final class RegisterBlockEntity {
    private static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);

    public static final RegistryObject<BlockEntityType<WoodenFluidTankEntity>> WOODEN_FLUID_TANK_ENTITY_TYPE = REGISTER.register("wooden_fluid_tank_block",
            () -> BlockEntityType.Builder.of(WoodenFluidTankEntity::new, RegisterBlock.WOODEN_FLUID_TANK_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<WoodenGlassedFluidTankEntity>> WOODEN_GLASSED_FLUID_TANK_ENTITY_TYPE = REGISTER.register("wooden_glassed_fluid_tank_block",
            () -> BlockEntityType.Builder.of(WoodenGlassedFluidTankEntity::new, RegisterBlock.WOODEN_GLASSED_FLUID_TANK_BLOCK.get()).build(null));

    public static void register(IEventBus modEventBus) {
        REGISTER.register(modEventBus);
    }
    private RegisterBlockEntity() {
    }
}
