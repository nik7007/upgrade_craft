package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.fluids.tanks.EventFluidTank;
import com.nik7.upgradecraft.fluids.tanks.FluidTankWrapper;
import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDirection;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import javax.annotation.Nullable;

import static com.nik7.upgradecraft.UpgradeCraft.LOGGER;
import static com.nik7.upgradecraft.blocks.AbstractFluidTankBlock.MIXED;
import static com.nik7.upgradecraft.blocks.WoodenFluidTankBlock.TYPE;

public abstract class AbstractFluidTankTileEntity extends BaseFluidHandlerTileEntity implements ITickableTileEntity {
    private boolean firstTick = true;
    protected AbstractFluidTankTileEntity otherTank = null;
    protected final int initialCapacity;
    protected short tickNumber;

    public AbstractFluidTankTileEntity(TileEntityType<? extends AbstractFluidTankTileEntity> tileEntityTypeIn,
                                       int initialCapacity) {
        super(tileEntityTypeIn);
        this.initialCapacity = initialCapacity * FluidAttributes.BUCKET_VOLUME;
        this.tank = new FluidTankWrapper<>(new EventFluidTank(this.initialCapacity, this::onFluidChange));
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        tickNumber = tag.getShort("tickNumber");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.putShort("tickNumber", tickNumber);
        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(this.getPos(), 0, getUpdateTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return write(new CompoundNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        if (world != null && world.isRemote() && net.getDirection() == PacketDirection.CLIENTBOUND) {
            handleUpdateTag(getBlockState(), pkt.getNbtCompound());
        }
    }

    protected void onFluidChange(Void aVoid) {
        if (world != null && !world.isRemote()) {
            world.updateComparatorOutputLevel(pos, getBlockState().getBlock());
            TankType tankType = getTankType();
            BlockPos blockPos = null;
            if (tankType == TankType.BOTTOM) {
                blockPos = pos.up();
            } else if (tankType == TankType.TOP) {
                blockPos = pos.down();
            }
            if (blockPos != null) {
                world.updateComparatorOutputLevel(blockPos, world.getBlockState(blockPos).getBlock());
            }
            if (isTankMixed()) {
                notifyBlockUpdate();
            }
            markDirty();
        }
    }

    @Override
    protected void notifyBlockUpdate() {
        if (world != null) {
            notifyBlockUpdate(this);
            if (otherTank != null) {
                notifyBlockUpdate(otherTank);
            }
        }
    }

    @Override
    protected void notifyBlockUpdate(TileEntity fluidTank) {
        if (world == null) {
            return;
        }
        world.notifyBlockUpdate(fluidTank.getPos(), fluidTank.getBlockState(), fluidTank.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
    }

    protected void otherSeparateTank(AbstractFluidTankTileEntity otherTank) {
        if (otherTank == null || otherTank.world == null || otherTank.isRemoved()) {
            return;
        }
        TankType tankType = otherTank.getBlockState().get(TYPE);
        if (tankType != TankType.SINGLE) {
            boolean imBottom = tankType == TankType.BOTTOM;
            FluidTank singleTank = new EventFluidTank(initialCapacity, otherTank::onFluidChange);
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
            if (otherTank.getBlockState().hasProperty(TYPE)) {
                otherTank.world.setBlockState(otherTank.getPos(), otherTank.getBlockState().with(TYPE, TankType.SINGLE).with(MIXED, false), Constants.BlockFlags.DEFAULT_AND_RERENDER);
            }
            otherTank.markDirty();
            otherTank.otherTank = null;
        }
    }

    @Override
    public void tick() {
        initTitleEntity();

        if (world != null && !world.isRemote()) {
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
            TankType tankType = this.getBlockState().get(TYPE);
            if (tankType == TankType.SINGLE) {
                return;
            }
            boolean isBottom = tankType == TankType.BOTTOM;
            BlockPos pos = isBottom ? this.getPos().up() : this.getPos().down();
            TankType validType = isBottom ? TankType.TOP : TankType.BOTTOM;
            AbstractFluidTankTileEntity otherTank = getTank(pos, validType);
            if (otherTank != null) {
                FluidTank doubleTank = new EventFluidTank(2 * initialCapacity, this::onFluidChange);
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
            AbstractFluidTankTileEntity otherTank = getTank(this.getPos().up());
            boolean imBottom = false;
            if (otherTank != null) {
                imBottom = true;
            } else {
                otherTank = getTank(this.getPos().down());
            }
            if (otherTank != null) {

                FluidTank doubleTank = new EventFluidTank(2 * initialCapacity, this::onFluidChange);
                int amount = doubleTank.fill(this.tank.getFluid().copy(), IFluidHandler.FluidAction.EXECUTE);
                FluidStack otherFluid = otherTank.tank.getFluid().copy();
                int newAmount = doubleTank.fill(otherFluid, IFluidHandler.FluidAction.EXECUTE);

                if (newAmount == amount + otherFluid.getAmount()) {
                    this.setTank(doubleTank);
                    otherTank.setTank(doubleTank);

                    TankType myNewType = imBottom ? TankType.BOTTOM : TankType.TOP;
                    TankType otherNewType = myNewType == TankType.BOTTOM ? TankType.TOP : TankType.BOTTOM;
                    BlockState otherBlockState = otherTank.getBlockState();


                    boolean mixed = !(otherBlockState.getBlock().getClass().equals(this.getBlockState().getBlock().getClass()));

                    this.world.setBlockState(getPos(), this.getBlockState().with(TYPE, myNewType).with(MIXED, mixed),
                            Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.RERENDER_MAIN_THREAD);
                    this.world.setBlockState(otherTank.getPos(), otherBlockState.with(TYPE, otherNewType).with(MIXED, mixed),
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
    private AbstractFluidTankTileEntity getTank(BlockPos pos) {
        return this.getTank(pos, TankType.SINGLE);
    }

    protected abstract boolean tileIsCorrectInstance(TileEntity tileEntity);

    @Nullable
    private AbstractFluidTankTileEntity getTank(BlockPos pos, TankType validType) {
        if (this.world != null) {
            TankType tankType = this.getBlockState().get(TYPE);
            if (tankType == validType.getOpposite()) {
                BlockState blockState = this.world.getBlockState(pos);
                if (blockState.hasProperty(TYPE) && blockState.get(TYPE) == validType) {
                    TileEntity tileEntity = this.world.getTileEntity(pos);
                    if (tileIsCorrectInstance(tileEntity)) {
                        return (AbstractFluidTankTileEntity) tileEntity;
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

    @Nullable
    public TankType getTankType() {
        BlockState blockState = getBlockState();
        if (blockState.hasProperty(TYPE)) {
            return blockState.get(TYPE);
        }

        return null;
    }

    public boolean isTankMixed() {
        BlockState blockState = getBlockState();
        if (blockState.hasProperty(MIXED)) {
            return blockState.get(MIXED);
        }
        return false;
    }

    @Override
    public int getCapacity() {
        if (this.getBlockState().hasProperty(TYPE)) {
            TankType tankType = this.getBlockState().get(TYPE);
            if (tankType == TankType.SINGLE) {
                return initialCapacity;
            } else {
                return 2 * initialCapacity;
            }
        }
        return this.tank.getCapacity();
    }

    public FluidTank getTank() {
        return tank;
    }
}
