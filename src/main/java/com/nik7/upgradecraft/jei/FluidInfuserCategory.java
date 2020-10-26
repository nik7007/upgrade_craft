package com.nik7.upgradecraft.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nik7.upgradecraft.client.gui.container.FluidInfuserScreen;
import com.nik7.upgradecraft.init.Config;
import com.nik7.upgradecraft.init.RegisterBlocks;
import com.nik7.upgradecraft.recipes.FluidInfuserRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated.StartDirection;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IGuiFluidStackGroup;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fluids.FluidAttributes;

import javax.annotation.Nonnull;

public class FluidInfuserCategory implements IRecipeCategory<FluidInfuserRecipe> {
    private static final ResourceLocation GUI = FluidInfuserScreen.GUI;

    private final IDrawableStatic background;
    private final IDrawable icon;
    private final IDrawable bubbles;
    private final IDrawable arrow;

    public FluidInfuserCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(GUI, 10, 16, 155, 54);
        this.icon = guiHelper.createDrawableIngredient(new ItemStack(RegisterBlocks.FLUID_INFUSER_BLOCK.get()));
        this.bubbles = guiHelper.drawableBuilder(GUI, 176, 0, 15, 14).buildAnimated(300, StartDirection.TOP, true);
        this.arrow = guiHelper.drawableBuilder(GUI, 176, 14, 24, 17).buildAnimated(200, StartDirection.LEFT, false);

    }

    @Override
    public void draw(@Nonnull FluidInfuserRecipe recipe, @Nonnull MatrixStack matrixStack, double mouseX, double mouseY) {
        bubbles.draw(matrixStack, 42, 23);
        arrow.draw(matrixStack, 94, 21);

        drawExperience(recipe, matrixStack);
        drawTime(recipe.getDissolveTick(), matrixStack, 42, 40, 16);
        drawTime(recipe.getInfuseTick(), matrixStack, 92, 40, 16);
    }

    private void drawExperience(FluidInfuserRecipe recipe, MatrixStack matrixStack) {
        float experience = recipe.getExperience();
        if (experience > 0.0F) {
            TranslationTextComponent experienceString = new TranslationTextComponent("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.fontRenderer;
            int stringWidth = fontRenderer.getStringPropertyWidth(experienceString);
            fontRenderer.func_243248_b(matrixStack, experienceString, (float) (this.background.getWidth() - stringWidth), 5.0F, -8355712);
        }
    }

    protected void drawTime(int operationTime, MatrixStack matrixStack, int x, int y, int width) {
        if (operationTime > 0) {
            int cookTimeSeconds = operationTime / 20;
            TranslationTextComponent timeString = new TranslationTextComponent("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            FontRenderer fontRenderer = minecraft.fontRenderer;
            int stringWidth = fontRenderer.getStringPropertyWidth(timeString);
            fontRenderer.func_243248_b(matrixStack, timeString, (float) x + (width - stringWidth) / 2.0f, (float) y, -8355712);
        }

    }

    @Override
    @Nonnull
    public ResourceLocation getUid() {
        return UpgCRecipeCategoryUid.FLUID_INFUSER;
    }

    @Override
    @Nonnull
    public Class<? extends FluidInfuserRecipe> getRecipeClass() {
        return FluidInfuserRecipe.class;
    }

    @Override
    @Nonnull
    public String getTitle() {
        return I18n.format("block.upgradecraft.fluid_infuser_block");
    }

    @Override
    @Nonnull
    public IDrawable getBackground() {
        return background;
    }

    @Override
    @Nonnull
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setIngredients(FluidInfuserRecipe fluidInfuserRecipe, IIngredients iIngredients) {
        iIngredients.setInput(VanillaTypes.FLUID, fluidInfuserRecipe.getFluidStack());
        iIngredients.setInputIngredients(fluidInfuserRecipe.getInputItems());
        iIngredients.setOutput(VanillaTypes.ITEM, fluidInfuserRecipe.getResult());
    }

    @Override
    public void setRecipe(IRecipeLayout iRecipeLayout, @Nonnull FluidInfuserRecipe fluidInfuserRecipe, @Nonnull IIngredients iIngredients) {
        IGuiItemStackGroup guiItemStacks = iRecipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 41, 3);
        guiItemStacks.init(1, true, 71, 3);
        guiItemStacks.init(2, false, 131, 21);

        IGuiFluidStackGroup guiFluidStacks = iRecipeLayout.getFluidStacks();
        guiFluidStacks.init(3, true, 5, 10, 16, 32, Config.TANK_CAPACITY.get() * FluidAttributes.BUCKET_VOLUME, true, null);

        guiItemStacks.set(iIngredients);
        guiFluidStacks.set(iIngredients);
    }
}
