package com.nik7.upgradecraft.inventory;

import com.nik7.upgradecraft.blockentity.AbstractMachineEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.function.BiConsumer;

public class MachineResultSlot extends SlotItemHandler {

    private final Player player;
    private final AbstractMachineEntity machineEntity;

    private int removeCount = 0;
    @Nullable
    private final BiConsumer<Player, ItemStack> onCrafting;

    public MachineResultSlot(Player player,
                             AbstractMachineEntity machineEntity,
                             ItemStackHandler itemHandler,
                             int index,
                             int xPosition,
                             int yPosition,
                             @Nullable BiConsumer<Player, ItemStack> onCrafting) {
        super(itemHandler, index, xPosition, yPosition);

        this.player = player;
        this.machineEntity = machineEntity;
        this.onCrafting = onCrafting;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack pStack) {
        return false;
    }

    @Override
    public @NotNull ItemStack remove(int pAmount) {
        if (this.hasItem()) {
            this.removeCount += Math.min(pAmount, this.getItem().getCount());
        }

        return super.remove(pAmount);
    }

    @Override
    public void onTake(@NotNull Player pPlayer, @NotNull ItemStack pStack) {
        this.checkTakeAchievements(pStack);
        super.onTake(pPlayer, pStack);
    }

    @Override
    protected void onQuickCraft(@NotNull ItemStack pStack, int pAmount) {
        this.removeCount += pAmount;
        this.checkTakeAchievements(pStack);
    }

    @Override
    protected void checkTakeAchievements(ItemStack pStack) {
        pStack.onCraftedBy(this.player.level, this.player, this.removeCount);
        if (this.player instanceof ServerPlayer serverPlayer && this.container instanceof AbstractMachineEntity machineEntity) {
            machineEntity.awardUsedRecipesAndPopExperience(serverPlayer);
        }

        this.removeCount = 0;
        if(onCrafting!= null){
            onCrafting.accept(player, pStack);
        }
    }

}
