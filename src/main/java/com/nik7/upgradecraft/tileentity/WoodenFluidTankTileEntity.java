package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.fluids.tanks.FluidTankWrapper;
import com.nik7.upgradecraft.init.Config;
import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

import static com.nik7.upgradecraft.UpgradeCraft.LOGGER;
import static com.nik7.upgradecraft.blocks.WoodenFluidTankBlock.TYPE;
import static com.nik7.upgradecraft.init.RegisterTileEntity.WOODEN_FLUID_TANK_TILE_ENTITY_TYPE;

public class WoodenFluidTankTileEntity extends TileFluidHandler implements ITickableTileEntity {
    WoodenFluidTankTileEntity otherTank = null;
    private boolean firstTick = true;

    public WoodenFluidTankTileEntity() {
        super(WOODEN_FLUID_TANK_TILE_ENTITY_TYPE.get());
        this.tank = new FluidTankWrapper<>(new FluidTank(Config.TANK_CAPACITY.get() * FluidAttributes.BUCKET_VOLUME));
    }

    private static void otherSeparateTank(WoodenFluidTankTileEntity otherTank) {
        if (otherTank.world == null) {
            return;
        }
        TankType tankType = otherTank.getBlockState().get(TYPE);
        if (tankType != TankType.SINGLE) {
            boolean imBottom = tankType == TankType.BOTTOM;
            FluidTank singleTank = new FluidTank(Config.TANK_CAPACITY.get() * FluidAttributes.BUCKET_VOLUME);
            FluidStack fluidStack = otherTank.tank.getFluid().copy();

            if (fluidStack.getAmount() > 0) {
                int fluidAmount = fluidStack.getAmount();
                int tankCapacity = singleTank.getCapacity();

                if (imBottom) {
                    fluidStack.setAmount(Math.min(tankCapacity, fluidAmount));
                } else {
                    if (fluidAmount > tankCapacity) {
                        fluidStack.setAmount(fluidAmount - tankCapacity);
                    } else {
                        fluidStack = FluidStack.EMPTY;
                    }
                }
            } else {
                fluidStack = FluidStack.EMPTY;
            }

            singleTank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

            otherTank.setTank(singleTank);
            if (otherTank.getBlockState().func_235901_b_(TYPE)) {
                otherTank.world.setBlockState(otherTank.getPos(), otherTank.getBlockState().with(TYPE, TankType.SINGLE),
                        Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.RERENDER_MAIN_THREAD);
            }
            otherTank.markDirty();
            otherTank.otherTank = null;
        }
    }

    @Override
    public void tick() {
        if (this.firstTick) {
            this.firstTick = false;
            this.initTank();
        }
    }

    private void initTank() {
        if (this.getBlockState().func_235901_b_(TYPE) && this.otherTank == null) {
            TankType tankType = this.getBlockState().get(TYPE);
            if (tankType == TankType.SINGLE) {
                return;
            }
            boolean isBottom = tankType == TankType.BOTTOM;
            BlockPos pos = isBottom ? this.getPos().up() : this.getPos().down();
            TankType validType = isBottom ? TankType.TOP : TankType.BOTTOM;
            WoodenFluidTankTileEntity otherTank = getTank(pos, validType);
            if (otherTank != null) {
                FluidTank doubleTank = new FluidTank(2 * Config.TANK_CAPACITY.get() * FluidAttributes.BUCKET_VOLUME);
                doubleTank.fill(this.tank.getFluid().copy(), IFluidHandler.FluidAction.EXECUTE);
                this.setTank(doubleTank);
                otherTank.setTank(doubleTank);
                this.markDirty();
                otherTank.markDirty();
                this.otherTank = otherTank;
                otherTank.otherTank = this;
            } else {
                LOGGER.error("No tanks to merge!");
            }
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void setTank(FluidTank tank) {
        if (this.tank instanceof FluidTankWrapper) {
            ((FluidTankWrapper) this.tank).setInternalTank(tank);
        }
    }

    public void mergeTank() {
        if (this.world != null) {
            WoodenFluidTankTileEntity otherTank = getTank(this.getPos().up());
            boolean imBottom = false;
            if (otherTank != null) {
                imBottom = true;
            } else {
                otherTank = getTank(this.getPos().down());
            }
            if (otherTank != null) {

                FluidTank doubleTank = new FluidTank(2 * Config.TANK_CAPACITY.get() * FluidAttributes.BUCKET_VOLUME);
                int amount = doubleTank.fill(this.tank.getFluid().copy(), IFluidHandler.FluidAction.EXECUTE);
                FluidStack otherFluid = otherTank.tank.getFluid().copy();
                int newAmount = doubleTank.fill(otherFluid, IFluidHandler.FluidAction.EXECUTE);

                if (newAmount == amount + otherFluid.getAmount()) {
                    this.setTank(doubleTank);
                    otherTank.setTank(doubleTank);

                    TankType myNewType = imBottom ? TankType.BOTTOM : TankType.TOP;
                    TankType otherNewType = myNewType == TankType.BOTTOM ? TankType.TOP : TankType.BOTTOM;
                    BlockState otherBlockState = otherTank.getBlockState();

                    this.world.setBlockState(getPos(), this.getBlockState().with(TYPE, myNewType),
                            Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.RERENDER_MAIN_THREAD);
                    this.world.setBlockState(otherTank.getPos(), otherBlockState.with(TYPE, otherNewType),
                            Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.RERENDER_MAIN_THREAD);
                    this.otherTank = otherTank;
                    otherTank.otherTank = this;
                    this.markDirty();
                    otherTank.markDirty();
                }
            }
        }
    }

    @Nullable
    private WoodenFluidTankTileEntity getTank(BlockPos pos) {
        return this.getTank(pos, TankType.SINGLE);
    }

    @Nullable
    private WoodenFluidTankTileEntity getTank(BlockPos pos, TankType validType) {
        if (this.world != null) {
            TankType tankType = this.getBlockState().get(TYPE);
            if (tankType == validType.getOpposite()) {
                BlockState blockState = this.world.getBlockState(pos);
                if (blockState.func_235901_b_(TYPE) && blockState.get(TYPE) == validType) {
                    TileEntity tileEntity = this.world.getTileEntity(pos);
                    if (tileEntity instanceof WoodenFluidTankTileEntity) {
                        return (WoodenFluidTankTileEntity) tileEntity;
                    }
                }
            }
        }
        return null;
    }

    public void separateTank(TankType tankType) {
        if (tankType != TankType.SINGLE) {
            otherSeparateTank(this.otherTank);
            this.otherTank = null;
        }
    }
}
