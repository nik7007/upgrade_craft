package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.init.Config;
import net.minecraft.tileentity.TileEntity;

import static com.nik7.upgradecraft.init.RegisterTileEntity.WOODEN_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE;


public class WoodenFluidTankGlassedTileEntity extends AbstractFluidTankGlassedTileEntity implements BaseWoodenFluidTankTileEntity {

    public WoodenFluidTankGlassedTileEntity() {
        super(WOODEN_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE.get(), Config.TANK_CAPACITY.get());
    }

    @Override
    protected void tankOperation() {
        setBlockInFire(this, tickNumber);
    }

    @Override
    protected void onFluidChange(Void aVoid) {
        super.onFluidChange(aVoid);
        if (world == null || world.isRemote()) {
            return;
        }
        updateBurningState(world, getPos(), getBlockState(), isFluidHot());
    }

    @Override
    protected void otherSeparateTank(AbstractFluidTankTileEntity otherTank) {
        super.otherSeparateTank(otherTank);
        if (otherTank.getWorld() != null) {
            updateBurningState(otherTank.getWorld(), otherTank.getPos(), otherTank.getBlockState(), otherTank.isFluidHot());
        }
    }

    @Override
    protected boolean tileIsCorrectInstance(TileEntity tileEntity) {
        return tileEntity instanceof WoodenFluidTankGlassedTileEntity || tileEntity instanceof WoodenFluidTankTileEntity;
    }

}
