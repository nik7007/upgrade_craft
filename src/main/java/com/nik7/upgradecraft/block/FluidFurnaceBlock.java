package com.nik7.upgradecraft.block;

import com.nik7.upgradecraft.blockentity.FluidFurnaceEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class FluidFurnaceBlock extends AbstractMachineBlock {
    public FluidFurnaceBlock() {
        super(Properties.of(Material.STONE)
                .noOcclusion());
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new FluidFurnaceEntity(blockPos, blockState);
    }
}
