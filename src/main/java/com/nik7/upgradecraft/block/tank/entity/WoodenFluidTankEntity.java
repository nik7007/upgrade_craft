package com.nik7.upgradecraft.block.tank.entity;

import com.nik7.upgradecraft.init.Config;
import com.nik7.upgradecraft.init.RegisterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import static com.nik7.upgradecraft.block.tank.WoodenFluidTankBlock.BURNING;
import static com.nik7.upgradecraft.util.FluidHelper.isFluidHot;

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

    protected boolean isBurning() {
        BlockState blockState = getBlockState();
        if (blockState.hasProperty(BURNING)) {
            return blockState.getValue(BURNING);
        }
        return false;
    }

    @Override
    protected void onChangeFluid(FluidStack fluidStack) {
        super.onChangeFluid(fluidStack);
        calculateBurningStatus(this);
    }

    private void calculateBurningStatus(WoodenFluidTankEntity fluidTankEntity) {
        Level level = fluidTankEntity.getLevel();
        if (level == null || level.isClientSide()) {
            return;
        }

        boolean fluidHot = isFluidHot(fluidTankEntity.getFluid());
        boolean burning = fluidTankEntity.isBurning();
        if (fluidHot == burning) {
            return;
        }
        level.setBlock(fluidTankEntity.getBlockPos(), fluidTankEntity.getBlockState().setValue(BURNING, fluidHot), Block.UPDATE_ALL_IMMEDIATE);
        AbstractFluidTankEntityBlock otherTank = fluidTankEntity.otherTank;
        if (otherTank == null || otherTank.getLevel() == null) {
            return;
        }
        otherTank.getLevel().setBlock(otherTank.getBlockPos(), otherTank.getBlockState().setValue(BURNING, fluidHot), Block.UPDATE_ALL_IMMEDIATE);
    }

    @Override
    protected void madeTankSingle(AbstractFluidTankEntityBlock tankEntityBlock) {
        super.madeTankSingle(tankEntityBlock);
        if (tankEntityBlock instanceof WoodenFluidTankEntity woodenFluidTankEntity) {
            calculateBurningStatus(woodenFluidTankEntity);
        }
    }

    @Override
    protected boolean tileIsMergeableInstance(BlockEntity tileEntity) {
        return tileEntity instanceof WoodenFluidTankEntity;
    }
}
