package com.nik7.upgradecraft.jei;

import com.nik7.upgradecraft.recipes.FluidInfuserRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class FluidInfuserCategory implements IRecipeCategory<FluidInfuserRecipe> {

    public FluidInfuserCategory(IGuiHelper guiHelper) {
    }

    @Override
    public ResourceLocation getUid() {
        return UpgCRecipeCategoryUid.FLUID_INFUSER;
    }

    @Override
    public Class<? extends FluidInfuserRecipe> getRecipeClass() {
        return FluidInfuserRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("block.upgradecraft.fluid_infuser_block");
    }

    @Override
    public IDrawable getBackground() {
        return null;
    }

    @Override
    public IDrawable getIcon() {
        return null;
    }

    @Override
    public void setIngredients(FluidInfuserRecipe fluidInfuserRecipe, IIngredients iIngredients) {
        iIngredients.setInput(VanillaTypes.FLUID, fluidInfuserRecipe.getFluidStack());
        iIngredients.setInputIngredients(fluidInfuserRecipe.getInputItems());
        iIngredients.setOutput(VanillaTypes.ITEM, fluidInfuserRecipe.getResult());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, FluidInfuserRecipe fluidInfuserRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();
        iRecipeLayout.getFluidStacks().init(0, true, 1, 0);
        guiItemStacks.init(0, true, 8, 0);
        guiItemStacks.init(1, true, 16, 0);
        guiItemStacks.init(2, false, 61, 18);
    }
}
