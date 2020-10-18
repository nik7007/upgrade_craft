package com.nik7.upgradecraft.container;

import com.nik7.upgradecraft.tileentity.AbstractFluidMachineTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class MachineResultSlot extends SlotItemHandler {
    private final PlayerEntity player;
    private final AbstractFluidMachineTileEntity<?> tileEntity;
    private int removeCount = 0;

    @Nullable
    private final BiConsumer<PlayerEntity, ItemStack> onCrafting;

    public MachineResultSlot(PlayerEntity player,
                             AbstractFluidMachineTileEntity<?> tileEntity,
                             IItemHandler itemHandler,
                             int index, int xPosition, int yPosition) {
        this(player, tileEntity, itemHandler, index, xPosition, yPosition, null);
    }

    public MachineResultSlot(PlayerEntity player,
                             AbstractFluidMachineTileEntity<?> tileEntity,
                             IItemHandler itemHandler,
                             int index, int xPosition, int yPosition,
                             @Nullable BiConsumer<PlayerEntity, ItemStack> onCrafting) {
        super(itemHandler, index, xPosition, yPosition);
        this.player = player;
        this.tileEntity = tileEntity;
        this.onCrafting = onCrafting;
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
        if (onCrafting != null) {
            onCrafting.accept(player, stack);
        }
    }
}
