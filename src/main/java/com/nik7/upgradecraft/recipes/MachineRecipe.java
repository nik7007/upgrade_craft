package com.nik7.upgradecraft.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

public abstract class MachineRecipe extends RecipeUpgC {
    protected final List<FluidStack> outputFluids = new ArrayList<>();
    protected final List<Float> outputItemChances = new ArrayList<>();
    private final List<Ingredient> inputItems = NonNullList.create();
    private final List<FluidStack> inputFluids = NonNullList.create();
    private final List<Integer> minTicks = NonNullList.create();
    private final List<ItemStack> outputItems = NonNullList.create();
    protected float experience;

    public MachineRecipe(ResourceLocation recipeId,
                         List<Ingredient> inputItems,
                         List<FluidStack> inputFluids,
                         List<Integer> minTicks,
                         List<ItemStack> outputItems,
                         List<FluidStack> outputFluids,
                         List<Float> outputItemChances) {
        super(recipeId);
        if (inputItems != null) {
            this.inputItems.addAll(inputItems);
        }
        if (inputFluids != null) {
            this.inputFluids.addAll(inputFluids);
        }
        if (minTicks != null) {
            this.minTicks.addAll(minTicks);
        }
        if (outputItems != null) {
            this.outputItems.addAll(outputItems);
        }
        if (outputFluids != null) {
            this.outputFluids.addAll(outputFluids);
        }
        if (outputItemChances != null) {
            this.outputItemChances.addAll(outputItemChances);
        }
    }

    public List<Ingredient> getInputItems() {
        return inputItems;
    }

    public List<FluidStack> getInputFluids() {
        return inputFluids;
    }

    public List<ItemStack> getOutputItems() {
        return outputItems;
    }

    public List<FluidStack> getOutputFluids() {
        return outputFluids;
    }

    public List<Float> getOutputItemChances() {
        return outputItemChances;
    }

    public float getExperience() {
        return experience;
    }

    public List<Integer> getMinTicks() {
        return minTicks;
    }
}
