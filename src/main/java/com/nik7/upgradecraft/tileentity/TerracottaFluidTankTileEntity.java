package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.init.Config;
import net.minecraft.tileentity.TileEntity;

import static com.nik7.upgradecraft.init.RegisterTileEntity.TERRACOTTA_FLUID_TANK_TILE_ENTITY_TYPE;

public class TerracottaFluidTankTileEntity extends AbstractFluidTankTileEntity {

    public TerracottaFluidTankTileEntity() {
        super(TERRACOTTA_FLUID_TANK_TILE_ENTITY_TYPE.get(), Config.TANK_CAPACITY.get());
    }

    @Override
    protected void tankOperation() {

    }

    @Override
    protected boolean tileIsCorrectInstance(TileEntity tileEntity) {
        return tileEntity instanceof TerracottaFluidTankGlassedTileEntity || tileEntity instanceof TerracottaFluidTankTileEntity;
    }
}
