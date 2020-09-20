package com.nik7.upgradecraft.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nik7.upgradecraft.utils.RecipeUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RecipeSerializerUpgc<T extends MachineRecipe> extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<T> {

    private final Builder<T> builder;

    public RecipeSerializerUpgc(Builder<T> builder) {
        this.builder = builder;
    }

    @Override
    @Nonnull
    public T read(@Nonnull ResourceLocation recipeId, JsonObject json) {
        List<Ingredient> inputItems = new ArrayList<>();
        List<FluidStack> inputFluids = new ArrayList<>();
        List<Integer> minTicks = new ArrayList<>();
        List<ItemStack> outputItems = new ArrayList<>();
        List<FluidStack> outputFluids = new ArrayList<>();
        List<Float> outputItemChances = new ArrayList<>();


        JsonElement ingredients = null;
        if (json.has(RecipeUtils.INGREDIENT)) {
            ingredients = json.get(RecipeUtils.INGREDIENT);
        }
        if (json.has(RecipeUtils.INGREDIENTS)) {
            ingredients = json.get(RecipeUtils.INGREDIENTS);
        }
        if (ingredients != null) {
            RecipeUtils.parseIngredients(ingredients, inputItems, inputFluids);
        }
        JsonElement timePhases = null;
        if (json.has(RecipeUtils.PHASE)) {
            timePhases = json.get(RecipeUtils.PHASE);
        }
        if (json.has(RecipeUtils.PHASES)) {
            timePhases = json.get(RecipeUtils.PHASES);
        }
        if (timePhases != null) {
            RecipeUtils.parsePhases(timePhases, minTicks);
        }

        JsonElement results = null;

        if (json.has(RecipeUtils.RESULT)) {
            results = json.get(RecipeUtils.RESULT);
        }
        if (json.has(RecipeUtils.RESULTS)) {
            results = json.get(RecipeUtils.RESULTS);
        }
        if (results != null) {
            RecipeUtils.parseResults(results, outputItems, outputFluids, outputItemChances);
        }

        T recipe = builder.create(recipeId, inputItems, inputFluids, minTicks, outputItems, outputFluids, outputItemChances);

        if (json.has(RecipeUtils.EXPERIENCE)) {
            recipe.experience = json.get(RecipeUtils.EXPERIENCE).getAsFloat();
        }


        return recipe;
    }

    @Nullable
    @Override
    public T read(@Nonnull ResourceLocation recipeId, PacketBuffer buffer) {
        float experience = buffer.readFloat();

        int numInputItems = buffer.readVarInt();
        List<Ingredient> inputItems = new ArrayList<>(numInputItems);
        for (int i = 0; i < numInputItems; ++i) {
            inputItems.add(Ingredient.read(buffer));
        }

        int numInputFluids = buffer.readVarInt();
        List<FluidStack> inputFluids = new ArrayList<>(numInputFluids);
        for (int i = 0; i < numInputFluids; ++i) {
            inputFluids.add(buffer.readFluidStack());
        }

        int numMinTicks = buffer.readInt();
        List<Integer> minTicks = new ArrayList<>();
        for (int i = 0; i < numMinTicks; i++) {
            minTicks.add(buffer.readInt());
        }

        int numOutputItems = buffer.readVarInt();
        List<ItemStack> outputItems = new ArrayList<>(numOutputItems);
        List<Float> outputItemChances = new ArrayList<>(numOutputItems);
        for (int i = 0; i < numOutputItems; ++i) {
            outputItems.add(buffer.readItemStack());
            outputItemChances.add(buffer.readFloat());
        }

        int numOutputFluids = buffer.readVarInt();
        List<FluidStack> outputFluids = new ArrayList<>(numOutputFluids);
        for (int i = 0; i < numOutputFluids; ++i) {
            outputFluids.add(buffer.readFluidStack());
        }

        T recipe = builder.create(recipeId, inputItems, inputFluids, minTicks, outputItems, outputFluids, outputItemChances);
        recipe.experience = experience;

        return recipe;
    }

    @Override
    public void write(PacketBuffer buffer, T recipe) {
        buffer.writeFloat(recipe.getExperience());
        List<Ingredient> inputItems = recipe.getInputItems();
        buffer.writeInt(inputItems.size());
        for (Ingredient inputItem : inputItems) {
            inputItem.write(buffer);
        }

        List<FluidStack> inputFluids = recipe.getInputFluids();
        buffer.writeInt(inputFluids.size());
        for (FluidStack inputFluid : inputFluids) {
            buffer.writeFluidStack(inputFluid);
        }

        List<Integer> minTicks = recipe.getMinTicks();
        buffer.writeInt(minTicks.size());
        for (Integer minTick : minTicks) {
            buffer.writeInt(minTick);
        }

        List<ItemStack> outputItems = recipe.getOutputItems();
        int numOutItem = outputItems.size();
        buffer.writeInt(numOutItem);
        for (int i = 0; i < numOutItem; i++) {
            buffer.writeItemStack(outputItems.get(i));
            buffer.writeFloat(recipe.getOutputItemChances().get(i));
        }

        List<FluidStack> outputFluids = recipe.getOutputFluids();
        buffer.writeInt(outputFluids.size());
        for (FluidStack outputFluid : outputFluids) {
            buffer.writeFluidStack(outputFluid);
        }

    }

    @FunctionalInterface
    public interface Builder<T> {
        T create(ResourceLocation recipeId,
                 List<Ingredient> inputItems,
                 List<FluidStack> inputFluids,
                 List<Integer> minTicks,
                 List<ItemStack> outputItems,
                 List<FluidStack> outputFluids,
                 List<Float> outputItemChances);
    }
}
