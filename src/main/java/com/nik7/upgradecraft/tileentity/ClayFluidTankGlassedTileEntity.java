package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.init.Config;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

import static com.nik7.upgradecraft.init.RegisterTileEntity.CLAY_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE;

public class ClayFluidTankGlassedTileEntity extends AbstractFluidTankGlassedTileEntity implements BaseClayFluidTankTileEntity {
    private short cookingTick = 0;

    public ClayFluidTankGlassedTileEntity() {
        super(CLAY_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE.get(), Config.TANK_CAPACITY.get());
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        tag.putShort("cookingTick", cookingTick);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        cookingTick = tag.getShort("cookingTick");
        return tag;
    }


    @Override
    protected void tankOperation() {
        operate(this, tickNumber);
    }

    @Override
    public void increaseCooking() {
        cookingTick++;
    }

    @Override
    public void checkAndCook() {
        if (cookingTick == 450) {
            cookingTick = 0;
        }
    }

    @Override
    protected boolean tileIsCorrectInstance(TileEntity tileEntity) {
        return tileEntity instanceof ClayFluidTankGlassedTileEntity || tileEntity instanceof ClayFluidTankTileEntity;
    }
}
