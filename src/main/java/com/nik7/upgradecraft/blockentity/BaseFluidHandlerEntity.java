package com.nik7.upgradecraft.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class BaseFluidHandlerEntity extends AbstractEntityFluidHandler {

    private int oldLuminosity = 0;


    public BaseFluidHandlerEntity(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        if (!exchangeInfoCS()) {
            return super.getUpdatePacket();
        }
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        if (!exchangeInfoCS()) {
            return super.getUpdateTag();
        }
        return saveWithoutMetadata();
    }

    protected boolean exchangeInfoCS() {
        return false;
    }

    protected void notifyBlockUpdate() {
        notifyBlockUpdate(this);
    }

    protected void notifyBlockUpdate(BlockEntity entity) {
        Level level = entity.getLevel();
        if (level == null) {
            return;
        }
        updateComparator();
        level.sendBlockUpdated(entity.getBlockPos(), entity.getBlockState(), entity.getBlockState(), 3);
        entity.setChanged();
    }

    public boolean isFluidHot() {
        FluidStack fluid = getFluid();
        return isFluidHot(fluid);
    }

    protected boolean isFluidHot(FluidStack fluidStack) {
        return fluidStack.getFluid().getAttributes().getTemperature(fluidStack) > 250 + 273;
    }

    public int getLuminosity() {
        FluidStack fluid = getFluid();
        int luminosity = fluid.getFluid().getAttributes().getLuminosity(fluid);
        if (getLevel() != null && getLevel().isClientSide()) {
            if (oldLuminosity != luminosity) {
                oldLuminosity = luminosity;
                getLevel().getLightEngine().checkBlock(getBlockPos());
            }
        }
        return luminosity;
    }

}
