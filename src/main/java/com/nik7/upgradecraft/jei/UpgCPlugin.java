package com.nik7.upgradecraft.jei;

import com.nik7.upgradecraft.client.gui.container.FluidFurnaceScreen;
import com.nik7.upgradecraft.client.gui.container.FluidInfuserScreen;
import com.nik7.upgradecraft.container.FluidFurnaceContainer;
import com.nik7.upgradecraft.container.FluidInfuserContainer;
import com.nik7.upgradecraft.init.RegisterBlocks;
import com.nik7.upgradecraft.recipes.FluidInfuserRecipeManager;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

@SuppressWarnings("unused")
@JeiPlugin
public class UpgCPlugin implements IModPlugin {

    @Override
    @Nonnull
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(MOD_ID, "jei_plugin");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
        FluidInfuserCategory fluidInfuserCategory = new FluidInfuserCategory(guiHelper);
        registration.addRecipeCategories(fluidInfuserCategory);
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        assert Minecraft.getInstance().world != null;
        registration.addRecipes(FluidInfuserRecipeManager.getManager(Minecraft.getInstance().world).getRecipes(), UpgCRecipeCategoryUid.FLUID_INFUSER);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(FluidFurnaceScreen.class, 78, 32, 28, 23, VanillaRecipeCategoryUid.FURNACE);
        registration.addRecipeClickArea(FluidInfuserScreen.class, 105, 37, 25, 18, UpgCRecipeCategoryUid.FLUID_INFUSER);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(FluidFurnaceContainer.class, VanillaRecipeCategoryUid.FURNACE, 0, 1, 2, 36);
        registration.addRecipeTransferHandler(FluidInfuserContainer.class, UpgCRecipeCategoryUid.FLUID_INFUSER, 0, 2, 3, 36);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(RegisterBlocks.FLUID_FURNACE_BLOCK.get()), VanillaRecipeCategoryUid.FURNACE);
        registration.addRecipeCatalyst(new ItemStack(RegisterBlocks.FLUID_INFUSER_BLOCK.get()), UpgCRecipeCategoryUid.FLUID_INFUSER);
    }
}
