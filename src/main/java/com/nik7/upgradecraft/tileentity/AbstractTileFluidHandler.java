package com.nik7.upgradecraft.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.TileFluidHandler;

import javax.annotation.Nonnull;

public abstract class AbstractTileFluidHandler extends TileFluidHandler {

    public AbstractTileFluidHandler(@Nonnull TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    protected void updateComparator() {
        updateComparator(this);
    }

    protected void updateComparator(TileEntity entity) {
        if (entity.getWorld() == null) {
            return;
        }
        entity.getWorld().updateComparatorOutputLevel(entity.getPos(), entity.getBlockState().getBlock());
    }

    public FluidStack getFluid() {
        return this.tank.getFluid().copy();
    }

    public int getFluidAmount() {
        return this.tank.getFluidAmount();
    }

    public int getCapacity() {
        return this.tank.getCapacity();
    }
}
