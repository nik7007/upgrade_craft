package com.nik7.upgradecraft.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.fluids.FluidStack;

import static com.nik7.upgradecraft.init.RegisterRecipe.FLUID_INFUSER_RECIPE_TYPE;
import static com.nik7.upgradecraft.init.RegisterRecipeSerializer.FLUID_INFUSER_RECIPE_SERIALIZER;

public class FluidInfuserRecipe extends ModRecipe {
    public FluidInfuserRecipe(ResourceLocation recipeId,
                              FluidStack inputFluid,
                              Ingredient dissolveInput,
                              Ingredient infuseInput,
                              int dissolveTime,
                              int infuseTime,
                              ItemStack output,
                              float experience) {
        super(recipeId,
                new RecipeComponent<>("inputFluid", inputFluid, RecipeComponent.Type.FLUID_STACK),
                new RecipeComponent<>("dissolveInput", dissolveInput, RecipeComponent.Type.INGREDIENT),
                new RecipeComponent<>("infuseInput", infuseInput, RecipeComponent.Type.INGREDIENT),
                new RecipeComponent<>("dissolveTime", dissolveTime, RecipeComponent.Type.INTEGER),
                new RecipeComponent<>("infuseTime", infuseTime, RecipeComponent.Type.INTEGER),
                new RecipeComponent<>("output", output, RecipeComponent.Type.ITEM_STACK),
                new RecipeComponent<>("experience", experience, RecipeComponent.Type.FLOAT)
        );
    }

    public FluidInfuserRecipe(ResourceLocation recipeId, RecipeComponent<?>... components) {
        super(recipeId, components);
    }

    public FluidStack getInputFluid() {
        return getComponent("inputFluid");
    }

    public Ingredient getDissolveInput() {
        return getComponent("dissolveInput");
    }

    public Ingredient getInfuseInput() {
        return getComponent("infuseInput");
    }

    public int getDissolveTime() {
        return getComponent("dissolveTime");
    }

    public int getInfuseTime() {
        return getComponent("infuseTime");
    }

    public ItemStack getOutput() {
        return getComponent("output");
    }

    public float getExperience() {
        return getComponent("experience");
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return FLUID_INFUSER_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return FLUID_INFUSER_RECIPE_TYPE.get();
    }
}
