package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.recipes.FluidInfuserRecipe;
import com.nik7.upgradecraft.recipes.RecipeTypeUpgC;
import net.minecraft.util.ResourceLocation;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public final class RecipeTypesUpgC {
    public static final ResourceLocation ID_RECIPE_FLUID_INFUSER = new ResourceLocation(MOD_ID, "fluid_infuser");
    public static final RecipeTypeUpgC<FluidInfuserRecipe> RECIPE_FLUID_INFUSER = new RecipeTypeUpgC<>(ID_RECIPE_FLUID_INFUSER);

    private RecipeTypesUpgC() {
    }

    public static void init() {
        RECIPE_FLUID_INFUSER.register();
    }
}
