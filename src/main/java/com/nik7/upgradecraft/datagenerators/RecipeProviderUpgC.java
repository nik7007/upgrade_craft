package com.nik7.upgradecraft.datagenerators;

import com.nik7.upgradecraft.crafting.TagUpgC;
import com.nik7.upgradecraft.init.RegisterBlocks;
import com.nik7.upgradecraft.init.RegisterItems;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Items;
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

        ShapedRecipeBuilder.shapedRecipe(RegisterBlocks.FLUID_FURNACE_BLOCK.get())
                .key('f', Blocks.FURNACE)
                .key('c', Tags.Items.COBBLESTONE)
                .key('t', RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get())
                .key('i', Tags.Items.INGOTS_IRON)
                .patternLine("cfc")
                .patternLine("ctc")
                .patternLine("iii")
                .addCriterion("has_wooden_glassed_tank", hasItem(RegisterBlocks.WOODEN_FLUID_TANK_GLASSED_BLOCK.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(RegisterBlocks.FLUID_INFUSER_BLOCK.get())
                .key('f', RegisterBlocks.FLUID_FURNACE_BLOCK.get())
                .key('s', Tags.Items.STONE)
                .key('i', Tags.Items.INGOTS_IRON)
                .patternLine("sfs")
                .patternLine("s s")
                .patternLine("iii")
                .addCriterion("has_wooden_glassed_tank", hasItem(RegisterBlocks.FLUID_FURNACE_BLOCK.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(RegisterBlocks.CLAY_FLUID_TANK_BLOCK.get())
                .key('c', RegisterItems.CLAY_INGOT_ITEM.get())
                .patternLine("ccc")
                .patternLine("c c")
                .patternLine("ccc")
                .addCriterion("has_clay_ingot_item", hasItem(RegisterItems.CLAY_INGOT_ITEM.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(RegisterBlocks.CLAY_FLUID_TANK_GLASSED_BLOCK.get())
                .key('c', RegisterItems.CLAY_INGOT_ITEM.get())
                .key('g', Tags.Items.GLASS)
                .patternLine("ccc")
                .patternLine("cgc")
                .patternLine("ccc")
                .addCriterion("has_clay_ingot_item", hasItem(RegisterItems.CLAY_INGOT_ITEM.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(RegisterBlocks.CLAY_BRICKS_BLOCK.get())
                .key('i', RegisterItems.CLAY_INGOT_ITEM.get())
                .patternLine("ii")
                .patternLine("ii")
                .addCriterion("has_clay_ingot_item", hasItem(RegisterItems.CLAY_INGOT_ITEM.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(RegisterBlocks.CLAY_BRICKS_SLAB_BLOCK.get(), 6)
                .key('b', RegisterBlocks.CLAY_BRICKS_BLOCK.get())
                .patternLine("bbb")
                .addCriterion("has_clay_ingot_item", hasItem(RegisterBlocks.CLAY_BRICKS_BLOCK.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(RegisterBlocks.CLAY_BRICKS_WALL_BLOCK.get(), 6)
                .key('b', RegisterBlocks.CLAY_BRICKS_BLOCK.get())
                .patternLine("bbb")
                .patternLine("bbb")
                .addCriterion("has_clay_ingot_item", hasItem(RegisterBlocks.CLAY_BRICKS_BLOCK.get()))
                .build(consumer);

        ShapedRecipeBuilder.shapedRecipe(RegisterBlocks.CLAY_BRICKS_STAIRS_BLOCK.get(), 4)
                .key('b', RegisterBlocks.CLAY_BRICKS_BLOCK.get())
                .patternLine("b  ")
                .patternLine("bb ")
                .patternLine("bbb")
                .addCriterion("has_clay_ingot_item", hasItem(RegisterBlocks.CLAY_BRICKS_BLOCK.get()))
                .build(consumer);

        // fluid infuser recipes
        MachineUpgCRecipeBuilder.fluidInfuserRecipe("slimy_planks_block")
                .ingredient(new FluidStack(Fluids.LAVA, 50)) // input fluid
                .ingredient(Tags.Items.SLIMEBALLS, 2) // input dissolve
                .ingredient(ItemTags.PLANKS) // input infuse
                .phase(50) // dissolve ticks
                .phase(100) // infuse tickes
                .result(RegisterBlocks.SLIMY_PLANKS_BLOCK.get())
                .experienceRecipe(0.4f)
                .build(consumer);

        MachineUpgCRecipeBuilder.fluidInfuserRecipe("gilded_blackstone_block")
                .ingredient(new FluidStack(Fluids.LAVA, 300)) // input fluid
                .ingredient(Tags.Items.INGOTS_GOLD, 1) // input dissolve
                .ingredient(Blocks.BLACKSTONE) // input infuse
                .phase(100) // dissolve ticks
                .phase(200) // infuse tickes
                .result(Blocks.GILDED_BLACKSTONE)
                .experienceRecipe(0.4f)
                .build(consumer);

        MachineUpgCRecipeBuilder.fluidInfuserRecipe("clay_ingot_item")
                .ingredient(new FluidStack(Fluids.WATER, 250))  // input fluid
                .ingredient(Items.CLAY_BALL)  // input dissolve
                .ingredient(Items.BRICK)  // input infuse
                .phase(50)  // dissolve ticks
                .phase(100)  // infuse tickes
                .result(RegisterItems.CLAY_INGOT_ITEM.get())
                .experienceRecipe(0.6f)
                .build(consumer);

        MachineUpgCRecipeBuilder.fluidInfuserRecipe("mossy_cobblestone")
                .ingredient(new FluidStack(Fluids.WATER, 150))  // input fluid
                .ingredient(Blocks.GRASS, 4)  // input dissolve
                .ingredient(Blocks.COBBLESTONE)  // input infuse
                .phase(150)  // dissolve ticks
                .phase(100)  // infuse tickes
                .result(Blocks.MOSSY_COBBLESTONE)
                .experienceRecipe(0.2f)
                .build(consumer);

        MachineUpgCRecipeBuilder.fluidInfuserRecipe("mossy_stone_bricks")
                .ingredient(new FluidStack(Fluids.WATER, 150))  // input fluid
                .ingredient(Blocks.GRASS, 4)  // input dissolve
                .ingredient(Blocks.STONE_BRICKS)  // input infuse
                .phase(150)  // dissolve ticks
                .phase(100)  // infuse tickes
                .result(Blocks.MOSSY_STONE_BRICKS)
                .experienceRecipe(0.2f)
                .build(consumer);
    }
}
