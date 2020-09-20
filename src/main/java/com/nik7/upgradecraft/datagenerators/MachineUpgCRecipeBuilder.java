package com.nik7.upgradecraft.datagenerators;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nik7.upgradecraft.init.RegisterRecipeSerializer;
import com.nik7.upgradecraft.recipes.MachineRecipe;
import com.nik7.upgradecraft.recipes.RecipeSerializerUpgc;
import com.nik7.upgradecraft.utils.RecipeUtils;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class MachineUpgCRecipeBuilder {

    private final ResourceLocation id;
    private final List<InputIngredient> inputIngredients = NonNullList.create();
    private final List<FluidStack> inputFluidStacks = NonNullList.create();
    private final List<Integer> minTimes = NonNullList.create();
    private final List<OutputsItemStack> outputsItemStacks = NonNullList.create();
    private final List<FluidStack> outputFluidStacks = NonNullList.create();
    private final IRecipeSerializer<? extends MachineRecipe> serializer;
    private float experience = 0;

    private MachineUpgCRecipeBuilder(ResourceLocation id,
                                     IRecipeSerializer<? extends MachineRecipe> serializer) {
        this.id = id;
        this.serializer = serializer;
    }

    private static <T extends MachineRecipe> MachineUpgCRecipeBuilder machineRecipe(String name,
                                                                                    RegistryObject<RecipeSerializerUpgc<T>> upgcRegistryObject) {
        String path = upgcRegistryObject.getId().getPath();
        ResourceLocation id = new ResourceLocation(MOD_ID, path + "_" + name);
        return new MachineUpgCRecipeBuilder(id, upgcRegistryObject.get());
    }

    public static MachineUpgCRecipeBuilder fluidInfuserRecipe(String name) {
        return machineRecipe(name, RegisterRecipeSerializer.FLUID_INFUSER_SERIALIZER);
    }

    public MachineUpgCRecipeBuilder ingredient(ITag<Item> tagIn, int count) {
        inputIngredients.add(new InputIngredient(Ingredient.fromTag(tagIn), count));
        return this;
    }

    public MachineUpgCRecipeBuilder ingredient(ITag<Item> tagIn) {
        return ingredient(tagIn, 1);
    }

    public MachineUpgCRecipeBuilder ingredient(ItemStack itemStack) {
        int count = itemStack.getCount();
        inputIngredients.add(new InputIngredient(Ingredient.fromStacks(itemStack), count));
        return this;
    }

    public MachineUpgCRecipeBuilder ingredient(IItemProvider itemIn, int count) {
        return ingredient(new ItemStack(itemIn, count));
    }

    public MachineUpgCRecipeBuilder ingredient(IItemProvider itemIn) {
        return ingredient(itemIn, 1);
    }

    public MachineUpgCRecipeBuilder ingredient(FluidStack fluidStack) {
        this.inputFluidStacks.add(fluidStack);
        return this;
    }

    public MachineUpgCRecipeBuilder phase(int minTime) {
        this.minTimes.add(minTime);
        return this;
    }

    public MachineUpgCRecipeBuilder result(ItemStack itemStack, float chance) {
        this.outputsItemStacks.add(new OutputsItemStack(itemStack, chance));
        return this;
    }

    public MachineUpgCRecipeBuilder result(ItemStack itemStack) {
        return result(itemStack, 1);
    }

    public MachineUpgCRecipeBuilder result(IItemProvider itemIn, int count, float chance) {
        return result(new ItemStack(itemIn, count), chance);
    }

    public MachineUpgCRecipeBuilder result(IItemProvider itemIn, float chance) {
        return result(itemIn, 1, chance);
    }

    public MachineUpgCRecipeBuilder result(IItemProvider itemIn, int count) {
        return result(itemIn, count, 1);
    }

    public MachineUpgCRecipeBuilder result(IItemProvider itemIn) {
        return result(itemIn, 1, 1);
    }

    public MachineUpgCRecipeBuilder result(FluidStack fluidStack) {
        this.outputFluidStacks.add(fluidStack);
        return this;
    }

    public MachineUpgCRecipeBuilder experienceRecipe(float experience) {
        this.experience = experience;
        return this;
    }

    public void build(Consumer<IFinishedRecipe> consumer) {
        consumer.accept(
                new Result(id,
                        inputIngredients,
                        inputFluidStacks,
                        minTimes,
                        outputsItemStacks,
                        outputFluidStacks,
                        experience,
                        serializer)
        );
    }

    private static class Result implements IFinishedRecipe {
        private final ResourceLocation id;
        private final List<InputIngredient> inputIngredients;
        private final List<FluidStack> inputFluidStacks;
        private final List<Integer> minTimes;
        private final List<OutputsItemStack> outputsItemStacks;
        private final List<FluidStack> outputFluidStacks;
        private final float experience;
        private final IRecipeSerializer<? extends MachineRecipe> serializer;

        public Result(ResourceLocation id,
                      List<InputIngredient> inputIngredients,
                      List<FluidStack> inputFluidStacks,
                      List<Integer> minTimes,
                      List<OutputsItemStack> outputsItemStacks,
                      List<FluidStack> outputFluidStacks,
                      float experience, IRecipeSerializer<? extends MachineRecipe> serializer) {
            this.id = id;
            this.inputIngredients = inputIngredients;
            this.inputFluidStacks = inputFluidStacks;
            this.minTimes = minTimes;
            this.outputsItemStacks = outputsItemStacks;
            this.outputFluidStacks = outputFluidStacks;
            this.experience = experience;
            this.serializer = serializer;
        }

        @Override
        public void serialize(@Nonnull JsonObject json) {
            List<JsonElement> inputs = new ArrayList<>();
            for (InputIngredient inputIngredient : inputIngredients) {
                inputs.add(RecipeUtils.serializeIngredient(inputIngredient.ingredient, inputIngredient.amount));
            }
            for (FluidStack inputFluidStack : inputFluidStacks) {
                inputs.add(RecipeUtils.serializeFluidStack(inputFluidStack));
            }

            if (!inputs.isEmpty()) {
                if (inputs.size() == 1) {
                    json.add(RecipeUtils.INGREDIENT, inputs.get(0));
                } else {
                    JsonArray ingredients = new JsonArray();
                    for (JsonElement input : inputs) {
                        ingredients.add(input);
                    }
                    json.add(RecipeUtils.INGREDIENTS, ingredients);
                }
            }

            if (!minTimes.isEmpty()) {
                JsonElement phasesJson = RecipeUtils.serializePhases(minTimes);
                String phasesKey = minTimes.size() > 1 ? RecipeUtils.PHASES : RecipeUtils.PHASE;
                json.add(phasesKey, phasesJson);
            }

            List<JsonElement> outputs = new ArrayList<>();
            for (OutputsItemStack outputsItemStack : outputsItemStacks) {
                outputs.add(RecipeUtils.serializeItemStack(outputsItemStack.itemStack, outputsItemStack.chance));
            }
            for (FluidStack outputFluidStack : outputFluidStacks) {
                outputs.add(RecipeUtils.serializeFluidStack(outputFluidStack));
            }

            if (!outputs.isEmpty()) {
                if (outputs.size() == 1) {
                    json.add(RecipeUtils.RESULT, outputs.get(0));
                } else {
                    JsonArray results = new JsonArray();
                    for (JsonElement output : outputs) {
                        results.add(output);
                    }
                    json.add(RecipeUtils.RESULTS, results);
                }
            }
            json.addProperty(RecipeUtils.EXPERIENCE, experience);

        }

        @Override
        @Nonnull
        public ResourceLocation getID() {
            return this.id;
        }

        @Override
        @Nonnull
        public IRecipeSerializer<?> getSerializer() {
            return serializer;
        }

        @Nullable
        @Override
        public JsonObject getAdvancementJson() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementID() {
            return null;
        }
    }

    private static class InputIngredient {
        private final Ingredient ingredient;
        private final int amount;

        private InputIngredient(Ingredient ingredient, int amount) {
            this.ingredient = ingredient;
            this.amount = amount;
        }
    }

    private static class OutputsItemStack {
        private final ItemStack itemStack;
        private final float chance;

        private OutputsItemStack(ItemStack itemStack, float chance) {
            this.itemStack = itemStack;
            this.chance = chance;
        }
    }
}
