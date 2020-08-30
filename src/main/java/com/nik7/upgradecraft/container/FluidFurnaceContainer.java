package com.nik7.upgradecraft.container;

import com.nik7.upgradecraft.tileentity.FluidFurnaceTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import java.util.Objects;

import static com.nik7.upgradecraft.capabilities.FluidFurnaceItemHandler.INPUT;
import static com.nik7.upgradecraft.capabilities.FluidFurnaceItemHandler.OUTPUT;
import static com.nik7.upgradecraft.init.RegisterBlocks.FLUID_FURNACE_BLOCK;
import static com.nik7.upgradecraft.init.RegisterContainers.FLUID_FURNACE_CONTAINER_TYPE;

public class FluidFurnaceContainer extends Container {

    private final IIntArray data;
    private final PlayerInventory playerInventory;
    @Nullable
    private final FluidFurnaceTileEntity tileEntity;

    public FluidFurnaceContainer(int windowId, PlayerInventory inv, PacketBuffer data) {
        this(windowId, new IntArray(3), inv, (FluidFurnaceTileEntity) inv.player.world.getTileEntity(data.readBlockPos()), new ItemStackHandler(2));
    }

    public FluidFurnaceContainer(int id, IIntArray data, PlayerInventory playerInventory, FluidFurnaceTileEntity tileEntity, ItemStackHandler handler) {
        super(FLUID_FURNACE_CONTAINER_TYPE.get(), id);
        this.data = data;
        this.playerInventory = playerInventory;
        this.tileEntity = tileEntity;

        this.addSlot(new SlotItemHandler(handler, INPUT, 56, 17));
        this.addSlot(new FluidFurnaceResultSlot(playerInventory.player, tileEntity, handler, OUTPUT, 116, 35));

        addPlayerSlots(playerInventory, 8, 84);

        trackIntArray(data);
    }

    private void addPlayerSlots(PlayerInventory playerInventory, int x, int y) {
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, x + j * 18, y + i * 18));
            }
        }

        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, x + i * 18, y + 58));
        }
    }

    public int getBurnTime() {
        return this.data.get(0);
    }

    public int getCookTime() {
        return this.data.get(1);
    }

    public int getCookTimeTotal() {
        return this.data.get(2);
    }

    @Nullable
    public FluidFurnaceTileEntity getTileEntity() {
        return tileEntity;
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return isWithinUsableDistance(IWorldPosCallable.of(Objects.requireNonNull(tileEntity.getWorld()), tileEntity.getPos()), playerIn, FLUID_FURNACE_BLOCK.get());
    }

    @Override
    public ItemStack transferStackInSlot(PlayerEntity player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            if (slotIndex == OUTPUT) {
                if (!this.mergeItemStack(slotStack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(slotStack, itemStack);
            } else if (slotIndex != INPUT) {
                if (hasRecipe(slotStack)) {
                    if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex < 29) {
                    if (!this.mergeItemStack(slotStack, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex < 38 && !this.mergeItemStack(slotStack, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotStack, 2, 38, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return itemStack;
    }

    private boolean hasRecipe(ItemStack stack) {
        World world = playerInventory.player.getEntityWorld();
        return world.getRecipeManager().getRecipe(IRecipeType.SMELTING, new Inventory(stack), world).isPresent();
    }
}
