package com.nik7.upgradecraft.recipes;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class RecipeUpgC implements IRecipe<Inventory> {
    protected final ResourceLocation recipeId;

    public RecipeUpgC(ResourceLocation recipeId) {
        this.recipeId = recipeId;
    }

    @Override
    public boolean matches(Inventory inv, World worldIn) {
        return true;
    }

    @Override
    public ItemStack getCraftingResult(Inventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return ItemStack.EMPTY;
    }

    @Override
    public ResourceLocation getId() {
        return recipeId;
    }

    @Override
    public abstract IRecipeSerializer<?> getSerializer();

    @Override
    public abstract IRecipeType<?> getType();
}
