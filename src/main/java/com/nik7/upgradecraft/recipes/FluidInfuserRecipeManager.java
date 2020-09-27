package com.nik7.upgradecraft.recipes;

import com.nik7.upgradecraft.init.RecipeTypesUpgC;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class FluidInfuserRecipeManager {
    private final static FluidInfuserRecipeManager INSTANCE = new FluidInfuserRecipeManager();
    private List<FluidInfuserRecipe> recipes = null;

    private FluidInfuserRecipeManager() {
    }

    public static FluidInfuserRecipeManager getManager(World world) {
        if (INSTANCE.recipes == null) {
            INSTANCE.recipes = world.getRecipeManager().getRecipesForType(RecipeTypesUpgC.RECIPE_FLUID_INFUSER);
        }
        return INSTANCE;
    }

    @Nullable
    public FluidInfuserRecipe getRecipe(FluidStack fluidStack, IItemProvider dissolve, IItemProvider infuse) {
        return recipes.stream().filter(r ->
                r.getFluidStack().getFluid() == fluidStack.getFluid() &&
                        r.getDissolve().test(new ItemStack(dissolve)) &&
                        r.getInfuse().test(new ItemStack(infuse))
        ).findFirst().orElse(null);
    }

    public List<FluidInfuserRecipe> getRecipes(FluidStack fluidStack,
                                               IItemProvider dissolve,
                                               IItemProvider infuse) {
        return recipes.stream().filter(r ->
                (fluidStack.getFluid() == Fluids.EMPTY || r.getFluidStack().getFluid() == fluidStack.getFluid()) &&
                        (dissolve.asItem() == Items.AIR || r.getDissolve().test(new ItemStack(dissolve))) &&
                        (infuse.asItem() == Items.AIR || r.getInfuse().test(new ItemStack(infuse)))
        ).collect(Collectors.toList());
    }
}
