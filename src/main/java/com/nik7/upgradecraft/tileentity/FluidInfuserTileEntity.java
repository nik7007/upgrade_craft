package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.blocks.FluidInfuserBlock;
import com.nik7.upgradecraft.capabilities.FluidInfuserItemHandler;
import com.nik7.upgradecraft.container.FluidInfuserContainer;
import com.nik7.upgradecraft.init.Config;
import com.nik7.upgradecraft.recipes.FluidInfuserRecipe;
import com.nik7.upgradecraft.recipes.FluidInfuserRecipeManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.util.Constants;
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

    protected final IIntArray fluidInfuserData = new IIntArray() {
        public int get(int index) {
            switch (index) {
                case 0:
                    return FluidInfuserTileEntity.this.dissolveTime;
                case 1:
                    return FluidInfuserTileEntity.this.totalDissolveTime;
                case 2:
                    return FluidInfuserTileEntity.this.infuseTime;
                case 3:
                    return FluidInfuserTileEntity.this.totalInfuseTime;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch (index) {
                case 0:
                    FluidInfuserTileEntity.this.dissolveTime = value;
                    break;
                case 1:
                    FluidInfuserTileEntity.this.totalDissolveTime = value;
                    break;
                case 2:
                    FluidInfuserTileEntity.this.infuseTime = value;
                    break;
                case 3:
                    FluidInfuserTileEntity.this.totalInfuseTime = value;
                    break;
            }

        }

        public int size() {
            return 4;
        }
    };

    @Nullable
    private FluidInfuserRecipe currentInfuserRecipe;
    @Nullable
    private FluidInfuserRecipeManager infuserRecipeManager;
    private String currentInfuserRecipeId = null;

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
            currentInfuserRecipeId = tag.getString("currentInfuserRecipe");
            restoreCurrentRecipe();
        }

        CompoundNBT inventory = tag.getCompound("internalInventory");
        internalInventory.deserializeNBT(inventory);

        phase = tag.getInt("phase");

        dissolveTime = tag.getInt("dissolveTime");
        totalDissolveTime = tag.getInt("totalDissolveTime");
        infuseTime = tag.getInt("infuseTime");
        totalInfuseTime = tag.getInt("totalInfuseTime");
    }

    private void restoreCurrentRecipe() {
        if (currentInfuserRecipeId == null || getInfuserRecipeManager() == null) {
            return;
        }
        currentInfuserRecipe = getInfuserRecipeManager().getRecipe(new ResourceLocation(currentInfuserRecipeId));
        currentInfuserRecipeId = null;
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

    @Nullable
    private FluidInfuserRecipeManager getInfuserRecipeManager() {
        if (infuserRecipeManager == null) {
            infuserRecipeManager = FluidInfuserRecipeManager.getManager(world);
        }
        return infuserRecipeManager;
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.upgradecraft.fluid_infuser_block");
    }

    @Nullable
    @Override
    public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerEntity) {
        return new FluidInfuserContainer(id, fluidInfuserData, playerInventory, this, itemStackHandler);
    }

    @Override
    public void tick() {
        if (getWorld() == null || getWorld().isRemote()) {
            return;
        }
        restoreCurrentRecipe();
        boolean isOperating = false;
        if (this.currentInfuserRecipe == null && this.itemStackHandler.hasInputs() && getInfuserRecipeManager() != null) {

            FluidInfuserRecipe infuserRecipe = getInfuserRecipeManager().getRecipe(this.getFluid(),
                    this.itemStackHandler.getDissolveItemStack().getItem(),
                    this.itemStackHandler.getInfuseItemStack().getItem());

            if (canOperate(infuserRecipe)) {
                startOperation(infuserRecipe);
            }
        }
        if (this.currentInfuserRecipe != null) {
            operate();
            isOperating = true;
        }
        Boolean lit = getBlockState().get(FluidInfuserBlock.LIT);
        if (lit != isOperating) {
            getWorld().setBlockState(pos, getBlockState().with(FluidInfuserBlock.LIT, isOperating),
                    Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.RERENDER_MAIN_THREAD);
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
                break;
            case 3:
                ItemStack outputItems = this.itemStackHandler.getItemStack().get(OUTPUT);
                ItemStack recipeOutput = currentInfuserRecipe.getResult().copy();

                if (outputItems.isEmpty()) {
                    itemStackHandler.getItemStack().set(OUTPUT, recipeOutput.copy());
                } else {
                    outputItems.grow(recipeOutput.getCount());
                }

                setRecipeUsed(currentInfuserRecipe);

                currentInfuserRecipe = null;
                dissolveTime = 0;
                infuseTime = 0;
                phase = 0;

                break;
        }
        markDirty();
    }

    private void startOperation(FluidInfuserRecipe infuserRecipe) {
        this.currentInfuserRecipe = infuserRecipe;
        totalDissolveTime = infuserRecipe.getDissolveTick();
        totalInfuseTime = infuserRecipe.getInfuseTick();

        if (internalIsEmpty()) {
            this.tank.drain(infuserRecipe.getFluidStack(), IFluidHandler.FluidAction.EXECUTE);

            int itemToDissolve = getItemNumber(infuserRecipe.getDissolve());
            ItemStack dissolveItemStack = itemStackHandler.getDissolveItemStack();
            moveItemInternally(itemToDissolve, DISSOLVE, 0, dissolveItemStack);

            int itemToInfuse = getItemNumber(infuserRecipe.getInfuse());
            ItemStack InfuseItemStack = itemStackHandler.getInfuseItemStack();
            moveItemInternally(itemToInfuse, INFUSE, 1, InfuseItemStack);
            phase = 1;
        }
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

    public boolean internalIsEmpty() {
        return internalInventory.getStackInSlot(0).isEmpty() &&
                internalInventory.getStackInSlot(1).isEmpty();
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
                    && outputItemStack.getMaxStackSize() >= outputItemStack.getCount() + result.getCount();
        }
    }
}
