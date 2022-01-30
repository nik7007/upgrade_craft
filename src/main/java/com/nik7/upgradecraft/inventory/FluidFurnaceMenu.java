package com.nik7.upgradecraft.inventory;

import com.nik7.upgradecraft.blockentity.FluidFurnaceEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static com.nik7.upgradecraft.init.RegisterContainerMenu.FLUID_FURNACE_CONTAINER_TYPE;

public class FluidFurnaceMenu extends BaseMachineContainerMenu<FluidFurnaceEntity> {
    private final static int INPUT = 0;
    private final static int OUTPUT = 1;

    public FluidFurnaceMenu(int pContainerId, Inventory playerInventory, FriendlyByteBuf data) {
        this(pContainerId, new SimpleContainerData(4), playerInventory, (FluidFurnaceEntity) playerInventory.player.level.getBlockEntity(data.readBlockPos()), new ItemStackHandler(), new ItemStackHandler());
    }

    public FluidFurnaceMenu(int pContainerId, ContainerData dataAccess, Inventory playerInventory, @Nullable FluidFurnaceEntity blockEntity, ItemStackHandler inputsHandler, ItemStackHandler outputsHandler) {
        super(FLUID_FURNACE_CONTAINER_TYPE.get(), pContainerId, dataAccess, playerInventory, blockEntity);
        this.addSlot(new SlotItemHandler(inputsHandler, 0, 56, 17));
        this.addSlot(new MachineResultSlot(playerInventory.player, blockEntity, outputsHandler, 0, 116, 35, ForgeEventFactory::firePlayerSmeltedEvent));
        addPlayerSlots(playerInventory, 8, 84);
    }

    public int getBurnTime() {
        return this.dataAccess.get(0);
    }

    public int getCookTime() {
        return this.dataAccess.get(1);
    }

    public int getCookTimeTotal() {
        return this.dataAccess.get(2);
    }

    public int getBurnTimeTotal() {
        return this.dataAccess.get(3);
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotIndex) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();

            if (slotIndex == OUTPUT) {
                if (!this.moveItemStackTo(slotStack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, itemStack);
            } else if (slotIndex != INPUT) {
                if (hasRecipe(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex < 29) {
                    if (!this.moveItemStackTo(slotStack, 29, 38, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (slotIndex < 38 && !this.moveItemStackTo(slotStack, 2, 29, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 2, 38, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, slotStack);
        }

        return itemStack;
    }

    private boolean hasRecipe(ItemStack stack) {
        Level level = playerInventory.player.getLevel();
        return level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(stack), level).isPresent();
    }
}
