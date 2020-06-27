package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.block.SlimyPlanksBlock;
import com.nik7.upgradecraft.block.WoodenFluidTankBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class RegisterBlocks {
    private static final DeferredRegister<Block> REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static void init() {
        REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SlimyPlanksBlock> SLIMY_PLANKS_BLOCK = REGISTER.register("slimy_planks_block", SlimyPlanksBlock::new);
    public static final RegistryObject<WoodenFluidTankBlock> WOODEN_FLUID_TANK_BLOCK = REGISTER.register("wooden_fluid_tank_block", WoodenFluidTankBlock::new);

}
