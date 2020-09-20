package com.nik7.upgradecraft.recipes;

import com.nik7.upgradecraft.init.RegisterRecipeSerializer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import static com.nik7.upgradecraft.init.RecipeTypesUpgC.RECIPE_FLUID_INFUSER;

public class FluidInfuserRecipe extends MachineRecipe {


    public FluidInfuserRecipe(ResourceLocation recipeId, List<Ingredient> inputItems, List<FluidStack> inputFluids, List<Integer> minTicks, List<ItemStack> outputItems, List<FluidStack> outputFluids, List<Float> outputItemChances) {
        super(recipeId, inputItems, inputFluids, minTicks, outputItems, outputFluids, outputItemChances);
    }

    public FluidStack getFluidStack() {
        return this.getInputFluids().get(0);
    }

    public Ingredient getDissolve() {
        return this.getInputItems().get(0);
    }

    public Ingredient getInfuse() {
        return this.getInputItems().get(1);
    }

    public ItemStack getResult() {
        return this.getOutputItems().get(0);
    }

    public int getDissolveTick() {
        return this.getMinTicks().get(0);
    }

    public int getInfuseTick() {
        return this.getMinTicks().get(1);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RegisterRecipeSerializer.FLUID_INFUSER_SERIALIZER.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return RECIPE_FLUID_INFUSER;
    }
}
