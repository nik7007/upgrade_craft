package com.nik7.upgradecraft.datagenerators;

import com.google.gson.JsonObject;
import com.nik7.upgradecraft.init.RegisterRecipeSerializer;
import com.nik7.upgradecraft.recipes.FluidInfuserRecipe;
import com.nik7.upgradecraft.recipes.ModRecipe;
import com.nik7.upgradecraft.recipes.ModRecipeSerializer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class ModRecipeProvider<T extends ModRecipe> implements RecipeBuilder {
    protected final Item result;
    protected final ModRecipeSerializer<T> serializer;
    protected String group;
    protected final Advancement.Builder advancement = Advancement.Builder.advancement();

    protected ModRecipeProvider(ItemLike result,
                                ModRecipeSerializer<T> serializer) {
        this.result = result.asItem();
        this.serializer = serializer;
    }

    public static FluidInfuserRecipeProvider fluidInfuserRecipe(ItemLike result) {
        return new FluidInfuserRecipeProvider(result);
    }

    @Override
    public RecipeBuilder unlockedBy(String criterion, CriterionTriggerInstance triggerInstance) {
        this.advancement.addCriterion(criterion, triggerInstance);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    public static class FluidInfuserRecipeProvider extends ModRecipeProvider<FluidInfuserRecipe> {
        private FluidStack inputFluid;
        private Ingredient dissolveInput;
        private Ingredient infuseInput;
        private int dissolveTime;
        private int infuseTime;
        private ItemStack output;
        private float experience;

        protected FluidInfuserRecipeProvider(ItemLike result) {
            super(result, RegisterRecipeSerializer.FLUID_INFUSER_RECIPE_SERIALIZER.get());
        }


        public FluidInfuserRecipeProvider setInputFluid(FluidStack inputFluid) {
            this.inputFluid = inputFluid;
            return this;
        }

        public FluidInfuserRecipeProvider setDissolveInput(Ingredient dissolveInput, int count) {
            this.dissolveInput = dissolveInput;
            for (ItemStack item : this.dissolveInput.getItems()) {
                item.setCount(count);
            }
            return this;
        }

        public FluidInfuserRecipeProvider setInfuseInput(Ingredient infuseInput, int count) {
            this.infuseInput = infuseInput;
            for (ItemStack item : this.infuseInput.getItems()) {
                item.setCount(count);
            }
            return this;
        }

        public FluidInfuserRecipeProvider setDissolveTime(int dissolveTime) {
            this.dissolveTime = dissolveTime;
            return this;
        }

        public FluidInfuserRecipeProvider setInfuseTime(int infuseTime) {
            this.infuseTime = infuseTime;
            return this;
        }

        public FluidInfuserRecipeProvider setOutput(ItemStack output) {
            this.output = output;
            return this;
        }

        public FluidInfuserRecipeProvider setExperience(float experience) {
            this.experience = experience;
            return this;
        }

        @Override
        public void save(Consumer<FinishedRecipe> recipeConsumer, ResourceLocation outputId) {
            ResourceLocation recipeId = new ResourceLocation(outputId.getNamespace(), "fluid_infuser_recipe_"+outputId.getPath());
            // TODO: checks
            FluidInfuserRecipe recipe = new FluidInfuserRecipe(
                    outputId,
                    inputFluid,
                    dissolveInput,
                    infuseInput,
                    dissolveTime,
                    infuseTime,
                    output,
                    experience
            );
            this.advancement.parent(ROOT_RECIPE_ADVANCEMENT)
                    .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(outputId))
                    .rewards(AdvancementRewards.Builder.recipe(outputId))
                    .requirements(RequirementsStrategy.OR);
            recipeConsumer.accept(new ModRecipeProvider.Result<>(
                    recipe,
                    serializer,
                    recipeId,
                    advancement,
                    new ResourceLocation(outputId.getNamespace(), "recipes/" + this.result.getItemCategory().getRecipeFolderName() + "/" + outputId.getPath())
            ));
        }
    }

    public static class Result<T extends ModRecipe> implements FinishedRecipe {
        private final T modRecipe;
        private final ModRecipeSerializer<T> serializer;
        private final ResourceLocation id;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(T modRecipe,
                      ModRecipeSerializer<T> serializer,
                      ResourceLocation id,
                      Advancement.Builder advancement,
                      ResourceLocation advancementId) {
            this.modRecipe = modRecipe;
            this.serializer = serializer;
            this.id = id;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject) {
            modRecipe.toJson(jsonObject);
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return serializer;
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return this.advancement.serializeToJson();
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return this.advancementId;
        }
    }
}
