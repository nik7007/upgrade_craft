package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.init.Config;
import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDirection;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

import static com.nik7.upgradecraft.blocks.AbstractFluidTankBlock.TYPE;
import static com.nik7.upgradecraft.init.RegisterTileEntity.WOODEN_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE;


public class WoodenFluidTankGlassedTileEntity extends AbstractFluidTankTileEntity {


    public WoodenFluidTankGlassedTileEntity() {
        super(WOODEN_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE.get(), Config.TANK_CAPACITY.get());
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

    @Override
    protected boolean tileIsCorrectInstance(TileEntity tileEntity) {
        return tileEntity instanceof WoodenFluidTankGlassedTileEntity;
    }

    public FluidStack getFluid() {
        return this.tank.getFluid().copy();
    }

    public int getCapacity() {
        if (this.getBlockState().func_235901_b_(TYPE)) {
            TankType tankType = this.getBlockState().get(TYPE);
            if (tankType == TankType.SINGLE) {
                return initialCapacity;
            } else {
                return 2 * initialCapacity;
            }
        }
        return this.tank.getCapacity();
    }
}
