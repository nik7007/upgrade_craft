package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.blocks.ClayFluidTankBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface BaseClayFluidTankTileEntity {

    default void operate(AbstractFluidTankTileEntity tileEntity, short tickCount) {
        if (tileEntity.getWorld() == null) {
            return;
        }
        BlockState blockState = tileEntity.getBlockState();
        Boolean waterlogged = blockState.get(ClayFluidTankBlock.WATERLOGGED);
        FluidStack fluid = tileEntity.getFluid();

        FluidTank tank = tileEntity.getTank();
        if (waterlogged && (fluid.isEmpty() || fluid.getFluid().equals(Fluids.WATER))) {
            if (tickCount % 40 == 0) {
                int fill = tank.fill(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
                if (fill > 0) {
                    tileEntity.getWorld().setBlockState(tileEntity.getPos(), blockState.with(BlockStateProperties.WATERLOGGED, Boolean.FALSE), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                }
            }
        } else {
            if (tickCount % 5 == 0 && !tank.isEmpty()) {
                tank.drain(25, IFluidHandler.FluidAction.EXECUTE);
            }
            boolean isCooking = false;
            if (!tank.isEmpty()) {
                FluidStack tankFluid = tank.getFluid();
                isCooking = isHot(tankFluid);
            }
            cookingOperation(isCooking, tickCount, tileEntity);

        }

    }

    default boolean isHot(FluidStack tankFluid) {
        return tankFluid.getFluid().getAttributes().getTemperature(tankFluid) > (600 + 273);
    }

    default void cookingOperation(Boolean isCooking, short tickNumber, AbstractFluidTankTileEntity tileEntity) {
        World world = tileEntity.getWorld();
        BlockState tileBlockState = tileEntity.getBlockState();
        BlockPos tilePos = tileEntity.getPos();
        if (world == null) {
            return;
        }
        Boolean blockCooking = tileBlockState.get(ClayFluidTankBlock.COOKING);

        if (blockCooking != isCooking) {
            world.setBlockState(tilePos, tileBlockState.with(ClayFluidTankBlock.COOKING, isCooking), Constants.BlockFlags.DEFAULT_AND_RERENDER);
        }
        if (isCooking) {
            increaseCooking();
        } else {
            BlockPos pos = tilePos.offset(Direction.values()[tickNumber % 6]);
            Block block = world.getBlockState(pos).getBlock();
            if (block instanceof FlowingFluidBlock) {
                Fluid fluid = ((FlowingFluidBlock) block).getFluid().getStillFluid();
                if (isHot(new FluidStack(fluid, 1000))) {
                    increaseCooking();
                }
            }
        }
        checkAndCook();
    }

    void increaseCooking();

    void checkAndCook();
}
