package com.nik7.upgradecraft.recipes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;

import static net.minecraftforge.fluids.FluidType.BUCKET_VOLUME;

public final class RecipeHelper {
    public static final String FLUID = "fluid";
    public static final String AMOUNT = "amount";
    public static final String COUNT = "count";
    public static final String NBT = "nbt";
    public static final String VALUE = "value";
    public static final String ITEM = "item";
    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private RecipeHelper() {
    }

    public static Ingredient parseIngredient(JsonElement element) {

        if (element == null || element.isJsonNull()) {
            return Ingredient.of(ItemStack.EMPTY);
        }
        Ingredient ingredient;

        if (element.isJsonArray()) {
            try {
                ingredient = Ingredient.fromJson(element);
            } catch (Throwable t) {
                ingredient = Ingredient.of(ItemStack.EMPTY);
            }
        } else {
            JsonElement subElement = element.getAsJsonObject();
            try {
                JsonObject object = subElement.getAsJsonObject();
                if (object.has(VALUE)) {
                    ingredient = Ingredient.fromJson(object.get(VALUE));
                } else {
                    ingredient = Ingredient.fromJson(subElement);
                }
                int count = 1;
                if (object.has(COUNT)) {
                    count = object.get(COUNT).getAsInt();
                } else if (object.has(AMOUNT)) {
                    count = object.get(AMOUNT).getAsInt();
                }
                if (count > 1) {
                    for (ItemStack stack : ingredient.getItems()) {
                        stack.setCount(count);
                    }
                }
            } catch (Throwable t) {
                ingredient = Ingredient.of(ItemStack.EMPTY);
            }
        }
        return ingredient;
    }

    public static JsonElement serializeIngredient(Ingredient ingredient) {
        int amount = Arrays.stream(ingredient.getItems())
                .map(ItemStack::getCount)
                .findFirst()
                .orElse(1);

        if (amount == 1) {
            return ingredient.toJson();
        } else {
            JsonObject result = new JsonObject();
            result.addProperty(AMOUNT, amount);
            result.add(VALUE, ingredient.toJson());
            return result;
        }
    }

    public static FluidStack parseFluidStack(JsonElement element) {

        if (element == null || element.isJsonNull()) {
            return FluidStack.EMPTY;
        }
        FluidStack stack;
        Fluid fluid = null;
        int amount = BUCKET_VOLUME;

        if (element.isJsonPrimitive()) {
            fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(element.getAsString()));
            return fluid == null ? FluidStack.EMPTY : new FluidStack(fluid, amount);
        } else {
            JsonObject fluidObject = element.getAsJsonObject();

            if (fluidObject.has(AMOUNT)) {
                amount = fluidObject.get(AMOUNT).getAsInt();
            } else if (fluidObject.has(COUNT)) {
                amount = fluidObject.get(COUNT).getAsInt();
            }

            if (fluidObject.has(FLUID)) {
                fluid = ForgeRegistries.FLUIDS.getValue(new ResourceLocation(fluidObject.get(FLUID).getAsString()));
            }
            if (fluid == null) {
                return FluidStack.EMPTY;
            }
            stack = new FluidStack(fluid, amount);

            if (fluidObject.has(NBT)) {
                try {
                    stack.setTag(TagParser.parseTag(fluidObject.get(NBT).getAsString()));
                } catch (Exception e) {
                    return FluidStack.EMPTY;
                }
            }
        }
        return stack;
    }

    public static JsonElement serializeFluidStack(FluidStack fluidStack) {
        JsonObject element = new JsonObject();
        element.addProperty(AMOUNT, fluidStack.getAmount());
        element.addProperty(FLUID, ForgeRegistries.FLUIDS.getKey(fluidStack.getFluid()).toString());
        if (fluidStack.hasTag()) {
            CompoundTag tag = fluidStack.getTag();
            element.addProperty(NBT, tag.toString());
        }
        return element;
    }

    public static ItemStack parseItemStack(JsonElement element) {

        if (element == null || element.isJsonNull()) {
            return ItemStack.EMPTY;
        }
        ItemStack stack;
        Item item = null;
        int count = 1;

        if (element.isJsonPrimitive()) {
            item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(element.getAsString()));
            return item == null ? ItemStack.EMPTY : new ItemStack(item);
        } else {
            JsonObject itemObject = element.getAsJsonObject();

            /* COUNT */
            if (itemObject.has(COUNT)) {
                count = itemObject.get(COUNT).getAsInt();
            } else if (itemObject.has(AMOUNT)) {
                count = itemObject.get(AMOUNT).getAsInt();
            }

            /* ITEM */
            if (itemObject.has(ITEM)) {
                item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemObject.get(ITEM).getAsString()));
            }
            if (item == null) {
                return ItemStack.EMPTY;
            }
            stack = new ItemStack(item, count);

            /* NBT */
            if (itemObject.has(NBT)) {
                JsonElement nbtElement = itemObject.get(NBT);
                CompoundTag nbt;
                try {
                    if (element.isJsonObject()) {
                        nbt = TagParser.parseTag(GSON.toJson(nbtElement));
                    } else {
                        nbt = TagParser.parseTag(GsonHelper.convertToString(nbtElement, NBT));
                    }
                    stack.setTag(nbt);
                } catch (Exception e) {
                    return ItemStack.EMPTY;
                }
            }
        }
        return stack;
    }

    public static JsonElement serializeItemStack(ItemStack itemStack) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(AMOUNT, itemStack.getCount());
        jsonObject.addProperty(ITEM, ForgeRegistries.ITEMS.getKey(itemStack.getItem()).toString());
        if (itemStack.hasTag()) {
            CompoundTag tag = itemStack.getTag();
            jsonObject.addProperty(NBT, tag.toString());
        }
        return jsonObject;
    }


}
