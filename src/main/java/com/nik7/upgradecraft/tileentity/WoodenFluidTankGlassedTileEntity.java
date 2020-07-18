package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.init.Config;
import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.PacketDirection;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

import static com.nik7.upgradecraft.blocks.AbstractFluidTankBlock.TYPE;
import static com.nik7.upgradecraft.init.RegisterTileEntity.WOODEN_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE;


public class WoodenFluidTankGlassedTileEntity extends AbstractFluidTankTileEntity {


    public WoodenFluidTankGlassedTileEntity() {
        super(WOODEN_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE.get(), Config.TANK_CAPACITY.get());
    }

    @Override
    protected void onFluidChange(Void aVoid) {
        if (world == null || world.isRemote) {
            return;
        }
        TankType tankType = getTankType();
        if (tankType != TankType.BOTTOM) {
            world.notifyBlockUpdate(pos, getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS);
        }
    }

    @Nullable
    public TankType getTankType() {
        BlockState blockState = getBlockState();
        if (blockState.func_235901_b_(TYPE)) {
            return blockState.get(TYPE);
        }

        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        TankType tankType = getTankType();
        if (tankType == TankType.BOTTOM) {
            return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
        }
        return super.getRenderBoundingBox();
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
