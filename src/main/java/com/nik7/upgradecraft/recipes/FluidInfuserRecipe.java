package com.nik7.upgradecraft.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;

import static com.nik7.upgradecraft.init.RecipeTypesUpgC.RECIPE_FLUID_INFUSER;

public class FluidInfuserRecipe extends MachineRecipe {
    public FluidInfuserRecipe(ResourceLocation recipeId, List<Ingredient> inputItems, List<FluidStack> inputFluids, List<ItemStack> outputItems, List<FluidStack> outputFluids, List<Float> outputItemChances) {
        super(recipeId, inputItems, inputFluids, outputItems, outputFluids, outputItemChances);
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public IRecipeType<?> getType() {
        return RECIPE_FLUID_INFUSER;
    }
}
