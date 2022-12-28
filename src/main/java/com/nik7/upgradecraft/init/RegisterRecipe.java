package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.recipes.FluidInfuserRecipe;
import com.nik7.upgradecraft.recipes.ModRecipeType;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.nik7.upgradecraft.UpgradeCraft.MODID;

public final class RegisterRecipe {
    private static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_TYPES, MODID);

    public static final String FLUID_INFUSER_RECIPE_TYPE_ID = "fluid_infuser_recipe_type";
    public static final RegistryObject<ModRecipeType<FluidInfuserRecipe>> FLUID_INFUSER_RECIPE_TYPE = RECIPE_TYPES.register(FLUID_INFUSER_RECIPE_TYPE_ID,
            () -> new ModRecipeType<>(MODID, FLUID_INFUSER_RECIPE_TYPE_ID));
    private RegisterRecipe() {
    }

    public static void register(IEventBus modEventBus) {
        RECIPE_TYPES.register(modEventBus);
    }
}
