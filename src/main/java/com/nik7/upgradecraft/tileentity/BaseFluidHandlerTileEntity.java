package com.nik7.upgradecraft.tileentity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDirection;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.TileFluidHandler;

import javax.annotation.Nullable;

public abstract class BaseFluidHandlerTileEntity extends TileFluidHandler {

    private int oldLuminosity = 0;

    public BaseFluidHandlerTileEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
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

    protected void notifyBlockUpdate() {
        notifyBlockUpdate(this);
    }

    protected void notifyBlockUpdate(TileEntity entity) {
        World world = entity.getWorld();
        if (world == null) {
            return;
        }
        world.updateComparatorOutputLevel(entity.getPos(), entity.getBlockState().getBlock());
        world.notifyBlockUpdate(entity.getPos(), entity.getBlockState(), entity.getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
        entity.markDirty();
    }

    public FluidStack getFluid() {
        return this.tank.getFluid().copy();
    }

    public boolean isFluidHot() {
        FluidStack fluid = getFluid();
        return fluid.getFluid().getAttributes().getTemperature(fluid) > 250 + 273;
    }

    public int getCapacity() {
        return this.tank.getCapacity();
    }

    public int getLuminosity() {
        FluidStack fluid = getFluid();
        int luminosity = fluid.getFluid().getAttributes().getLuminosity(fluid);
        if (world != null && world.isRemote()) {
            if (oldLuminosity != luminosity) {
                oldLuminosity = luminosity;
                world.getLightManager().checkBlock(getPos());
            }
        }
        return luminosity;
    }

}
