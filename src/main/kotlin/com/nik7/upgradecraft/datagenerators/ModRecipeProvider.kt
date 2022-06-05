package com.nik7.upgradecraft.datagenerators

import com.nik7.upgradecraft.init.ModBlocks
import net.minecraft.data.DataGenerator
import net.minecraft.data.recipes.FinishedRecipe
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraftforge.common.Tags
import java.util.function.Consumer

class ModRecipeProvider(pGenerator: DataGenerator) : RecipeProvider(pGenerator) {

    override fun buildCraftingRecipes(consumer: Consumer<FinishedRecipe>) {
        ShapedRecipeBuilder.shaped(ModBlocks.SLIMY_PLANKS_BLOCK)
            .define('s', Tags.Items.SLIMEBALLS)
            .define('p', ItemTags.PLANKS)
            .pattern(" s ")
            .pattern("sps")
            .pattern(" s ")
            .unlockedBy("has_slimeballs", has(Tags.Items.SLIMEBALLS))
            .save(consumer)

    }
}
