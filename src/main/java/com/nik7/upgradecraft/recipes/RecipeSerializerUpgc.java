package com.nik7.upgradecraft.recipes;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;

public class RecipeSerializerUpgc<T extends MachineRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

    private final Builder<T> builder;

    public RecipeSerializerUpgc(Builder<T> builder) {
        this.builder = builder;
    }

    @Override
    public T read(ResourceLocation recipeId, JsonObject json) {
        return null;
    }

    @Nullable
    @Override
    public T read(ResourceLocation recipeId, PacketBuffer buffer) {
        return null;
    }

    @Override
    public void write(PacketBuffer buffer, T recipe) {

    }

    @FunctionalInterface
    public interface Builder<T> {
        T create(ResourceLocation recipeId,
                 List<Ingredient> inputItems,
                 List<FluidStack> inputFluids,
                 List<Integer> minTicks,
                 List<ItemStack> outputItems,
                 List<FluidStack> outputFluids);
    }
}
