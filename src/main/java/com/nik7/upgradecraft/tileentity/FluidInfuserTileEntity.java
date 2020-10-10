package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.capabilities.FluidInfuserItemHandler;
import com.nik7.upgradecraft.init.Config;
import com.nik7.upgradecraft.recipes.FluidInfuserRecipe;
import com.nik7.upgradecraft.recipes.FluidInfuserRecipeManager;
import com.sun.istack.internal.NotNull;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.nik7.upgradecraft.capabilities.FluidInfuserItemHandler.*;
import static com.nik7.upgradecraft.init.RegisterTileEntity.FLUID_INFUSER_TILE_ENTITY_TYPE;

public class FluidInfuserTileEntity extends AbstractFluidMachineTileEntity<FluidInfuserItemHandler> {

    private final ItemStackHandler internalInventory = new ItemStackHandler(2);
    private int phase = 0;

    private int dissolveTime = 0;
    private int totalDissolveTime = 0;
    private int infuseTime = 0;
    private int totalInfuseTime = 0;

    @Nullable
    private FluidInfuserRecipe currentInfuserRecipe;
    @Nullable
    private FluidInfuserRecipeManager infuserRecipeManager;

    public FluidInfuserTileEntity() {
        super(FLUID_INFUSER_TILE_ENTITY_TYPE.get(), Config.TANK_CAPACITY.get() * FluidAttributes.BUCKET_VOLUME);
    }

    @Override
    protected FluidInfuserItemHandler createInventory() {
        return new FluidInfuserItemHandler(this::onInventoryChange);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        if (tag.contains("currentInfuserRecipe")) {
            String currentInfuserRecipeId = tag.getString("currentInfuserRecipe");
            currentInfuserRecipe = getInfuserRecipeManager().getRecipe(new ResourceLocation(currentInfuserRecipeId));
        }

        CompoundNBT inventory = tag.getCompound("internalInventory");
        internalInventory.deserializeNBT(inventory);

        phase = tag.getInt("phase");

        dissolveTime = tag.getInt("dissolveTime");
        totalDissolveTime = tag.getInt("totalDissolveTime");
        infuseTime = tag.getInt("infuseTime");
        totalInfuseTime = tag.getInt("totalInfuseTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        if (currentInfuserRecipe != null) {
            tag.putString("currentInfuserRecipe", currentInfuserRecipe.getId().toString());
        }

        CompoundNBT inventory = internalInventory.serializeNBT();
        tag.put("internalInventory", inventory);

        tag.putInt("phase", phase);

        tag.putInt("dissolveTime", dissolveTime);
        tag.putInt("totalDissolveTime", totalDissolveTime);
        tag.putInt("infuseTime", infuseTime);
        tag.putInt("totalInfuseTime", totalInfuseTime);
        return tag;
    }

    @NotNull
    private FluidInfuserRecipeManager getInfuserRecipeManager() {
        if (infuserRecipeManager == null) {
            infuserRecipeManager = FluidInfuserRecipeManager.getManager(world);
        }
        return infuserRecipeManager;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("lock.upgradecraft.fluid_infuser_block");
    }

    @Nullable
    @Override
    public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerEntity) {
        return null;
    }

    @Override
    public void tick() {
        if (getWorld() == null || getWorld().isRemote()) {
            return;
        }
        if (this.currentInfuserRecipe == null) {

            FluidInfuserRecipe infuserRecipe = getInfuserRecipeManager().getRecipe(this.getFluid(),
                    this.itemStackHandler.getDissolveItemStack().getItem(),
                    this.itemStackHandler.getInfuseItemStack().getItem());

            if (canOperate(infuserRecipe)) {
                startOperation(infuserRecipe);
            }
        }
        if (this.currentInfuserRecipe != null) {
            operate();
        }
    }

    private void operate() {
        if (currentInfuserRecipe == null) {
            return;
        }
        switch (phase) {
            case 1:
                if (dissolveTime < totalDissolveTime) {
                    dissolveTime++;
                } else {
                    internalInventory.setStackInSlot(0, ItemStack.EMPTY);
                    phase = 2;
                }
                break;
            case 2:
                if (infuseTime < totalInfuseTime) {
                    infuseTime++;
                } else {
                    internalInventory.setStackInSlot(1, ItemStack.EMPTY);
                    phase = 3;
                }
            case 3:
                ItemStack outputItems = this.itemStackHandler.getItemStack().get(OUTPUT);
                ItemStack recipeOutput = currentInfuserRecipe.getRecipeOutput().copy();

                if (outputItems.isEmpty()) {
                    itemStackHandler.getItemStack().set(OUTPUT, recipeOutput.copy());
                } else {
                    outputItems.grow(recipeOutput.getCount());
                }

                setRecipeUsed(currentInfuserRecipe);
                currentInfuserRecipe = null;
                phase = 0;
                break;
        }
        markDirty();
    }

    private void startOperation(FluidInfuserRecipe infuserRecipe) {
        this.currentInfuserRecipe = infuserRecipe;
        dissolveTime = 0;
        infuseTime = 0;
        totalDissolveTime = infuserRecipe.getDissolveTick();
        totalInfuseTime = infuserRecipe.getInfuseTick();

        this.tank.drain(infuserRecipe.getFluidStack(), IFluidHandler.FluidAction.EXECUTE);

        int itemToDissolve = getItemNumber(infuserRecipe.getDissolve());
        ItemStack dissolveItemStack = itemStackHandler.getDissolveItemStack();
        moveItemInternally(itemToDissolve, DISSOLVE, 0, dissolveItemStack);

        int itemToInfuse = getItemNumber(infuserRecipe.getInfuse());
        ItemStack InfuseItemStack = itemStackHandler.getInfuseItemStack();
        moveItemInternally(itemToInfuse, INFUSE, 1, InfuseItemStack);
        phase = 1;
    }

    private void moveItemInternally(int itemToMove,
                                    int sourceSlot,
                                    int internalSlot,
                                    ItemStack originalItemStack) {
        ItemStack internalItemStack = originalItemStack.copy();
        internalItemStack.setCount(itemToMove);
        originalItemStack.shrink(itemToMove);
        if (originalItemStack.isEmpty()) {
            itemStackHandler.setStackInSlot(sourceSlot, ItemStack.EMPTY);
        }
        internalInventory.setStackInSlot(internalSlot, internalItemStack);
    }

    private int getItemNumber(Ingredient dissolve) {
        return dissolve.getMatchingStacks()[0].getCount();
    }

    private boolean canOperate(FluidInfuserRecipe infuserRecipe) {
        if (infuserRecipe == null) {
            return false;
        }
        return infuserRecipe.getFluidStack().isFluidEqual(this.getFluid()) &&

                infuserRecipe.getDissolve().test(this.itemStackHandler.getDissolveItemStack()) &&
                getItemNumber(infuserRecipe.getDissolve()) <= this.itemStackHandler.getDissolveItemStack().getCount() &&

                infuserRecipe.getInfuse().test(this.itemStackHandler.getInfuseItemStack()) &&
                getItemNumber(infuserRecipe.getInfuse()) <= this.itemStackHandler.getInfuseItemStack().getCount() &&

                checkOutput(infuserRecipe);
    }

    private boolean checkOutput(FluidInfuserRecipe infuserRecipe) {
        ItemStack outputItemStack = this.itemStackHandler.getOutputItemStack();
        if (outputItemStack == ItemStack.EMPTY) {
            return true;
        } else {
            ItemStack result = infuserRecipe.getResult().copy();
            return result.isItemEqual(outputItemStack)
                    && outputItemStack.getMaxStackSize() > outputItemStack.getCount() + result.getCount();
        }
    }
}
