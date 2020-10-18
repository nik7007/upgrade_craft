package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.crafting.TagUpgC;
import com.nik7.upgradecraft.init.RegisterBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.fluid.Fluids;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fluids.FluidStack;

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

        ShapedRecipeBuilder.shapedRecipe(RegisterBlocks.WOODEN_FLUID_TANK_BLOCK.get())
                .key('l', RegisterBlocks.SLIMY_PLANKS_BLOCK.get())
                .patternLine("lll")
                .patternLine("l l")
                .patternLine("lll")
                .addCriterion("has_slimy_planks_block", hasItem(RegisterBlocks.SLIMY_PLANKS_BLOCK.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get())
                .key('l', RegisterBlocks.SLIMY_PLANKS_BLOCK.get())
                .key('g', Tags.Items.GLASS)
                .patternLine("lll")
                .patternLine("lgl")
                .patternLine("lll")
                .addCriterion("has_slimy_planks_block", hasItem(RegisterBlocks.SLIMY_PLANKS_BLOCK.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(RegisterBlocks.FUNNEL_BLOCK.get())
                .key('t', TagUpgC.Items.WOODEN_TANK)
                .key('i', Tags.Items.INGOTS_IRON)
                .patternLine("i i")
                .patternLine("iti")
                .patternLine(" i ")
                .addCriterion("has_wooden_tank", hasItem(RegisterBlocks.WOODEN_FLUID_TANK_BLOCK.get()))
                .addCriterion("has_wooden_glassed_tank", hasItem(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get()))
                .build(consumer);

        MachineUpgCRecipeBuilder.fluidInfuserRecipe("slimy_planks_block")
                .ingredient(new FluidStack(Fluids.LAVA, 10))
                .ingredient(Tags.Items.SLIMEBALLS, 2)
                .ingredient(ItemTags.PLANKS)
                .phase(50)
                .phase(100)
                .result(RegisterBlocks.SLIMY_PLANKS_BLOCK.get())
                .experienceRecipe(0.4f)
                .build(consumer);
    }
}
