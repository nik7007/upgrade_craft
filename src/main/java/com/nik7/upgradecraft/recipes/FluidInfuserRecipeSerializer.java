package com.nik7.upgradecraft.recipes;

import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class FluidInfuserRecipeSerializer extends ModRecipeSerializer<FluidInfuserRecipe> {
    @Override
    protected FluidInfuserRecipe createRecipe(ResourceLocation recipeId, List<RecipeComponent<?>> recipeComponents) {
        return new FluidInfuserRecipe(recipeId, recipeComponents.toArray(new RecipeComponent[]{}));
    }
}
