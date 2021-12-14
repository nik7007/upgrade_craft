package com.nik7.upgradecraft.blockentity;

import com.nik7.upgradecraft.tanks.EventFluidTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;

import static com.nik7.upgradecraft.block.FunnelBlock.ENABLED;
import static com.nik7.upgradecraft.block.FunnelBlock.FACING;
import static com.nik7.upgradecraft.init.RegisterBlockEntity.FUNNEL_ENTITY_TYPE;
import static com.nik7.upgradecraft.utils.LazyOptionalHelper.getHandler;

public class FunnelEntity extends AbstractEntityFluidHandler {
    private static final int FLUID_TRANSFER_QUANTITY = 5;

    public FunnelEntity(BlockPos blockPos, BlockState blockState) {
        super(FUNNEL_ENTITY_TYPE.get(), blockPos, blockState);
        this.tank = new EventFluidTank(5 * FluidAttributes.BUCKET_VOLUME, this::onFluidChange);
    }

    protected void onFluidChange(Void aVoid) {
        updateComparator();
    }

    @Override
    public void tick() {
        if (level == null || level.isClientSide()) {
            return;
        }
        if (!getBlockState().getValue(ENABLED)) {
            return;
        }

        fill(level);
        drain(level);
    }

    private void fill(Level level) {
        if (this.tank.getSpace() == 0) {
            return;
        }
        BlockPos up = getBlockPos().above();
        IFluidHandler source = getFluidHandler(level, up, Direction.DOWN);
        if (source != null) {
            transferFluid(source, this.tank);
        }
    }

    @Nullable
    private IFluidHandler getFluidHandler(Level level, BlockPos pos, Direction direction) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity != null) {
            LazyOptional<IFluidHandler> capability = blockEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, direction);
            return getHandler(capability);
        }
        return null;
    }

    private void transferFluid(IFluidHandler source, IFluidHandler destination) {
        FluidStack fluidStack = source.drain(FLUID_TRANSFER_QUANTITY, IFluidHandler.FluidAction.SIMULATE);
        if (fluidStack.isEmpty()) {
            return;
        }
        int fill = destination.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
        if (fill > 0) {
            source.drain(fill, IFluidHandler.FluidAction.EXECUTE);
        }
    }

    private void drain(Level level) {
        if (this.tank.isEmpty()) {
            return;
        }

        Direction direction = getBlockState().getValue(FACING);
        BlockPos otherPosition = getBlockPos().offset(direction.getNormal());
        IFluidHandler destination = getFluidHandler(level, otherPosition, direction.getOpposite());
        if (destination != null) {
            transferFluid(this.tank, destination);
        }

    }

}
