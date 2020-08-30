package com.nik7.upgradecraft.container;

import com.nik7.upgradecraft.tileentity.FluidFurnaceTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.hooks.BasicEventHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class FluidFurnaceResultSlot extends SlotItemHandler {
    private final PlayerEntity player;
    private final FluidFurnaceTileEntity tileEntity;
    private int removeCount = 0;

    public FluidFurnaceResultSlot(PlayerEntity player, FluidFurnaceTileEntity tileEntity, IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.player = player;
        this.tileEntity = tileEntity;
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if (this.getHasStack()) {
            this.removeCount += Math.min(amount, this.getStack().getCount());
        }

        return super.decrStackSize(amount);
    }

    @Override
    protected void onCrafting(ItemStack stack, int amount) {
        this.removeCount += amount;
        this.onCrafting(stack);
    }

    @Override
    protected void onCrafting(ItemStack stack) {
        stack.onCrafting(this.player.world, this.player, this.removeCount);
        if (!this.player.world.isRemote && tileEntity != null) {
            tileEntity.unlockRecipes(this.player);
        }

        this.removeCount = 0;
        BasicEventHooks.firePlayerSmeltedEvent(this.player, stack);
    }
}
