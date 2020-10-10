package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.blocks.FluidFurnaceBlock;
import com.nik7.upgradecraft.capabilities.FluidFurnaceItemHandler;
import com.nik7.upgradecraft.container.FluidFurnaceContainer;
import com.nik7.upgradecraft.init.Config;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IIntArray;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.nik7.upgradecraft.init.RegisterTileEntity.FLUID_FURNACE_TILE_ENTITY_TYPE;

public class FluidFurnaceTileEntity extends AbstractFluidMachineTileEntity<FluidFurnaceItemHandler> {
    public final static int FLUID_TICK_DURATION;
    private final static int BURN_AMOUNT;

    static {
        int fluidBurnTime = ForgeHooks.getBurnTime(new ItemStack(Items.LAVA_BUCKET));

        float rawBurnAmount = FluidAttributes.BUCKET_VOLUME / (float) fluidBurnTime;
        float rawTickDuration = 1 / rawBurnAmount;


        FLUID_TICK_DURATION = rawTickDuration < 1 ? 1 : (int) (rawTickDuration + 0.5);
        BURN_AMOUNT = rawBurnAmount < 1 ? 1 : (int) (rawBurnAmount + 0.5);
    }

    private FluidStack previousFluidStack = FluidStack.EMPTY;

    private int burnTime = 0;
    private int cookTime = 0;
    private float cookTimeScale = 1;
    private int cookTimeTotal = 0;

    protected final IIntArray fluidFurnaceData = new IIntArray() {
        public int get(int index) {
            switch (index) {
                case 0:
                    return FluidFurnaceTileEntity.this.burnTime;
                case 1:
                    return FluidFurnaceTileEntity.this.cookTime;
                case 2:
                    return FluidFurnaceTileEntity.this.cookTimeTotal;
                default:
                    return 0;
            }
        }

        public void set(int index, int value) {
            switch (index) {
                case 0:
                    FluidFurnaceTileEntity.this.burnTime = value;
                    break;
                case 1:
                    FluidFurnaceTileEntity.this.cookTime = value;
                    break;
                case 2:
                    FluidFurnaceTileEntity.this.cookTimeTotal = value;
            }

        }

        public int size() {
            return 3;
        }
    };

    public FluidFurnaceTileEntity() {
        super(FLUID_FURNACE_TILE_ENTITY_TYPE.get(), Config.TANK_CAPACITY.get() * FluidAttributes.BUCKET_VOLUME);
    }

    @Override
    protected FluidFurnaceItemHandler createInventory() {
        return new FluidFurnaceItemHandler(this::onInventoryChange);
    }

    @Override
    protected boolean isFluidValid(FluidStack fluidStack) {
        return isFluidHot(fluidStack);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);

        burnTime = tag.getInt("burnTime");
        cookTime = tag.getInt("cookTime");
        cookTimeScale = tag.getFloat("cookTimeScale");

        CompoundNBT compoundPreviousFS = tag.getCompound("previousFluidStack");
        previousFluidStack = FluidStack.loadFluidStackFromNBT(compoundPreviousFS);

    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);

        tag.putInt("burnTime", burnTime);
        tag.putInt("cookTime", cookTime);
        tag.putFloat("cookTimeScale", cookTimeScale);

        tag.put("previousFluidStack", previousFluidStack.writeToNBT(new CompoundNBT()));

        return tag;
    }

    protected boolean canSmelt(@Nullable FurnaceRecipe recipeIn) {
        ItemStack outputStack = this.itemStackHandler.getOutputItemStack();
        if (recipeIn != null) {
            ItemStack recipeOutput = recipeIn.getRecipeOutput();
            if (recipeOutput.isEmpty()) {
                return false;
            } else {
                if (outputStack.isEmpty()) {
                    return true;
                } else if (!outputStack.isItemEqual(recipeOutput)) {
                    return false;
                } else if (outputStack.getCount() + recipeOutput.getCount() <= this.itemStackHandler.getSlotLimit(FluidFurnaceItemHandler.OUTPUT) && outputStack.getCount() + recipeOutput.getCount() <= outputStack.getMaxStackSize()) {
                    return true;
                } else {
                    return outputStack.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote || getFluidAmount() == 0) {
            return;
        }
        boolean isBurning = false;
        ItemStack inputItems = itemStackHandler.getInputItemStack();
        FurnaceRecipe furnaceRecipe = this.world.getRecipeManager()
                .getRecipe(IRecipeType.SMELTING, new Inventory(inputItems), this.world)
                .orElse(null);
        if (canSmelt(furnaceRecipe)) {
            assert furnaceRecipe != null;
            isBurning = true;
            if (this.burnTime == 0) {
                this.tank.drain(BURN_AMOUNT, IFluidHandler.FluidAction.EXECUTE);
                this.burnTime = FLUID_TICK_DURATION;
            }
            this.burnTime--;
            cookTimeTotal = (int) (furnaceRecipe.getCookTime() * cookTimeScale + 0.5);
            cookTime++;
            if (cookTime >= cookTimeTotal) {
                cookTime = 0;
                if (inputItems.getCount() == 1) {
                    itemStackHandler.getItemStack().set(FluidFurnaceItemHandler.INPUT, ItemStack.EMPTY);
                } else {
                    inputItems.shrink(1);
                }
                ItemStack outputItems = itemStackHandler.getOutputItemStack();
                ItemStack recipeOutput = furnaceRecipe.getRecipeOutput();
                if (outputItems.isEmpty()) {
                    itemStackHandler.getItemStack().set(FluidFurnaceItemHandler.OUTPUT, recipeOutput.copy());
                } else {
                    outputItems.grow(recipeOutput.getCount());
                }
                setRecipeUsed(furnaceRecipe);
            }
            markDirty();
        } else if (cookTime != 0) {
            cookTime = 0;
            markDirty();
        }

        Boolean lit = getBlockState().get(FluidFurnaceBlock.LIT);
        if (lit != isBurning) {
            world.setBlockState(pos, getBlockState().with(FluidFurnaceBlock.LIT, isBurning),
                    Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.RERENDER_MAIN_THREAD);
        }
    }

    @Override
    @Nonnull
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("block.upgradecraft.fluid_furnace_block");
    }

    @Nullable
    @Override
    public Container createMenu(int id, @Nonnull PlayerInventory playerInventory, @Nonnull PlayerEntity playerEntity) {
        return new FluidFurnaceContainer(id, fluidFurnaceData, playerInventory, this, itemStackHandler);
    }


    @Override
    protected void onFluidChange(Void aVoid) {
        if (this.getFluidAmount() > 0) {
            if (!this.tank.getFluid().isFluidEqual(previousFluidStack)) {
                previousFluidStack = this.getFluid();
                FluidStack tankFluid = this.tank.getFluid();
                int tankFluidTemp = tankFluid.getFluid().getAttributes().getTemperature(tankFluid);
                int lavaTemperature = Fluids.LAVA.getStillFluid().getAttributes().getTemperature();
                cookTimeScale = lavaTemperature / (float) tankFluidTemp;
            }
        } else if (!previousFluidStack.isEmpty()) {
            previousFluidStack = FluidStack.EMPTY;
            cookTimeScale = 1;
        }
        super.onFluidChange(aVoid);
    }
}
