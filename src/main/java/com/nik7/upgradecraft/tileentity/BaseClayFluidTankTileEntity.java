package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.blocks.ClayFluidTankBlock;
import com.nik7.upgradecraft.blocks.TerracottaFluidTankBlock;
import com.nik7.upgradecraft.init.Config;
import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import static com.nik7.upgradecraft.blocks.AbstractFluidTankBlock.TYPE;

public interface BaseClayFluidTankTileEntity {

    default void operate(AbstractFluidTankTileEntity tileEntity, short tickCount) {
        World world = tileEntity.getWorld();
        if (world == null || world.isRemote()) {
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
                    world.setBlockState(tileEntity.getPos(), blockState.with(BlockStateProperties.WATERLOGGED, Boolean.FALSE), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                }
            }
            waterloggedCheck();
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

    void waterloggedCheck();

    default boolean isHot(FluidStack tankFluid) {
        return tankFluid.getFluid().getAttributes().getTemperature(tankFluid) > (600 + 273);
    }

    default void cookingOperation(Boolean isCooking, short tickNumber, AbstractFluidTankTileEntity tileEntity) {
        World world = tileEntity.getWorld();
        BlockPos tilePos = tileEntity.getPos();
        if (world == null) {
            return;
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

    default void cookTank(AbstractFluidTankTileEntity tileEntity, TerracottaFluidTankBlock newBlock) {
        World world = tileEntity.getWorld();
        if (world != null) {

            TankType tankType = tileEntity.getBlockState().get(TYPE);
            FluidStack fluid = tileEntity.getFluid();
            tileEntity.separateTank(tankType);

            BlockPos pos = tileEntity.getPos();

            int amountToTransfer = 0;
            int amount = fluid.getAmount();
            switch (tankType) {
                case SINGLE:
                    amountToTransfer = amount;
                    break;
                case TOP:
                    amountToTransfer = Math.max(amount - Config.TANK_CAPACITY.get(), 0);
                    break;
                case BOTTOM:
                    amountToTransfer = Math.min(amount, Config.TANK_CAPACITY.get());
                    break;
            }
            FluidStack toTransfer = FluidStack.EMPTY;
            if (amountToTransfer > 0) {
                toTransfer = new FluidStack(fluid, amountToTransfer);
            }
            BlockState tankState = newBlock.getDefaultState();

            world.setBlockState(pos, tankState);
            TileEntity newTile = world.getTileEntity(pos);
            if (newTile instanceof AbstractFluidTankTileEntity) {
                AbstractFluidTankTileEntity newTank = (AbstractFluidTankTileEntity) newTile;
                newTank.getTank().setFluid(toTransfer);
                newTank.mergeTank();
            }
        }

    }
}
