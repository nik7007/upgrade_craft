package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.fluids.tanks.EventFluidTank;
import com.nik7.upgradecraft.init.Config;
import net.minecraftforge.fluids.FluidAttributes;

import static com.nik7.upgradecraft.init.RegisterTileEntity.FLUID_FURNACE_TILE_ENTITY_TYPE;

public class FluidFurnaceTileEntity extends BaseFluidHandlerTileEntity {
    public FluidFurnaceTileEntity() {
        super(FLUID_FURNACE_TILE_ENTITY_TYPE.get());
        this.tank = new EventFluidTank(Config.TANK_CAPACITY.get() * FluidAttributes.BUCKET_VOLUME, this::onFluidChange);
    }

    private void onFluidChange(Void aVoid) {
        notifyBlockUpdate();
    }


}
