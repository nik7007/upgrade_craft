package com.nik7.upgradecraft.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nik7.upgradecraft.client.gui.container.FluidFurnaceScreen;
import com.nik7.upgradecraft.init.RegisterBlocks;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class FluidFurnaceCategory implements IRecipeCategory<FurnaceRecipe> {
    protected final IDrawableAnimated flame;
    protected final IDrawableAnimated arrow;
    private final IDrawable background;
    private final IDrawable icon;

    public FluidFurnaceCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(FluidFurnaceScreen.GUI, 54, 16, 82, 54);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(RegisterBlocks.FLUID_FURNACE_BLOCK.get()));
        this.flame = guiHelper.drawableBuilder(FluidFurnaceScreen.GUI, 176, 0, 14, 14).buildAnimated(300, StartDirection.TOP, true);
        this.arrow = guiHelper.drawableBuilder(FluidFurnaceScreen.GUI, 176, 14, 24, 17).buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public void draw(FurnaceRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        flame.draw(matrixStack, 3, 20);
        arrow.draw(matrixStack, 25, 18);

        float experience = recipe.getExperience();
        if (experience > 0.0F) {
            TranslationTextComponent experienceString = new TranslationTextComponent("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.fontRenderer;
            int stringWidth = fontRenderer.func_238414_a_(experienceString);
            fontRenderer.func_243248_b(matrixStack, experienceString, (float) (this.background.getWidth() - stringWidth), 0.0F, -8355712);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UpgCRecipeCategoryUid.FLUID_FURNACE;
    }

    @Override
    public Class<? extends FurnaceRecipe> getRecipeClass() {
        return FurnaceRecipe.class;
    }

    @Override
    public String getTitle() {
        return I18n.format("block.upgradecraft.fluid_furnace_block");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(FurnaceRecipe furnaceRecipe, IIngredients iIngredients) {
        iIngredients.setInputIngredients(furnaceRecipe.getIngredients());
        iIngredients.setOutput(VanillaTypes.ITEM, furnaceRecipe.getRecipeOutput());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, FurnaceRecipe furnaceRecipe, IIngredients iIngredients) {
        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 1, 0);
        guiItemStacks.init(1, false, 61, 18);
        guiItemStacks.set(iIngredients);
    }
}
