package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.blocks.ClayFluidTankBlock;
import com.nik7.upgradecraft.init.Config;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import static com.nik7.upgradecraft.init.RegisterTileEntity.CLAY_FLUID_TANK_TILE_ENTITY_TYPE;

public class ClayFluidTankTileEntity extends AbstractFluidTankTileEntity implements BaseClayFluidTankTileEntity {
    public static final short MAX_COOKING_TICK = 450;

    private short cookingTick = 0;
    private short oldCookingTick = 0;
    private boolean waterloggedCheck = false;

    public ClayFluidTankTileEntity() {
        super(CLAY_FLUID_TANK_TILE_ENTITY_TYPE.get(), Config.TANK_CAPACITY.get());
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        tag.putShort("cookingTick", cookingTick);
        tag.putShort("oldCookingTick", oldCookingTick);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        cookingTick = tag.getShort("cookingTick");
        oldCookingTick = tag.getShort("oldCookingTick");
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
        }
    }

    @SuppressWarnings("DuplicatedCode")
    protected void checkIsCooking() {
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