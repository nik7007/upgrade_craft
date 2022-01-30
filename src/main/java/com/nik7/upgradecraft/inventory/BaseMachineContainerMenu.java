package com.nik7.upgradecraft.inventory;

import com.nik7.upgradecraft.blockentity.AbstractMachineEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public abstract class BaseMachineContainerMenu<T extends AbstractMachineEntity> extends AbstractContainerMenu {

    protected final ContainerData dataAccess;
    protected final Inventory playerInventory;
    @Nullable
    protected final T blockEntity;

    protected BaseMachineContainerMenu(@Nullable MenuType<?> pMenuType, int pContainerId, ContainerData dataAccess, Inventory playerInventory, @Nullable T blockEntity) {
        super(pMenuType, pContainerId);
        this.dataAccess = dataAccess;
        this.playerInventory = playerInventory;
        this.blockEntity = blockEntity;
        addDataSlots(dataAccess);
    }

    protected void addPlayerSlots(Inventory playerInventory, int x, int y) {
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
    public T getBlockEntity() {
        return blockEntity;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return blockEntity != null && stillValid(ContainerLevelAccess.create(Objects.requireNonNull(blockEntity.getLevel()), blockEntity.getBlockPos()),
                pPlayer,
                blockEntity.getBlockState().getBlock());
    }
}
