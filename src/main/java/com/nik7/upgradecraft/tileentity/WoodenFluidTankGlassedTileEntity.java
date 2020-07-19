package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.init.Config;
import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.nik7.upgradecraft.init.RegisterTileEntity.WOODEN_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE;


public class WoodenFluidTankGlassedTileEntity extends AbstractFluidTankTileEntity {


    public WoodenFluidTankGlassedTileEntity() {
        super(WOODEN_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE.get(), Config.TANK_CAPACITY.get());
    }

    @Override
    protected void onFluidChange(Void aVoid) {
        super.onFluidChange(aVoid);
        if (world == null || world.isRemote) {
            return;
        }
        TankType tankType = getTankType();
        if (tankType != TankType.BOTTOM && !isTankMixed()) {
            notifyBlockUpdate();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public AxisAlignedBB getRenderBoundingBox() {
        TankType tankType = getTankType();
        if (tankType == TankType.BOTTOM) {
            return new AxisAlignedBB(getPos(), getPos().add(1, 2, 1));
        }
        if (isTankMixed() && tankType == TankType.TOP) {
            return new AxisAlignedBB(getPos().add(0, -1, 0), getPos().add(1, 1, 1)
            );
        }
        return super.getRenderBoundingBox();
    }


    @Override
    protected boolean tileIsCorrectInstance(TileEntity tileEntity) {
        return tileEntity instanceof WoodenFluidTankGlassedTileEntity || tileEntity instanceof WoodenFluidTankTileEntity;
    }

}
