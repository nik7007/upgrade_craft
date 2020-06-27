package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.init.RegisterBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;

public class RecipeProviderUpgC extends RecipeProvider {
    public RecipeProviderUpgC(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        ShapedRecipeBuilder.shapedRecipe(RegisterBlocks.SLIMY_PLANKS_BLOCK.get())
                .key('s', Tags.Items.SLIMEBALLS)
                .key('p', ItemTags.PLANKS)
                .patternLine(" s ")
                .patternLine("sps")
                .patternLine(" s ")
                .addCriterion("has_slimeballs", hasItem(Tags.Items.SLIMEBALLS))
                .build(consumer);
    }
}
