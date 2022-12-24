package com.nik7.upgradecraft.block.tank.entity;

import com.nik7.upgradecraft.init.RegisterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WoodenGlassedFluidTankEntity extends WoodenFluidTankEntity {

    public WoodenGlassedFluidTankEntity(BlockPos pos, BlockState state) {
        super(RegisterBlockEntity.WOODEN_GLASSED_FLUID_TANK_ENTITY_TYPE.get(), pos, state, true);
    }

}
