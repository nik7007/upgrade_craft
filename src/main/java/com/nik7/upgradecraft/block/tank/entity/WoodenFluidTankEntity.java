package com.nik7.upgradecraft.block.tank.entity;

import com.nik7.upgradecraft.init.Config;
import com.nik7.upgradecraft.init.RegisterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class WoodenFluidTankEntity extends AbstractFluidTankEntityBlock {
    public WoodenFluidTankEntity(BlockPos pos, BlockState state) {
        this(RegisterBlockEntity.WOODEN_FLUID_TANK_ENTITY_TYPE.get(), pos, state, false);
    }

    public WoodenFluidTankEntity(@NotNull BlockEntityType<?> blockEntityType, BlockPos pos, BlockState state, boolean glassed) {
        super(blockEntityType, pos, state, glassed);
    }

    @Override
    protected int originalCapacityProvider() {
        return Config.TANK_CAPACITY.get();
    }

    @Override
    protected boolean tileIsMergeableInstance(BlockEntity tileEntity) {
        return tileEntity instanceof WoodenFluidTankEntity;
    }
}
