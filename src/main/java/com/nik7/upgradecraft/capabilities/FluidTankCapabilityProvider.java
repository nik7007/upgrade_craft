package com.nik7.upgradecraft.capabilities;

import com.nik7.upgradecraft.fluids.tanks.EventFluidTank;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FluidTankCapabilityProvider implements ICapabilityProvider {

    private final ItemStack itemStack;
    private final EventFluidTank tank;
    private final LazyOptional<IFluidHandler> holder;

    public FluidTankCapabilityProvider(ItemStack itemStack, int capacity) {
        this.itemStack = itemStack;
        this.tank = new EventFluidTank(capacity * FluidAttributes.BUCKET_VOLUME, this::onFluidChange);
        CompoundNBT tag = itemStack.getTag();
        if (tag != null) {
            this.tank.readFromNBT(tag);
        }
        this.holder = LazyOptional.of(() -> tank);
    }

    private void onFluidChange(Void aVoid) {
        CompoundNBT tag = itemStack.getTag();
        if (tag == null) {
            tag = new CompoundNBT();
        }
        this.tank.writeToNBT(tag);
        itemStack.setTag(tag);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return holder.cast();
        return LazyOptional.empty();
    }
}
