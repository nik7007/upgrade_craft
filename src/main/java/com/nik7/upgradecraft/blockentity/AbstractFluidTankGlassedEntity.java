package com.nik7.upgradecraft.blockentity;

import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class AbstractFluidTankGlassedEntity extends AbstractFluidTankEntity {
    public AbstractFluidTankGlassedEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int initialCapacity) {
        super(blockEntityType, blockPos, blockState, initialCapacity);
    }

    @Override
    protected boolean canNotifyBlockUpdate() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public AABB getRenderBoundingBox() {
        TankType tankType = getTankType();
        if (tankType == TankType.BOTTOM) {
            return new AABB(getBlockPos(), getBlockPos().offset(1, 2, 1));
        }
        if (isTankMixed() && tankType == TankType.TOP) {
            return new AABB(getBlockPos().offset(0, -1, 0), getBlockPos().offset(1, 1, 1)
            );
        }
        return super.getRenderBoundingBox();
    }

    @Override
    protected boolean exchangeInfoCS() {
        return true;
    }

}
