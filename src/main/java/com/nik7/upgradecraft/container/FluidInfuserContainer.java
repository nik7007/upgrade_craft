package com.nik7.upgradecraft.container;

import com.nik7.upgradecraft.capabilities.FluidInfuserItemHandler;
import com.nik7.upgradecraft.recipes.FluidInfuserRecipeManager;
import com.nik7.upgradecraft.tileentity.FluidInfuserTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.nik7.upgradecraft.capabilities.FluidInfuserItemHandler.*;
import static com.nik7.upgradecraft.init.RegisterContainers.FLUID_INFUSER_CONTAINER_TYPE;

public class FluidInfuserContainer extends BaseMachineContainer<FluidInfuserTileEntity> {
    private static FluidInfuserRecipeManager FLUID_INFUSER_RECIPE_MANAGER = null;

    public FluidInfuserContainer(int windowId, PlayerInventory inv, PacketBuffer data) {
        this(windowId, new IntArray(4), inv, (FluidInfuserTileEntity) inv.player.world.getTileEntity(data.readBlockPos()), new FluidInfuserItemHandler(null));
    }

    public FluidInfuserContainer(int id, IIntArray data, PlayerInventory playerInventory, @Nullable FluidInfuserTileEntity tileEntity, FluidInfuserItemHandler handler) {
        super(FLUID_INFUSER_CONTAINER_TYPE.get(), id, data, playerInventory, tileEntity);
        this.addSlot(new MachineInputSlot(handler, DISSOLVE, 52, 20));
        this.addSlot(new MachineInputSlot(handler, INFUSE, 82, 20));
        this.addSlot(new MachineResultSlot(playerInventory.player, tileEntity, handler, OUTPUT, 142, 38));
        addPlayerSlots(playerInventory, 8, 84);
        if (FLUID_INFUSER_RECIPE_MANAGER == null) {
            FLUID_INFUSER_RECIPE_MANAGER = FluidInfuserRecipeManager.getManager(playerInventory.player.world);
        }
    }

    public int getDissolveTime() {
        return data.get(0);
    }

    public int getTotalDissolveTime() {
        return data.get(1);
    }

    public int getInfuseTime() {
        return data.get(2);
    }

    public int getTotalInfuseTime() {
        return data.get(3);
    }

    @Override
    @Nonnull
    public ItemStack transferStackInSlot(@Nonnull PlayerEntity player, int slotIndex) {
        if (tileEntity == null) {
            return ItemStack.EMPTY;
        }

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(slotIndex);

        if (slot != null && slot.getHasStack()) {

            ItemStack slotStack = slot.getStack();

            itemstack = slotStack.copy();


            if (slotIndex == OUTPUT) {

                if (!this.mergeItemStack(slotStack, 3, 39, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(slotStack, itemstack);

            } else if (slotIndex != INFUSE && slotIndex != DISSOLVE) {

                final ItemStack toDissolve = this.inventorySlots.get(DISSOLVE).getStack().copy();
                final ItemStack toInfuse = this.inventorySlots.get(INFUSE).getStack().copy();
                final FluidStack fluid = this.tileEntity.getFluid();

                if (FLUID_INFUSER_RECIPE_MANAGER.hasRecipe(fluid, slotStack.getItem(), toInfuse.getItem())) {

                    if (!this.mergeItemStack(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }

                } else if (FLUID_INFUSER_RECIPE_MANAGER.hasRecipe(fluid, toDissolve.getItem(), slotStack.getItem())) {

                    if (!this.mergeItemStack(slotStack, 1, 2, false)) {
                        return ItemStack.EMPTY;
                    }

                } else if (slotIndex < 30) {

                    if (!this.mergeItemStack(slotStack, 30, 39, false)) {
                        return ItemStack.EMPTY;
                    }

                } else if (slotIndex < 39 && !this.mergeItemStack(slotStack, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }


            } else if (!this.mergeItemStack(slotStack, 3, 39, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);

        }

        return itemstack;

    }
}
