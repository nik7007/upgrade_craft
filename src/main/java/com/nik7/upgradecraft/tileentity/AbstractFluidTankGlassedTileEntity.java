package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.state.properties.TankType;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public abstract class AbstractFluidTankGlassedTileEntity extends AbstractFluidTankTileEntity {

    public AbstractFluidTankGlassedTileEntity(TileEntityType<? extends AbstractFluidTankTileEntity> tileEntityTypeIn, int initialCapacity) {
        super(tileEntityTypeIn, initialCapacity);
    }

    @Override
    protected void onFluidChange(Void aVoid) {
        super.onFluidChange(aVoid);
        if (world == null || world.isRemote()) {
            return;
        }
        if (!isTankMixed()) {
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
}
