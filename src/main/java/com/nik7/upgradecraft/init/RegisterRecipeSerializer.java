package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.recipes.FluidInfuserRecipe;
import com.nik7.upgradecraft.recipes.RecipeSerializerUpgc;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.nik7.upgradecraft.UpgradeCraft.MOD_ID;

public class RegisterRecipeSerializer {

    private static final DeferredRegister<IRecipeSerializer<?>> REGISTER = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MOD_ID);
    public static final RegistryObject<RecipeSerializerUpgc<FluidInfuserRecipe>> FLUID_INFUSER_SERIALIZER = REGISTER.register("fluid_infuser",
            () -> new RecipeSerializerUpgc<>(FluidInfuserRecipe::new));

    public static void init() {
        REGISTER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
