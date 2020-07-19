package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.init.Config;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;

import static com.nik7.upgradecraft.init.RegisterTileEntity.WOODEN_FLUID_TANK_TILE_ENTITY_TYPE;

public class WoodenFluidTankTileEntity extends AbstractFluidTankTileEntity {

    public WoodenFluidTankTileEntity() {
        super(WOODEN_FLUID_TANK_TILE_ENTITY_TYPE.get(), Config.TANK_CAPACITY.get());
    }

    @Override
    protected boolean tileIsCorrectInstance(TileEntity tileEntity) {
        return tileEntity instanceof WoodenFluidTankTileEntity || tileEntity instanceof WoodenFluidTankGlassedTileEntity;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        if (!isTankMixed()) {
            return null;
        }
        return super.getUpdatePacket();
    }
}
