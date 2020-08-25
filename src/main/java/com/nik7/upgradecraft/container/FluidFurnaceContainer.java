package com.nik7.upgradecraft.container;

import com.nik7.upgradecraft.capabilities.FluidFurnaceItemHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.IIntArray;

import javax.annotation.Nullable;

public class FluidFurnaceContainer extends Container {
    protected FluidFurnaceContainer(@Nullable ContainerType<?> type, int id, IIntArray data, FluidFurnaceItemHandler furnaceItemHandler) {
        super(type, id);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return false;
    }
}
