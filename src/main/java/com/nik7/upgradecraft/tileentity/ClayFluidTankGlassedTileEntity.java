package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.init.Config;
import net.minecraft.tileentity.TileEntity;

import static com.nik7.upgradecraft.init.RegisterTileEntity.CLAY_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE;

public class ClayFluidTankGlassedTileEntity extends AbstractFluidTankGlassedTileEntity {

    public ClayFluidTankGlassedTileEntity() {
        super(CLAY_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE.get(), Config.TANK_CAPACITY.get());
    }

    @Override
    protected boolean tileIsCorrectInstance(TileEntity tileEntity) {
        return tileEntity instanceof ClayFluidTankGlassedTileEntity || tileEntity instanceof ClayFluidTankTileEntity;
    }
}
