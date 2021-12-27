package com.nik7.upgradecraft.blockentity;

import com.nik7.upgradecraft.state.properties.TankType;
import com.nik7.upgradecraft.tanks.EventFluidTank;
import com.nik7.upgradecraft.tanks.FluidTankWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

import static com.nik7.upgradecraft.UpgradeCraft.LOGGER;
import static com.nik7.upgradecraft.block.AbstractFluidTankBlock.MIXED;
import static com.nik7.upgradecraft.block.AbstractFluidTankBlock.TYPE;

public abstract class AbstractFluidTankEntity extends BaseFluidHandlerEntity {
    protected final int initialCapacity;
    protected final int rawInitialCapacity;
    protected AbstractFluidTankEntity otherTank = null;
    protected short tickNumber;
    private boolean firstTick = true;

    public AbstractFluidTankEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState, int initialCapacity) {
        super(blockEntityType, blockPos, blockState);
        this.rawInitialCapacity = initialCapacity;
        this.initialCapacity = initialCapacity * FluidAttributes.BUCKET_VOLUME;
        this.tank = new FluidTankWrapper<>(new EventFluidTank(this.initialCapacity, this::onFluidChange));
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        tickNumber = tag.getShort("tickNumber");
    }

    @Override
    protected boolean exchangeInfoCS() {
        return isTankMixed();
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag compoundTag) {
        super.saveAdditional(compoundTag);
        compoundTag.putShort("tickNumber", tickNumber);
    }

    protected void onFluidChange(Void aVoid) {
        Level level = getLevel();
        if (level != null && !level.isClientSide()) {
            BlockPos pos = getBlockPos();
            updateComparator();
            TankType tankType = getTankType();
            BlockPos blockPos = null;
            if (tankType == TankType.BOTTOM) {
                blockPos = pos.above();
            } else if (tankType == TankType.TOP) {
                blockPos = pos.below();
            }
            if (blockPos != null) {
                level.updateNeighbourForOutputSignal(blockPos, level.getBlockState(blockPos).getBlock());
            }
            if (canNotifyBlockUpdate()) {
                notifyBlockUpdate();
            }
            setChanged();
        }
    }

    protected boolean canNotifyBlockUpdate() {
        return isTankMixed();
    }

    public boolean isTankMixed() {
        BlockState blockState = getBlockState();
        if (blockState.hasProperty(MIXED)) {
            return blockState.getValue(MIXED);
        }
        return false;
    }

    @Nullable
    public TankType getTankType() {
        BlockState blockState = getBlockState();
        if (blockState.hasProperty(TYPE)) {
            return blockState.getValue(TYPE);
        }

        return null;
    }

    @Override
    protected void notifyBlockUpdate() {
        if (getLevel() != null) {
            notifyBlockUpdate(this);
            if (otherTank != null) {
                notifyBlockUpdate(otherTank);
            }
        }
    }

    protected FluidStack calculateFluidFormTank(FluidTank tank, TankType tankType) {
        FluidStack fluidStack = tank.getFluid().copy();
        int fluidAmount = fluidStack.getAmount();
        if (tankType != TankType.SINGLE && fluidAmount > 0) {
            if (tankType == TankType.BOTTOM) {
                fluidStack.setAmount(Math.min(initialCapacity, fluidAmount));
            } else {
                if (fluidAmount > initialCapacity) {
                    fluidStack.setAmount(fluidAmount - initialCapacity);
                } else {
                    fluidStack = FluidStack.EMPTY;
                }
            }
        }
        return fluidStack;
    }

    protected void otherSeparateTank(AbstractFluidTankEntity otherTank) {
        if (otherTank == null || otherTank.getLevel() == null || otherTank.isRemoved()) {
            return;
        }
        TankType tankType = otherTank.getBlockState().getValue(TYPE);
        if (tankType != TankType.SINGLE) {
            FluidTank singleTank = new EventFluidTank(initialCapacity, otherTank::onFluidChange);
            FluidStack fluidStack = calculateFluidFormTank(otherTank.tank, tankType);

            singleTank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            otherTank.setTank(singleTank);

            if (otherTank.getBlockState().hasProperty(TYPE)) {
                otherTank.getLevel().setBlock(otherTank.getBlockPos(), otherTank.getBlockState().setValue(TYPE, TankType.SINGLE).setValue(MIXED, false), Block.UPDATE_ALL_IMMEDIATE);
            }
            otherTank.setChanged();
            otherTank.otherTank = null;
        }
    }

    public void tick() {
        initTitleEntity();

        if (getLevel() != null && !getLevel().isClientSide()) {
            tickNumber %= 400;
            tankOperation();
            tickNumber++;
        }
    }

    protected void initTitleEntity() {
        if (this.firstTick) {
            this.firstTick = false;
            this.initTank();
            onFluidChange(null);
        }
    }

    protected abstract void tankOperation();

    private void initTank() {
        if (this.getBlockState().hasProperty(TYPE) && this.otherTank == null) {
            TankType tankType = this.getBlockState().getValue(TYPE);
            if (tankType == TankType.SINGLE) {
                return;
            }
            boolean isBottom = tankType == TankType.BOTTOM;
            BlockPos pos = isBottom ? this.getBlockPos().above() : this.getBlockPos().below();
            TankType validType = isBottom ? TankType.TOP : TankType.BOTTOM;
            AbstractFluidTankEntity otherTank = getTank(pos, validType);
            if (otherTank != null) {
                FluidTank doubleTank = new EventFluidTank(2 * initialCapacity, this::onFluidChange);
                doubleTank.fill(this.tank.getFluid().copy(), IFluidHandler.FluidAction.EXECUTE);
                this.setTank(doubleTank);
                otherTank.setTank(doubleTank);
                this.setChanged();
                otherTank.setChanged();
                this.otherTank = otherTank;
                otherTank.otherTank = this;
            } else {
                LOGGER.error("No tanks to merge!");
            }
        }
    }

    protected boolean fluidsAreCompatible(FluidStack fluidStack1, FluidStack fluidStack2) {
        if (fluidStack1.isEmpty() || fluidStack2.isEmpty()) {
            return true;
        } else {
            return fluidStack1.getFluid().isSame(fluidStack2.getFluid());
        }
    }

    public void mergeTank() {
        if (this.getLevel() != null) {
            AbstractFluidTankEntity otherTank = getTank(this.getBlockPos().above());
            boolean imBottom = false;
            if (otherTank != null) {
                imBottom = true;
            } else {
                otherTank = getTank(this.getBlockPos().below());
            }
            if (otherTank != null) {

                FluidTank doubleTank = new EventFluidTank(2 * initialCapacity, this::onFluidChange);
                FluidStack fluidStack = this.tank.getFluid().copy();
                FluidStack otherFluid = otherTank.tank.getFluid().copy();

                if (!fluidsAreCompatible(fluidStack, otherFluid)) {
                    return;
                }

                int amount = doubleTank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
                doubleTank.fill(otherFluid, IFluidHandler.FluidAction.EXECUTE);

                if (doubleTank.getFluid().getAmount() == amount + otherFluid.getAmount()) {
                    this.setTank(doubleTank);
                    otherTank.setTank(doubleTank);

                    TankType myNewType = imBottom ? TankType.BOTTOM : TankType.TOP;
                    TankType otherNewType = myNewType == TankType.BOTTOM ? TankType.TOP : TankType.BOTTOM;
                    BlockState otherBlockState = otherTank.getBlockState();


                    boolean mixed = !(otherBlockState.getBlock().getClass().equals(this.getBlockState().getBlock().getClass()));

                    this.getLevel().setBlock(getBlockPos(), this.getBlockState().setValue(TYPE, myNewType).setValue(MIXED, mixed),
                            11);
                    this.getLevel().setBlock(otherTank.getBlockPos(), otherBlockState.setValue(TYPE, otherNewType).setValue(MIXED, mixed),
                            11);
                    this.otherTank = otherTank;
                    otherTank.otherTank = this;
                    this.setChanged();
                    otherTank.setChanged();
                }
            }
        }
    }

    @Nullable
    private AbstractFluidTankEntity getTank(BlockPos pos) {
        return this.getTank(pos, TankType.SINGLE);
    }

    protected abstract boolean tileIsCorrectInstance(BlockEntity tileEntity);

    @Nullable
    private AbstractFluidTankEntity getTank(BlockPos pos, TankType validType) {
        if (this.getLevel() != null) {
            TankType tankType = this.getBlockState().getValue(TYPE);
            if (tankType == validType.getOpposite()) {
                BlockState blockState = this.getLevel().getBlockState(pos);
                if (blockState.hasProperty(TYPE) && blockState.getValue(TYPE) == validType) {
                    BlockEntity blockEntity = this.getLevel().getBlockEntity(pos);
                    if (tileIsCorrectInstance(blockEntity)) {
                        return (AbstractFluidTankEntity) blockEntity;
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

            FluidStack fluidStack = calculateFluidForSeparateTank();
            this.tank = new FluidTank(initialCapacity);
            this.tank.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    public FluidStack calculateFluidForSeparateTank() {
        return calculateFluidFormTank(this.tank, getTankType());
    }

    @Override
    public int getCapacity() {
        if (this.getBlockState().hasProperty(TYPE)) {
            TankType tankType = this.getBlockState().getValue(TYPE);
            if (tankType == TankType.SINGLE) {
                return initialCapacity;
            } else {
                return 2 * initialCapacity;
            }
        }
        return this.tank.getCapacity();
    }

    public int getRawInitialCapacity() {
        return rawInitialCapacity;
    }

    public FluidTank getTank() {
        return tank;
    }

    @SuppressWarnings({"unchecked"})
    private void setTank(FluidTank tank) {
        if (this.tank instanceof FluidTankWrapper tankWrapper) {
            tankWrapper.setInternalTank(tank);
        }
    }
}
