package com.nik7.upgradecraft.blockentity;

import com.nik7.upgradecraft.init.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static com.nik7.upgradecraft.init.RegisterBlockEntity.WOODEN_FLUID_TANK_ENTITY_TYPE;

public class WoodenFluidTankEntity extends AbstractFluidTankEntity implements BaseWoodenFluidTankEntity {
    public WoodenFluidTankEntity(BlockPos blockPos, BlockState blockState) {
        super(WOODEN_FLUID_TANK_ENTITY_TYPE.get(), blockPos, blockState, Config.TANK_CAPACITY.get());
    }

    @Override
    protected void tankOperation() {
        setBlockInFire(tickNumber);
    }

    @Override
    protected void onFluidChange(Void aVoid) {
        super.onFluidChange(aVoid);
        if (getLevel() == null || getLevel().isClientSide()) {
            return;
        }
        updateBurningState(this);
    }

    @Override
    protected void otherSeparateTank(AbstractFluidTankEntity otherTank) {
        super.otherSeparateTank(otherTank);
        if (otherTank.getLevel() != null || otherTank.getLevel().isClientSide()) {
            updateBurningState(otherTank);
        }
    }

    @Override
    protected boolean tileIsCorrectInstance(BlockEntity tileEntity) {
        return tileEntity instanceof WoodenFluidTankEntity || tileEntity instanceof WoodenFluidGlassTankEntity;
    }
}
