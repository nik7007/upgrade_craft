package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.crafting.TagUpgC;
import com.nik7.upgradecraft.init.RegisterBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Blocks;
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

        ShapedRecipeBuilder.shaped(RegisterBlocks.WOODEN_FLUID_TANK_BLOCK.get())
                .define('l', RegisterBlocks.SLIMY_PLANKS_BLOCK.get())
                .pattern("lll")
                .pattern("l l")
                .pattern("lll")
                .unlockedBy("has_slimy_planks_block", has(RegisterBlocks.SLIMY_PLANKS_BLOCK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get())
                .define('l', RegisterBlocks.SLIMY_PLANKS_BLOCK.get())
                .define('g', Tags.Items.GLASS)
                .pattern("lll")
                .pattern("lgl")
                .pattern("lll")
                .unlockedBy("has_slimy_planks_block", has(RegisterBlocks.SLIMY_PLANKS_BLOCK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.FUNNEL_BLOCK.get())
                .define('t', TagUpgC.Items.WOODEN_TANK)
                .define('i', Tags.Items.INGOTS_IRON)
                .pattern("i i")
                .pattern("iti")
                .pattern(" i ")
                .unlockedBy("has_wooden_tank", has(RegisterBlocks.WOODEN_FLUID_TANK_BLOCK.get()))
                .unlockedBy("has_wooden_glassed_tank", has(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get()))
                .save(consumer);

        ShapedRecipeBuilder.shaped(RegisterBlocks.FLUID_FURNACE_BLOCK.get())
                .define('f', Blocks.FURNACE)
                .define('c', Tags.Items.COBBLESTONE)
                .define('t', RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get())
                .define('i', Tags.Items.INGOTS_IRON)
                .pattern("cfc")
                .pattern("ctc")
                .pattern("iii")
                .unlockedBy("has_wooden_glassed_tank", has(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get()))
                .save(consumer);
    }
}
