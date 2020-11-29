package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.blocks.ClayFluidTankBlock;
import com.nik7.upgradecraft.init.Config;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import static com.nik7.upgradecraft.init.RegisterBlocks.TERRACOTTA_FLUID_TANK_GLASSED_BLOCK;
import static com.nik7.upgradecraft.init.RegisterTileEntity.CLAY_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE;
import static com.nik7.upgradecraft.tileentity.ClayFluidTankTileEntity.MAX_COOKING_TICK;

public class ClayFluidTankGlassedTileEntity extends AbstractFluidTankGlassedTileEntity implements BaseClayFluidTankTileEntity {
    private short cookingTick = 0;
    private short oldCookingTick = 0;
    private boolean waterloggedCheck = false;

    public ClayFluidTankGlassedTileEntity() {
        super(CLAY_FLUID_TANK_GLASSED_TILE_ENTITY_TYPE.get(), Config.TANK_CAPACITY.get());
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        cookingTick = tag.getShort("cookingTick");
        oldCookingTick = tag.getShort("oldCookingTick");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        tag.putShort("cookingTick", cookingTick);
        tag.putShort("oldCookingTick", oldCookingTick);
        return tag;
    }

    @SuppressWarnings("DuplicatedCode")
    @Override
    public void waterloggedCheck() {
        if (!waterloggedCheck) {
            waterloggedCheck = true;
            if (world != null) {
                Boolean blockCooking = getBlockState().get(ClayFluidTankBlock.COOKING);

                if (blockCooking) {
                    world.setBlockState(getPos(), getBlockState().with(ClayFluidTankBlock.COOKING, false), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                }
            }
        }
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
        waterloggedCheck = false;
        checkIsCooking();
        if (cookingTick == MAX_COOKING_TICK) {
            cookingTick = 0;
            cookTank(this, TERRACOTTA_FLUID_TANK_GLASSED_BLOCK.get());
        }
    }

    @SuppressWarnings("DuplicatedCode")
    private void checkIsCooking() {
        if (tickNumber % 24 == 0) {
            boolean isCooking = cookingTick != oldCookingTick;

            if (isCooking) {
                oldCookingTick = cookingTick;
            }

            if (world != null) {
                Boolean blockCooking = getBlockState().get(ClayFluidTankBlock.COOKING);

                if (blockCooking != isCooking) {
                    world.setBlockState(getPos(), getBlockState().with(ClayFluidTankBlock.COOKING, isCooking), Constants.BlockFlags.DEFAULT_AND_RERENDER);
                }
            }
        }
    }

    @Override
    protected boolean tileIsCorrectInstance(TileEntity tileEntity) {
        return tileEntity instanceof ClayFluidTankGlassedTileEntity || tileEntity instanceof ClayFluidTankTileEntity;
    }
}
