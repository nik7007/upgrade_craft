package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.item.ModBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.nik7.upgradecraft.UpgradeCraft.MODID;
import static com.nik7.upgradecraft.init.RegisterBlock.*;

public final class RegisterItem {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final RegistryObject<Item> SLIMY_PLANKS_BLOCK_ITEM = registerSimpleItemBlock("slimy_planks_block", SLIMY_PLANKS_BLOCK);
    public static final RegistryObject<Item> SLIMY_PLANKS_STAIRS_BLOCK_ITEM = registerSimpleItemBlock("slimy_planks_stairs_block", SLIMY_PLANKS_STAIRS_BLOCK);
    public static final RegistryObject<Item> SLIMY_PLANKS_SLAB_BLOCK_ITEM = registerSimpleItemBlock("slimy_planks_slab_block", SLIMY_PLANKS_SLAB_BLOCK);

    public static final RegistryObject<Item> SLIMY_PLANKS_FENCE_BLOCK_ITEM = registerSimpleItemBlock("slimy_planks_fence_block", SLIMY_PLANKS_FENCE_BLOCK);
    public static final RegistryObject<Item> SLIMY_PLANKS_FENCE_GATE_BLOCK_ITEM = registerSimpleItemBlock("slimy_planks_fence_gate_block", SLIMY_PLANKS_FENCE_GATE_BLOCK);
    public static final RegistryObject<Item> SLIMY_PLANKS_DOOR_BLOCK_ITEM = registerSimpleItemBlock("slimy_planks_door_block", SLIMY_PLANKS_DOOR_BLOCK);
    public static final RegistryObject<Item> SLIMY_PLANKS_TRAP_DOOR_BLOCK_ITEM = registerSimpleItemBlock("slimy_planks_trap_door_block", SLIMY_PLANKS_TRAP_DOOR_BLOCK);

    private static RegistryObject<Item> registerSimpleItemBlock(String name, RegistryObject<? extends Block> rBlock) {
        return ITEMS.register(name, () -> new ModBlockItem(rBlock.get()));
    }

    private RegisterItem() {
    }

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
