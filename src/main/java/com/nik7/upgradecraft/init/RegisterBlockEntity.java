package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.blockentity.FunnelEntity;
import com.nik7.upgradecraft.blockentity.WoodenFluidGlassTankEntity;
import com.nik7.upgradecraft.blockentity.WoodenFluidTankEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class RegisterBlockEntity {
    private static final DeferredRegister<BlockEntityType<?>> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MOD_ID);

    public static void init() {
        REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<BlockEntityType<WoodenFluidTankEntity>> WOODEN_FLUID_TANK_ENTITY_TYPE = REGISTER.register("wooden_fluid_tank_block",
            () -> BlockEntityType.Builder.of(WoodenFluidTankEntity::new, RegisterBlocks.WOODEN_FLUID_TANK_BLOCK.get()).build(null));

    public static final RegistryObject<BlockEntityType<WoodenFluidGlassTankEntity>> WOODEN_FLUID_TANK_GLASS_ENTITY_TYPE = REGISTER.register("wooden_fluid_glassed_tank_block",
            () -> BlockEntityType.Builder.of(WoodenFluidGlassTankEntity::new, RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get()).build(null));


    public static final RegistryObject<BlockEntityType<FunnelEntity>> FUNNEL_ENTITY_TYPE = REGISTER.register("funnel_block",
            () -> BlockEntityType.Builder.of(FunnelEntity::new, RegisterBlocks.FUNNEL_BLOCK.get()).build(null));

}
