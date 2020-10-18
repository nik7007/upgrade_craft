package com.nik7.upgradecraft.container;

import com.nik7.upgradecraft.tileentity.AbstractFluidMachineTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;

import javax.annotation.Nullable;
import java.util.Objects;

public abstract class BaseMachineContainer<T extends AbstractFluidMachineTileEntity<?>> extends Container {

    protected final IIntArray data;
    protected final PlayerInventory playerInventory;
    @Nullable
    protected final T tileEntity;

    protected BaseMachineContainer(@Nullable ContainerType<?> type, int id, IIntArray data, PlayerInventory playerInventory, @Nullable T tileEntity) {
        super(type, id);
        this.data = data;
        this.playerInventory = playerInventory;
        this.tileEntity = tileEntity;
        trackIntArray(data);
    }


    protected void addPlayerSlots(PlayerInventory playerInventory, int x, int y) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, x + i * 18, y + 58));
        }
    }

    @Nullable
    public T getTileEntity() {
        return tileEntity;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return tileEntity != null && isWithinUsableDistance(IWorldPosCallable.of(Objects.requireNonNull(tileEntity.getWorld()), tileEntity.getPos()), playerIn, tileEntity.getBlockState().getBlock());
    }
}
