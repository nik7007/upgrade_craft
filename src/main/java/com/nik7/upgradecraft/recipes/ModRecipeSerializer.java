package com.nik7.upgradecraft.recipes;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class ModRecipeSerializer<T extends ModRecipe> implements RecipeSerializer<T> {

    protected abstract T createRecipe(ResourceLocation recipeId, List<RecipeComponent<?>> recipeComponents);

    @Override
    public T fromJson(ResourceLocation recipeId, JsonObject jsonObject) {
        List<RecipeComponent<?>> recipeComponents = ModRecipe.fromJson(jsonObject);
        return createRecipe(recipeId, recipeComponents);
    }

    @Override
    public @Nullable T fromNetwork(ResourceLocation recipeId, FriendlyByteBuf friendlyByteBuf) {
        List<RecipeComponent<?>> recipeComponents = ModRecipe.fromNetwork(friendlyByteBuf);
        return createRecipe(recipeId, recipeComponents);
    }

    @Override
    public void toNetwork(FriendlyByteBuf friendlyByteBuf, T modRecipe) {
        modRecipe.toNetwork(friendlyByteBuf);
    }
}
