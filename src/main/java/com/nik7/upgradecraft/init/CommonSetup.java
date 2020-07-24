package com.nik7.upgradecraft.init;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

import static com.nik7.upgradecraft.init.RegisterBlocks.SLIMY_PLANKS_BLOCK;

public class CommonSetup {

    public static void init(final FMLCommonSetupEvent event) {
        setFireInfo(SLIMY_PLANKS_BLOCK, 10, 30);
    }

    private static <B extends Block> void setFireInfo(RegistryObject<B> registryObject, int encouragement, int flammability) {
        Block block = registryObject.get();
        ((FireBlock) Blocks.FIRE).setFireInfo(block, encouragement, flammability);
    }
}
