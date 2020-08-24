package com.nik7.upgradecraft.tileentity;

import com.nik7.upgradecraft.blocks.FluidFurnaceBlock;
import com.nik7.upgradecraft.capabilities.FluidFurnaceItemHandler;
import com.nik7.upgradecraft.fluids.tanks.EventFluidTank;
import com.nik7.upgradecraft.init.Config;
import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.nik7.upgradecraft.init.RegisterTileEntity.FLUID_FURNACE_TILE_ENTITY_TYPE;

public class FluidFurnaceTileEntity extends BaseFluidHandlerTileEntity implements ITickableTileEntity {
    private final static int FLUID_TICK_DURATION;
    private final static int BURN_AMOUNT;

    static {
        int fluidBurnTime = ForgeHooks.getBurnTime(new ItemStack(Items.LAVA_BUCKET));

        float rawBurnAmount = FluidAttributes.BUCKET_VOLUME / (float) fluidBurnTime;
        float rawTickDuration = 1 / rawBurnAmount;


        FLUID_TICK_DURATION = rawTickDuration < 1 ? 1 : (int) (rawTickDuration + 0.5);
        BURN_AMOUNT = rawBurnAmount < 1 ? 1 : (int) (rawBurnAmount + 0.5);
    }

    private final LazyOptional<ItemStackHandler> inventory;
    private final FluidFurnaceItemHandler furnaceItemHandler;

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
        super(FLUID_FURNACE_TILE_ENTITY_TYPE.get());
        this.tank = new EventFluidTank(Config.TANK_CAPACITY.get() * FluidAttributes.BUCKET_VOLUME,
                this::onFluidChange,
                this::isFluidHot);
        this.furnaceItemHandler = new FluidFurnaceItemHandler(slot -> {
            this.markDirty();
            if (world != null && !world.isRemote()) {
                world.updateComparatorOutputLevel(pos, getBlockState().getBlock());
            }
        });
        this.inventory = LazyOptional.of(() -> furnaceItemHandler);
    }

    @Override
    public void read(BlockState state, CompoundNBT tag) {
        super.read(state, tag);
        CompoundNBT inventory = tag.getCompound("inventory");

        burnTime = tag.getInt("burnTime");
        cookTime = tag.getInt("cookTime");
        cookTimeScale = tag.getFloat("cookTimeScale");

        furnaceItemHandler.deserializeNBT(inventory);

        CompoundNBT compoundPreviousFS = tag.getCompound("previousFluidStack");
        previousFluidStack = FluidStack.loadFluidStackFromNBT(compoundPreviousFS);
    }

    @Override
    public CompoundNBT write(CompoundNBT tag) {
        tag = super.write(tag);
        CompoundNBT inventory = furnaceItemHandler.serializeNBT();
        tag.put("inventory", inventory);

        tag.putInt("burnTime", burnTime);
        tag.putInt("cookTime", cookTime);
        tag.putFloat("cookTimeScale", cookTimeScale);

        tag.put("previousFluidStack", previousFluidStack.writeToNBT(new CompoundNBT()));
        return tag;
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote || getFluidAmount() == 0) {
            return;
        }
        boolean isBurning = false;
        ItemStack inputItems = furnaceItemHandler.getInputItemStack();
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
                    furnaceItemHandler.getItemStack().set(FluidFurnaceItemHandler.INPUT, ItemStack.EMPTY);
                } else {
                    inputItems.shrink(1);
                }
                ItemStack outputItems = furnaceItemHandler.getOutputItemStack();
                ItemStack recipeOutput = furnaceRecipe.getRecipeOutput();
                if (outputItems.isEmpty()) {
                    furnaceItemHandler.getItemStack().set(FluidFurnaceItemHandler.OUTPUT, recipeOutput.copy());
                } else {
                    outputItems.grow(recipeOutput.getCount());
                }
            }
            markDirty();
        }
        Boolean lit = getBlockState().get(FluidFurnaceBlock.LIT);
        if (lit != isBurning) {
            world.setBlockState(pos, getBlockState().with(FluidFurnaceBlock.LIT, isBurning),
                    Constants.BlockFlags.BLOCK_UPDATE + Constants.BlockFlags.NOTIFY_NEIGHBORS + Constants.BlockFlags.RERENDER_MAIN_THREAD);
        }
    }

    protected boolean canSmelt(@Nullable FurnaceRecipe recipeIn) {
        ItemStack outputStack = this.furnaceItemHandler.getOutputItemStack();
        if (recipeIn != null) {
            ItemStack recipeOutput = recipeIn.getRecipeOutput();
            if (recipeOutput.isEmpty()) {
                return false;
            } else {
                if (outputStack.isEmpty()) {
                    return true;
                } else if (!outputStack.isItemEqual(recipeOutput)) {
                    return false;
                } else if (outputStack.getCount() + recipeOutput.getCount() <= this.furnaceItemHandler.getSlotLimit(FluidFurnaceItemHandler.OUTPUT) && outputStack.getCount() + recipeOutput.getCount() <= outputStack.getMaxStackSize()) {
                    return true;
                } else {
                    return outputStack.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxStackSize();
                }
            }
        } else {
            return false;
        }
    }

    private void onFluidChange(Void aVoid) {
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
        notifyBlockUpdate();
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return inventory.cast();
        return super.getCapability(capability, facing);
    }
}
