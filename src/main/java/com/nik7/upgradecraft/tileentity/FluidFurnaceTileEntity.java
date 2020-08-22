package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.capabilities.FluidFurnaceItemHandler;
import com.nik7.upgradecraft.fluids.tanks.EventFluidTank;
import com.nik7.upgradecraft.init.Config;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.nik7.upgradecraft.init.RegisterTileEntity.FLUID_FURNACE_TILE_ENTITY_TYPE;

public class FluidFurnaceTileEntity extends BaseFluidHandlerTileEntity {

    private final LazyOptional<ItemStackHandler> inventory;
    private final FluidFurnaceItemHandler furnaceItemHandler;


    public FluidFurnaceTileEntity() {
        super(FLUID_FURNACE_TILE_ENTITY_TYPE.get());
        this.tank = new EventFluidTank(Config.TANK_CAPACITY.get() * FluidAttributes.BUCKET_VOLUME,
                this::onFluidChange,
                this::isFluidHot);
        this.furnaceItemHandler = new FluidFurnaceItemHandler(slot -> {
            this.markDirty();
            if (world != null && !world.isRemote()) {
                world.updateComparatorOutputLevel(pos, getBlockState().getBlock());
            }
        });
        this.inventory = LazyOptional.of(() -> furnaceItemHandler);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        CompoundNBT inventory = tag.getCompound("inventory");
        furnaceItemHandler.deserializeNBT(inventory);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        CompoundNBT inventory = furnaceItemHandler.serializeNBT();
        tag.put("inventory", inventory);
        return tag;
    }

    private void onFluidChange(Void aVoid) {
        notifyBlockUpdate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventory.cast();
        return super.getCapability(capability, facing);
    }
}
