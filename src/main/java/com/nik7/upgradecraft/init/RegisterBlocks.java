package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.block.SlimyPlanksBlock;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class RegisterBlocks {
    private static final DeferredRegister<Block> BLOCKS_REGISTER = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);

    public static void init() {
        BLOCKS_REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static final RegistryObject<SlimyPlanksBlock> SLIMY_PLANKS_BLOCK = BLOCKS_REGISTER.register("slimy_planks_block", SlimyPlanksBlock::new);

}
