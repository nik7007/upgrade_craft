package com.nik7.upgradecraft.init;

import net.minecraft.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static mcjty.theoneprobe.TheOneProbe.MODID;

public class RegisterBlocks {
    private static final DeferredRegister<Block> BLOCKS_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static void init() {
        BLOCKS_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
