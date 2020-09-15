package com.nik7.upgradecraft.recipes;

import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class RecipeTypeUpgC<T extends RecipeUpgC> implements IRecipeType<T> {

    private final ResourceLocation registryName;

    public RecipeTypeUpgC(ResourceLocation registryName) {
        this.registryName = registryName;
    }

    public void register() {

        Registry.register(Registry.RECIPE_TYPE, registryName, this);
    }

    @Override
    public String toString() {

        return registryName.toString();
    }
}
