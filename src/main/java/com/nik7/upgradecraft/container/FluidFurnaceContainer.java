package com.nik7.upgradecraft.container;

import com.nik7.upgradecraft.capabilities.FluidFurnaceItemHandler;
import com.nik7.upgradecraft.tileentity.FluidFurnaceTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import static com.nik7.upgradecraft.capabilities.FluidFurnaceItemHandler.INPUT;
import static com.nik7.upgradecraft.capabilities.FluidFurnaceItemHandler.OUTPUT;
import static com.nik7.upgradecraft.init.RegisterContainers.FLUID_FURNACE_CONTAINER_TYPE;

public class FluidFurnaceContainer extends BaseMachineContainer<FluidFurnaceTileEntity> {

    public FluidFurnaceContainer(int windowId, PlayerInventory inv, PacketBuffer data) {
        this(windowId, new IntArray(3), inv, (FluidFurnaceTileEntity) inv.player.world.getTileEntity(data.readBlockPos()), new FluidFurnaceItemHandler(null));
    }

    public FluidFurnaceContainer(int id, IIntArray data, PlayerInventory playerInventory, @Nullable FluidFurnaceTileEntity tileEntity, FluidFurnaceItemHandler handler) {
        super(FLUID_FURNACE_CONTAINER_TYPE.get(), id, data, playerInventory, tileEntity);

        this.addSlot(new MachineInputSlot(handler, INPUT, 56, 17));
        this.addSlot(new MachineResultSlot(playerInventory.player, tileEntity, handler, OUTPUT, 116, 35));

        addPlayerSlots(playerInventory, 8, 84);
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
