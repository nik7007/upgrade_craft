package com.nik7.upgradecraft.init;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.nik7.upgradecraft.init.RegisterBlocks.*;

public class CommonSetup {

    public static void init(final FMLCommonSetupEvent event) {
        setFireInfo(SLIMY_PLANKS_BLOCK, 10, 30);
        setFireInfo(WOODEN_FLUID_TANK_BLOCK, 10, 25);
        setFireInfo(WOODEN_FLUID_TANK_GLASSED_BLOCK, 10, 25);
    }

    private static <B extends Block> void setFireInfo(RegistryObject<B> registryObject, int encouragement, int flammability) {
        Block block = registryObject.get();
        ((FireBlock) Blocks.FIRE).setFireInfo(block, encouragement, flammability);
    }
}
