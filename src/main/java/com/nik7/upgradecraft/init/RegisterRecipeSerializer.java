package com.nik7.upgradecraft.init;

import com.nik7.upgradecraft.recipes.FluidInfuserRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static com.nik7.upgradecraft.UpgradeCraft.MODID;

public final class RegisterRecipeSerializer {
    private static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MODID);

    private static final String FLUID_INFUSER_RECIPE_SERIALIZER_ID = "fluid_infuser_recipe_serializer";
    public static final RegistryObject<FluidInfuserRecipeSerializer> FLUID_INFUSER_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(FLUID_INFUSER_RECIPE_SERIALIZER_ID,
            FluidInfuserRecipeSerializer::new);

    private RegisterRecipeSerializer() {
    }

    public static void register(IEventBus modEventBus) {
        RECIPE_SERIALIZERS.register(modEventBus);
    }
}
