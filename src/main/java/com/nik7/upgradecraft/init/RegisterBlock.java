package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.block.slimywood.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.nik7.upgradecraft.UpgradeCraft.MODID;

public final class RegisterBlock {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);

    public static final RegistryObject<Block> SLIMY_PLANKS_BLOCK = BLOCKS.register("slimy_planks_block", SlimyPlanksBlock::new);
    public static final RegistryObject<StairBlock> SLIMY_PLANKS_STAIRS_BLOCK = BLOCKS.register("slimy_planks_stairs_block",
            () -> new SlimyPlanksStairsBlock(SLIMY_PLANKS_BLOCK.get())
    );
    public static final RegistryObject<SlabBlock> SLIMY_PLANKS_SLAB_BLOCK = BLOCKS.register("slimy_planks_slab_block",
            () -> new SlimyPlanksSlabBlock(SLIMY_PLANKS_BLOCK.get())
    );
    public static final RegistryObject<FenceBlock> SLIMY_PLANKS_FENCE_BLOCK = BLOCKS.register("slimy_planks_fence_block",
            () -> new SlimyPlanksFenceBlock(SLIMY_PLANKS_BLOCK.get())
    );
    public static final RegistryObject<FenceGateBlock> SLIMY_PLANKS_FENCE_GATE_BLOCK = BLOCKS.register("slimy_planks_fence_gate_block",
            () -> new SlimyPlanksFenceGateBlock(SLIMY_PLANKS_BLOCK.get())
    );
    public static final RegistryObject<DoorBlock> SLIMY_PLANKS_DOOR_BLOCK = BLOCKS.register("slimy_planks_door_block",
            () -> new SlimyPlanksDoorBlock(SLIMY_PLANKS_BLOCK.get()));

    public static final RegistryObject<TrapDoorBlock> SLIMY_PLANKS_TRAP_DOOR_BLOCK = BLOCKS.register("slimy_planks_trap_door_block",
            () -> new SlimyPlanksTrapDoorBlock(SLIMY_PLANKS_BLOCK.get()));


    private RegisterBlock() {
    }

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }

}
