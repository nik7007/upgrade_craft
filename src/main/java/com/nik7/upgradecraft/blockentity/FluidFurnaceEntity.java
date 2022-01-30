package com.nik7.upgradecraft.blockentity;

import com.nik7.upgradecraft.block.AbstractMachineBlock;
import com.nik7.upgradecraft.init.Config;
import com.nik7.upgradecraft.init.RegisterBlockEntity;
import com.nik7.upgradecraft.inventory.FluidFurnaceMenu;
import com.nik7.upgradecraft.inventory.ItemStackHandlerUpgC;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class FluidFurnaceEntity extends AbstractMachineEntity {
    private static final Map<ResourceLocation, FuelInfo> FUEL_CACHE = new HashMap<>();
    private static final FuelInfo LAVA_INFO;

    static {
        int fluidBurnTime = ForgeHooks.getBurnTime(new ItemStack(Items.LAVA_BUCKET), RecipeType.BLASTING);
        float rawBurnAmount = FluidAttributes.BUCKET_VOLUME / (float) fluidBurnTime;
        float rawTickDuration = 1 / rawBurnAmount;
        int fluidTickDuration = rawTickDuration < 1 ? 1 : (int) (rawTickDuration + 0.5);
        int burnAmount = rawBurnAmount < 1 ? 1 : (int) (rawBurnAmount + 0.5);
        LAVA_INFO = new FuelInfo(fluidTickDuration, burnAmount, 1);
    }

    private int burnTime = 0;
    private int cookTime = 0;
    private int cookTimeTotal = 0;
    private int burnTimeTotal = 0;

    protected final ContainerData dataAccess = new ContainerData() {
        public int get(int index) {
            return switch (index) {
                case 0 -> FluidFurnaceEntity.this.burnTime;
                case 1 -> FluidFurnaceEntity.this.cookTime;
                case 2 -> FluidFurnaceEntity.this.cookTimeTotal;
                case 3 -> FluidFurnaceEntity.this.burnTimeTotal;
                default -> 0;
            };
        }

        public void set(int index, int value) {
            switch (index) {
                case 0 -> FluidFurnaceEntity.this.burnTime = value;
                case 1 -> FluidFurnaceEntity.this.cookTime = value;
                case 2 -> FluidFurnaceEntity.this.cookTimeTotal = value;
                case 3 -> FluidFurnaceEntity.this.burnTimeTotal = value;
            }

        }

        @Override
        public int getCount() {
            return 4;
        }
    };

    public FluidFurnaceEntity(BlockPos blockPos, BlockState blockState) {
        super(RegisterBlockEntity.FLUID_FURNACE_ENTITY_TYPE.get(), blockPos, blockState,
                Config.TANK_CAPACITY.get() * FluidAttributes.BUCKET_VOLUME);
    }

    @Override
    protected @NotNull ItemStackHandler createInputsHandler() {
        return new ItemStackHandlerUpgC(null, slot -> this.updateContent());
    }

    @Override
    protected @NotNull ItemStackHandler createOutputsHandler() {
        return new ItemStackHandler();
    }

    @Override
    public void load(@NotNull CompoundTag tag) {
        super.load(tag);
        burnTime = tag.getInt("burnTime");
        cookTime = tag.getInt("cookTime");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("burnTime", burnTime);
        tag.putInt("cookTime", cookTime);
    }

    @Override
    protected boolean isFluidValid(FluidStack fluidStack) {
        return isFluidHot(fluidStack);
    }

    private ItemStack getInputs() {
        return inputsHandler.getStackInSlot(0);
    }

    private ItemStack getOutputs() {
        return outputsHandler.getStackInSlot(0);
    }

    @Override
    protected void machineTick() {
        if (this.getLevel() == null) {
            return;
        }
        boolean isBurning = false;
        if (getFluidAmount() > 0) {
            FluidStack fluid = getFluid();
            FuelInfo fuelInfo = getOrCreateFuelInfo(fluid);
            burnTimeTotal = fuelInfo.tickDuration();
            ItemStack input = getInputs();
            if (!input.isEmpty() && getFluidAmount() >= fuelInfo.burnAmount()) {
                SmeltingRecipe recipe = getLevel().getRecipeManager()
                        .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(input), getLevel())
                        .orElse(null);

                if (canSmelt(recipe)) {
                    isBurning = true;
                    if (this.burnTime == 0) {
                        this.tank.drain(fuelInfo.burnAmount(), IFluidHandler.FluidAction.EXECUTE);
                        this.burnTime = fuelInfo.tickDuration();
                    }
                    this.burnTime--;
                    this.cookTimeTotal = (int) Math.floor(recipe.getCookingTime() * fuelInfo.cookTimeScale + 0.5);
                    this.cookTime++;
                    if (cookTime >= cookTimeTotal) {
                        this.cookTime = 0;
                        ItemStack outputs = getOutputs();
                        ItemStack recipeOutput = recipe.getResultItem();
                        if (outputs.isEmpty()) {
                            outputsHandler.setStackInSlot(0, recipeOutput.copy());
                        } else {
                            outputs.grow(recipeOutput.getCount());
                        }
                        input.shrink(1);
                        setRecipeUsed(recipe);
                    }
                } else {
                    cookTime = 0;
                }
            }
        }

        if (this.tickNumber % 5 == 0) {
            BlockState blockState = getBlockState();
            Boolean lit = blockState.getValue(AbstractMachineBlock.LIT);

            if (lit != isBurning) {
                blockState.setValue(AbstractMachineBlock.LIT, lit);
                getLevel().setBlock(getBlockPos(), blockState, Block.UPDATE_ALL);
            }
        }
    }

    private boolean canSmelt(@Nullable SmeltingRecipe recipe) {
        if (recipe == null) {
            return false;
        }
        ItemStack recipeOutput = recipe.getResultItem();
        if (recipeOutput.isEmpty()) {
            return false;
        }

        ItemStack outputs = getOutputs();

        if (outputs.isEmpty()) {
            return true;
        } else if (!outputs.sameItem(recipeOutput)) {
            return false;
        } else if (outputs.getCount() + recipeOutput.getCount() < outputsHandler.getSlotLimit(0) && outputs.getCount() + recipeOutput.getCount() <= outputs.getMaxStackSize()) {
            return true;
        } else {
            return outputs.getCount() + recipeOutput.getCount() <= recipeOutput.getMaxStackSize();
        }
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("block.upgradecraft.fluid_furnace_block");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int windowsIs, Inventory inventory, Player player) {
        return new FluidFurnaceMenu(windowsIs, dataAccess, inventory, this, inputsHandler, outputsHandler);
    }

    private static FuelInfo getOrCreateFuelInfo(FluidStack fluidStack) {
        Fluid fluid = fluidStack.getFluid();
        ResourceLocation name = fluid.delegate.name();
        return FUEL_CACHE.computeIfAbsent(name, resourceLocation -> {

            Item bucket = fluid.getBucket();
            int fluidBurnTime = ForgeHooks.getBurnTime(new ItemStack(bucket), RecipeType.BLASTING);
            float rawBurnAmount = FluidAttributes.BUCKET_VOLUME / (float) fluidBurnTime;
            int fluidTickDuration;
            int burnAmount;
            if (rawBurnAmount == 0) {
                fluidTickDuration = LAVA_INFO.tickDuration();
                burnAmount = LAVA_INFO.burnAmount();
            } else {
                float rawTickDuration = 1 / rawBurnAmount;
                fluidTickDuration = rawTickDuration < 1 ? 1 : (int) (rawTickDuration + 0.5);
                burnAmount = rawBurnAmount < 1 ? 1 : (int) (rawBurnAmount + 0.5);
            }

            float cookTimeScale = Fluids.LAVA.getAttributes().getTemperature() / (float) fluid.getAttributes().getTemperature(fluidStack);
            return new FuelInfo(fluidTickDuration, burnAmount, cookTimeScale);
        });
    }

    private record FuelInfo(int tickDuration, int burnAmount, float cookTimeScale) {
    }

}
