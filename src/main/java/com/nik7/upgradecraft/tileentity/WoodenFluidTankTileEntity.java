package com.nik7.upgradecraft.tileentity;

import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.TileFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import static com.nik7.upgradecraft.init.RegisterTileEntity.WOODEN_FLUID_TANK_TILE_ENTITY_TYPE;

public class WoodenFluidTankTileEntity extends TileFluidHandler {
    public WoodenFluidTankTileEntity() {
        super(WOODEN_FLUID_TANK_TILE_ENTITY_TYPE.get());
        this.tank = new FluidTank(27 * FluidAttributes.BUCKET_VOLUME);
    }
}
