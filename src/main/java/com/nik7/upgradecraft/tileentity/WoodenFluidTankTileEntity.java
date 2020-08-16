package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.init.Config;
import net.minecraft.tileentity.TileEntity;

import static com.nik7.upgradecraft.init.RegisterTileEntity.WOODEN_FLUID_TANK_TILE_ENTITY_TYPE;

public class WoodenFluidTankTileEntity extends AbstractFluidTankTileEntity implements BaseWoodenFluidTankTileEntity {

    public WoodenFluidTankTileEntity() {
        super(WOODEN_FLUID_TANK_TILE_ENTITY_TYPE.get(), Config.TANK_CAPACITY.get());
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
            updateBurningState(otherTank.getWorld(), otherTank.getPos(), otherTank.getBlockState(), isFluidHot());
        }
    }

    @Override
    protected boolean tileIsCorrectInstance(TileEntity tileEntity) {
        return tileEntity instanceof WoodenFluidTankTileEntity || tileEntity instanceof WoodenFluidTankGlassedTileEntity;
    }

}
