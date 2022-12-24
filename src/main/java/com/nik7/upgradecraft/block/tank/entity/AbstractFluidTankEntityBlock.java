package com.nik7.upgradecraft.block.tank.entity;

import com.nik7.upgradecraft.block.entity.AbstractEntityFluidHandler;
import com.nik7.upgradecraft.fluids.capabillity.EventFluidTank;
import com.nik7.upgradecraft.fluids.capabillity.FluidTankWrapper;
import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Optional;

import static com.nik7.upgradecraft.block.tank.AbstractFluidTankBlock.MIXED;
import static com.nik7.upgradecraft.block.tank.AbstractFluidTankBlock.TYPE;

public abstract class AbstractFluidTankEntityBlock extends AbstractEntityFluidHandler {

    protected AbstractFluidTankEntityBlock otherTank = null;
    private Integer originalCapacity = null;

    private final MergeInfo mergeInfoWithAbove = new MergeInfo(this.getBlockPos().above(), TankType.BOTTOM, TankType.TOP);
    private final MergeInfo mergeInfoWithBelow = new MergeInfo(this.getBlockPos().below(), TankType.TOP, TankType.BOTTOM);

    private int oldLuminosity = 0;
    protected final boolean glassed;

    public AbstractFluidTankEntityBlock(@NotNull BlockEntityType<?> blockEntityType,
                                        BlockPos pos,
                                        BlockState state,
                                        boolean glassed) {
        super(blockEntityType, pos, state);
        this.tank = new FluidTankWrapper<>(new EventFluidTank(0, this::onChangeFluid));
        this.glassed = glassed;
    }

    @SuppressWarnings("unchecked")
    private FluidTankWrapper<EventFluidTank> getTank() {
        return (FluidTankWrapper<EventFluidTank>) this.tank;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (createDoubleTankClient()) {
            super.load(tag);
        }
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        if (this.getLevel() != null && !this.getLevel().isClientSide()) {
            this.getSingleTank().writeToNBT(tag);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.getLevel() != null && !this.getLevel().isClientSide()) {
            if (getTank().getCapacity() <= 0) {
                getTank().setCapacity(getOriginalCapacity());
            }
            notifyBlockUpdate(this);
            tryToMergeTanks();
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return tank.writeToNBT(new CompoundTag());
    }

    public boolean isTankMixed() {
        BlockState blockState = getBlockState();
        if (blockState.hasProperty(MIXED)) {
            return blockState.getValue(MIXED);
        }
        return false;
    }

    private boolean createDoubleTankClient() {
        if (this.getLevel() == null || !this.getLevel().isClientSide()) {
            return false;
        }
        if (otherTank != null) {
            return false;
        }

        BlockState blockState = getBlockState();
        if (!glassed || !(blockState.hasProperty(MIXED) && blockState.getValue(MIXED))) {
            return false;
        }

        if (!blockState.hasProperty(TYPE)) {
            return false;
        }

        TankType tankType = blockState.getValue(TYPE);
        if (tankType == TankType.SINGLE) {
            return false;
        }

        Optional<MergeInfo> optionalMergeInfo = Arrays.stream(getMergeInfo())
                .filter(mergeInfo -> mergeInfo.myNewType == tankType)
                .findFirst();
        if (optionalMergeInfo.isEmpty()) {
            return false;
        }
        MergeInfo mergeInfo = optionalMergeInfo.get();
        otherTank = getEntityTank(mergeInfo.blockPos, mergeInfo.otherNewType);
        if (otherTank == null) {
            return false;
        }
        EventFluidTank newTank = createNewTank(getOriginalCapacity() * 2, this.getFluid(), otherTank.getFluid());
        this.getTank().setInternalTank(newTank);
        otherTank.getTank().setInternalTank(newTank);

        return true;
    }

    protected void onChangeFluid(FluidStack fluidStack) {
        if (getLevel() == null || getLevel().isClientSide()) {
            return;
        }

        TankType tankType = getTankType();
        if (tankType == TankType.TOP && this.otherTank != null) {
            this.otherTank.onChangeFluid(fluidStack);
            return;
        }
        setChanged();
        if (otherTank != null) {
            otherTank.setChanged();
        }
        if (glassed) {
            sendUpdateToTheClient(this);
        } else if (otherTank != null && otherTank.glassed) {
            sendUpdateToTheClient(this);
        }
    }

    protected static void sendUpdateToTheClient(AbstractEntityFluidHandler entityFluidHandler) {
        Level level = entityFluidHandler.getLevel();
        if (level == null || level.isClientSide()) {
            return;
        }
        BlockState blockState = entityFluidHandler.getBlockState();
        level.sendBlockUpdated(entityFluidHandler.getBlockPos(), blockState, blockState, Block.UPDATE_CLIENTS);
    }

    protected static void notifyBlockUpdate(BlockEntity entity) {
        Level level = entity.getLevel();
        if (level == null) {
            return;
        }
        updateComparator(entity);
        level.sendBlockUpdated(entity.getBlockPos(), entity.getBlockState(), entity.getBlockState(), Block.UPDATE_ALL);
        entity.setChanged();
    }

    protected abstract int originalCapacityProvider();

    protected int getOriginalCapacity() {
        if (originalCapacity == null) {
            originalCapacity = originalCapacityProvider() * FluidType.BUCKET_VOLUME;
        }
        return originalCapacity;
    }

    protected abstract boolean tileIsMergeableInstance(BlockEntity tileEntity);

    @Nullable
    private AbstractFluidTankEntityBlock getEntityTank(BlockPos pos, TankType validType) {
        if (this.getLevel() == null) {
            return null;
        }
        TankType tankType = this.getBlockState().getValue(TYPE);
        if (tankType == validType.getOpposite()) {
            BlockState blockState = this.getLevel().getBlockState(pos);
            if (blockState.hasProperty(TYPE) && blockState.getValue(TYPE) == validType) {
                BlockEntity blockEntity = this.getLevel().getBlockEntity(pos);
                if (tileIsMergeableInstance(blockEntity)) {
                    return (AbstractFluidTankEntityBlock) blockEntity;
                }
            }
        }
        return null;
    }

    @Nullable
    public TankType getTankType() {
        BlockState blockState = getBlockState();
        if (blockState.hasProperty(TYPE)) {
            return blockState.getValue(TYPE);
        }

        return null;
    }

    protected boolean fluidsAreCompatible(FluidStack fluidStack1, FluidStack fluidStack2) {
        if (fluidStack1.isEmpty() || fluidStack2.isEmpty()) {
            return true;
        } else {
            return fluidStack1.getFluid().isSame(fluidStack2.getFluid());
        }
    }

    private EventFluidTank createNewTank(int capacity, FluidStack fluidStack1, FluidStack fluidStack2) {
        EventFluidTank newTank = new EventFluidTank(capacity);
        newTank.fill(fluidStack1.copy(), IFluidHandler.FluidAction.EXECUTE);
        newTank.fill(fluidStack2.copy(), IFluidHandler.FluidAction.EXECUTE);
        newTank.setOnChange(this::onChangeFluid);
        return newTank;
    }

    private void changeTank(AbstractFluidTankEntityBlock entityBlock, EventFluidTank newTank) {
        entityBlock.getTank().setInternalTank(newTank);
        notifyBlockUpdate(entityBlock);
    }

    public void mergeTank() {
        if (this.getLevel() == null || this.getLevel().isClientSide()) {
            return;
        }

        TankType myTankType = getTankType();
        if (this.otherTank != null && this.getCapacity() == getOriginalCapacity() * 2 && myTankType != TankType.SINGLE) {
            return;
        }

        AbstractFluidTankEntityBlock otherTank = null;
        TankType myNewType = TankType.SINGLE;
        TankType otherNewType = TankType.SINGLE;

        MergeInfo[] mergeInfo = getMergeInfo();
        for (MergeInfo info : mergeInfo) {
            otherTank = getEntityTank(info.blockPos, Optional.ofNullable(myTankType).orElse(TankType.SINGLE).getOpposite());
            myNewType = info.myNewType;
            otherNewType = info.otherNewType;

            if (otherTank != null && otherTank.otherTank != null) {
                otherTank = null;
            }
            if (otherTank != null) {
                break;
            }
        }

        if (otherTank == null) {
            if (myTankType != TankType.SINGLE) {
                EventFluidTank newTank = createNewTank(getOriginalCapacity(), this.getFluid(), FluidStack.EMPTY);
                changeTank(this, newTank);
                this.getLevel().setBlock(getBlockPos(), this.getBlockState().setValue(TYPE, TankType.SINGLE).setValue(MIXED, false),
                        Block.UPDATE_ALL_IMMEDIATE);
            }
            return;
        }
        if (!fluidsAreCompatible(otherTank.getFluid(), this.getFluid())) {
            return;
        }
        EventFluidTank newTank = createNewTank(getOriginalCapacity() * 2, this.getFluid(), otherTank.getFluid());
        changeTank(this, newTank);
        changeTank(otherTank, newTank);
        BlockState otherBlockState = otherTank.getBlockState();

        boolean mixed = !(otherBlockState.getBlock().getClass().equals(this.getBlockState().getBlock().getClass()));

        this.getLevel().setBlock(getBlockPos(), this.getBlockState().setValue(TYPE, myNewType).setValue(MIXED, mixed),
                Block.UPDATE_ALL_IMMEDIATE);
        this.getLevel().setBlock(otherTank.getBlockPos(), otherBlockState.setValue(TYPE, otherNewType).setValue(MIXED, mixed),
                Block.UPDATE_ALL_IMMEDIATE);

        this.otherTank = otherTank;
        otherTank.otherTank = this;
        this.setChanged();
        otherTank.setChanged();
    }

    private void tryToMergeTanks() {
        if (otherTank != null) {
            return;
        }
        mergeTank();
    }

    protected void separateTanks() {
        Level level = this.getLevel();
        if (level == null || level.isClientSide() || getTankType() == TankType.SINGLE || otherTank == null) {
            return;
        }
        MinecraftServer server = level.getServer();
        if (server != null && !server.isRunning()) {
            return;
        }

        madeTankSingle(otherTank);
    }

    private void madeTankSingle(AbstractFluidTankEntityBlock tankEntityBlock) {
        changeTank(tankEntityBlock, tankEntityBlock.getSingleEventTank());
        madeBlockSingle(tankEntityBlock);
        tankEntityBlock.otherTank = null;
    }

    private void madeBlockSingle(AbstractFluidTankEntityBlock tankEntityBlock) {
        if (tankEntityBlock == null || tankEntityBlock.getLevel() == null) {
            return;
        }
        tankEntityBlock.getLevel().setBlock(tankEntityBlock.getBlockPos(), tankEntityBlock.getBlockState().setValue(TYPE, TankType.SINGLE).setValue(MIXED, false), Block.UPDATE_ALL_IMMEDIATE);
    }

    protected EventFluidTank getSingleEventTank() {
        final int originalCapacity = getOriginalCapacity();
        FluidStack fluid = getSingleTankFluid(originalCapacity);
        return createNewTank(originalCapacity, fluid, FluidStack.EMPTY);
    }

    protected FluidTank getSingleTank() {
        final int originalCapacity = getOriginalCapacity();
        FluidStack fluid = getSingleTankFluid(originalCapacity);
        FluidTank fluidTank = new FluidTank(originalCapacity);
        fluidTank.fill(fluid, IFluidHandler.FluidAction.EXECUTE);
        return fluidTank;
    }

    @NotNull
    protected FluidStack getSingleTankFluid(int originalCapacity) {
        FluidStack fluid = FluidStack.EMPTY;

        TankType tankType = getTankType();
        if (tankType != null) {
            int fluidAmount = this.getFluidAmount();

            int singleAmount = switch (tankType) {
                case TOP -> Math.max(0, fluidAmount - originalCapacity);
                case BOTTOM -> Math.min(originalCapacity, fluidAmount);
                case SINGLE -> fluidAmount;
            };

            if (singleAmount > 0) {
                fluid = this.getFluid();
                fluid.setAmount(singleAmount);
            }

        }
        return fluid;
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        separateTanks();
    }

    public int getLuminosity() {
        FluidStack fluid = getFluid();
        int luminosity = fluid.getFluid().getFluidType().getLightLevel(fluid);
        if (getLevel() != null && getLevel().isClientSide()) {
            if (oldLuminosity != luminosity) {
                oldLuminosity = luminosity;
                getLevel().getLightEngine().checkBlock(getBlockPos());
            }
        }
        return luminosity;
    }

    protected MergeInfo[] getMergeInfo() {

        if (getTankType() == TankType.BOTTOM) {
            return new MergeInfo[]{mergeInfoWithAbove, mergeInfoWithBelow};
        }

        return new MergeInfo[]{mergeInfoWithBelow, mergeInfoWithAbove};
    }

    private record MergeInfo(BlockPos blockPos, TankType myNewType, TankType otherNewType) {
    }
}
