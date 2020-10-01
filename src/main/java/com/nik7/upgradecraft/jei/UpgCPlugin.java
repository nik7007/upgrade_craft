package com.nik7.upgradecraft.jei;

import com.nik7.upgradecraft.client.gui.container.FluidFurnaceScreen;
import com.nik7.upgradecraft.container.FluidFurnaceContainer;
import com.nik7.upgradecraft.init.RegisterBlocks;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.registration.*;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

@JeiPlugin
public class UpgCPlugin implements IModPlugin {

    // private FluidFurnaceCategory fluidFurnaceCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        //   IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        //   IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        //   this.fluidFurnaceCategory = new FluidFurnaceCategory(guiHelper);
        //   registration.addRecipeCategories(this.fluidFurnaceCategory);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        // assert Minecraft.getInstance().world != null;
        // RecipeManager recipeManager = Minecraft.getInstance().world.getRecipeManager();
        // List<FurnaceRecipe> furnaceRecipes = recipeManager.getRecipesForType(IRecipeType.SMELTING);
        // registration.addRecipes(furnaceRecipes, UpgCRecipeCategoryUid.FLUID_FURNACE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(FluidFurnaceScreen.class, 78, 32, 28, 23, VanillaRecipeCategoryUid.FURNACE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(FluidFurnaceContainer.class, VanillaRecipeCategoryUid.FURNACE, 0, 1, 2, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(RegisterBlocks.FLUID_FURNACE_BLOCK.get()), VanillaRecipeCategoryUid.FURNACE);
    }
}
