package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.init.RegisterBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RecipeProviderUpgC extends RecipeProvider {
    public RecipeProviderUpgC(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        ShapedRecipeBuilder.shaped(RegisterBlocks.SLIMY_PLANKS_BLOCK.get())
                .define('s', Tags.Items.SLIMEBALLS)
                .define('p', ItemTags.PLANKS)
                .pattern(" s ")
                .pattern("sps")
                .pattern(" s ")
                .unlockedBy("has_slimeballs", has(Tags.Items.SLIMEBALLS))
                .save(consumer);
    }
}
