package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.init.RegisterBlock;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class UpgCRecipeProvider extends RecipeProvider {
    public UpgCRecipeProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void buildCraftingRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        Block slimyBlock = RegisterBlock.SLIMY_PLANKS_BLOCK.get();
        ShapedRecipeBuilder.shaped(slimyBlock)
                .define('s', Tags.Items.SLIMEBALLS)
                .define('p', ItemTags.PLANKS)
                .pattern(" s ")
                .pattern("sps")
                .pattern(" s ")
                .unlockedBy("has_slimeballs", has(Tags.Items.SLIMEBALLS))
                .save(consumer);

        stairBuilder(RegisterBlock.SLIMY_PLANKS_STAIRS_BLOCK.get(), Ingredient.of(slimyBlock))
                .unlockedBy("has_slimy_planks", has(slimyBlock))
                .save(consumer);

        slab(consumer, RegisterBlock.SLIMY_PLANKS_SLAB_BLOCK.get(), slimyBlock);

        fenceBuilder(RegisterBlock.SLIMY_PLANKS_FENCE_BLOCK.get(), Ingredient.of(slimyBlock))
                .unlockedBy("has_slimy_planks", has(slimyBlock))
                .save(consumer);

        fenceGateBuilder(RegisterBlock.SLIMY_PLANKS_FENCE_GATE_BLOCK.get(), Ingredient.of(slimyBlock))
                .unlockedBy("has_slimy_planks", has(slimyBlock))
                .save(consumer);

        doorBuilder(RegisterBlock.SLIMY_PLANKS_DOOR_BLOCK.get(), Ingredient.of(slimyBlock))
                .unlockedBy("has_slimy_planks", has(slimyBlock))
                .save(consumer);
        trapdoorBuilder(RegisterBlock.SLIMY_PLANKS_TRAP_DOOR_BLOCK.get(), Ingredient.of(slimyBlock))
                .unlockedBy("has_slimy_planks", has(slimyBlock))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlock.WOODEN_FLUID_TANK_BLOCK.get())
                .define('p', slimyBlock.asItem())
                .pattern("ppp")
                .pattern("p p")
                .pattern("ppp")
                .unlockedBy("has_slimy_planks", has(slimyBlock))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlock.WOODEN_GLASSED_FLUID_TANK_BLOCK.get())
                .define('p', slimyBlock.asItem())
                .define('g', Tags.Items.GLASS)
                .pattern("ppp")
                .pattern("pgp")
                .pattern("ppp")
                .unlockedBy("has_slimy_planks", has(slimyBlock))
                .save(consumer);

        // Mode Recipe
        ModRecipeProvider.fluidInfuserRecipe(slimyBlock)
                .setInputFluid(new FluidStack(Fluids.LAVA, 50))
                .setDissolveInput(Ingredient.of(Tags.Items.SLIMEBALLS), 2)
                .setInfuseInput(Ingredient.of(ItemTags.PLANKS), 1)
                .setDissolveTime(50)
                .setInfuseTime(100)
                .setOutput(new ItemStack(slimyBlock))
                .setExperience(0.4f)
                .save(consumer);
        ;
    }

}
