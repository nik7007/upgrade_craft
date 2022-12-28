package com.nik7.upgradecraft.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

public class ModRecipeType<T extends ModRecipe> implements RecipeType<T> {
    private final ResourceLocation registryName;

    public ModRecipeType(ResourceLocation registryName) {
        this.registryName = registryName;
    }

    public ModRecipeType(String modId, String name) {
        this.registryName = new ResourceLocation(modId, name);
    }

    @Override
    public String toString() {
        return registryName.toString();
    }
}
