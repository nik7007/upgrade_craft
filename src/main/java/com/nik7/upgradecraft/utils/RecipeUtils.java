package com.nik7.upgradecraft.utils;

import com.google.gson.*;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Objects;

import static net.minecraftforge.fluids.FluidAttributes.BUCKET_VOLUME;

public final class RecipeUtils {
    public static final String INGREDIENT = "ingredient";
    public static final String INGREDIENTS = "ingredients";
    public static final String PHASE = "phase";
    public static final String PHASES = "phases";
    public static final String TIME = "time";
    public static final String RESULT = "result";
    public static final String RESULTS = "results";
    public static final String EXPERIENCE = "experience";
    public static final String FLUID = "fluid";
    public static final String AMOUNT = "amount";
    public static final String COUNT = "count";
    public static final String CHANCE = "chance";
    public static final String NBT = "nbt";
    public static final String VALUE = "value";
    public static final String ITEM = "item";
    public static final String TAG = "tag";
    public static final int BASE_PHASE_TIME = 25;
    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    private RecipeUtils() {
    }

    public static void parseIngredients(JsonElement element, List<Ingredient> ingredients, List<FluidStack> fluids) {
        if (element.isJsonArray()) {
            for (JsonElement jsonElement : element.getAsJsonArray()) {
                if (jsonElement.isJsonObject() && jsonElement.getAsJsonObject().has(FLUID)) {
                    fluids.add(parseFluidStack(jsonElement));
                } else {
                    ingredients.add(parseIngredient(element));
                }
            }
        }
    }

    public static void parsePhases(JsonElement element, List<Integer> minTicks) {
        if (element.isJsonArray()) {
            for (JsonElement jsonElement : element.getAsJsonArray()) {
                minTicks.add(getTime(jsonElement));
            }
        } else {
            minTicks.add(getTime(element));
        }
    }

    public static void parseResults(JsonElement element, List<ItemStack> items, List<FluidStack> fluids, List<Float> chances) {

        if (element == null) {
            return;
        }
        if (element.isJsonArray()) {
            for (JsonElement arrayElement : element.getAsJsonArray()) {
                if (arrayElement.getAsJsonObject().has(FLUID)) {
                    fluids.add(parseFluidStack(arrayElement));
                } else {
                    ItemStack stack = parseItemStack(arrayElement);
                    if (!stack.isEmpty()) {
                        items.add(stack);
                        chances.add(parseItemChance(arrayElement));
                    }
                }
            }
        } else if (element.getAsJsonObject().has(FLUID)) {
            fluids.add(parseFluidStack(element));
        } else {
            ItemStack stack = parseItemStack(element);
            if (!stack.isEmpty()) {
                items.add(stack);
                chances.add(parseItemChance(element));
            }
        }
    }

    public static int getTime(JsonElement element) {
        if (element.isJsonPrimitive()) {
            return element.getAsInt();
        } else if (element.isJsonObject()) {
            JsonObject jsonObject = element.getAsJsonObject();
            if (jsonObject.has(TIME)) {
                return jsonObject.get(TIME).getAsInt();
            }

        }
        return BASE_PHASE_TIME;
    }

    public static JsonElement serializePhases(List<Integer> times) {
        if (times.size() == 1) {
            return new JsonPrimitive(times.get(0));
        } else {
            JsonArray jsonArray = new JsonArray();
            for (int time : times) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty(TIME, time);
                jsonArray.add(jsonObject);
            }
            return jsonArray;
        }
    }

    public static float parseItemChance(JsonElement element) {

        JsonObject json = element.getAsJsonObject();

        if (json.has(CHANCE)) {
            return json.get(CHANCE).getAsFloat();
        }
        return 1f;
    }

    public static Ingredient parseIngredient(JsonElement element) {

        if (element == null || element.isJsonNull()) {
            return Ingredient.fromStacks(ItemStack.EMPTY);
        }
        Ingredient ingredient;

        if (element.isJsonArray()) {
            try {
                ingredient = Ingredient.deserialize(element);
            } catch (Throwable t) {
                ingredient = Ingredient.fromStacks(ItemStack.EMPTY);
            }
        } else {
            JsonElement subElement = element.getAsJsonObject();
            try {
                JsonObject object = subElement.getAsJsonObject();
                if (object.has(VALUE)) {
                    ingredient = Ingredient.deserialize(object.get(VALUE));
                } else {
                    ingredient = Ingredient.deserialize(subElement);
                }
                int count = 1;
                if (object.has(COUNT)) {
                    count = object.get(COUNT).getAsInt();
                } else if (object.has(AMOUNT)) {
                    count = object.get(AMOUNT).getAsInt();
                }
                if (count > 1) {
                    for (ItemStack stack : ingredient.getMatchingStacks()) {
                        stack.setCount(count);
                    }
                }
            } catch (Throwable t) {
                ingredient = Ingredient.fromStacks(ItemStack.EMPTY);
            }
        }
        return ingredient;
    }

    public static JsonElement serializeIngredient(Ingredient ingredient, int amount) {
        if (amount == 1) {
            return ingredient.serialize();
        } else {
            JsonObject result = new JsonObject();
            result.addProperty(AMOUNT, amount);
            result.add(VALUE, ingredient.serialize());
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
                    stack.setTag(JsonToNBT.getTagFromJson(fluidObject.get(NBT).getAsString()));
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
        element.addProperty(FLUID, Objects.requireNonNull(fluidStack.getRawFluid().getRegistryName()).toString());
        if (fluidStack.hasTag()) {
            CompoundNBT tag = fluidStack.getTag();
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
                CompoundNBT nbt;
                try {
                    if (element.isJsonObject()) {
                        nbt = JsonToNBT.getTagFromJson(GSON.toJson(nbtElement));
                    } else {
                        nbt = JsonToNBT.getTagFromJson(JSONUtils.getString(nbtElement, NBT));
                    }
                    stack.setTag(nbt);
                } catch (Exception e) {
                    return ItemStack.EMPTY;
                }
            }
        }
        return stack;
    }

    public static JsonElement serializeItemStack(ItemStack itemStack, float chance) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty(AMOUNT, itemStack.getCount());
        jsonObject.addProperty(ITEM, Objects.requireNonNull(itemStack.getItem().getRegistryName()).toString());
        if (itemStack.hasTag()) {
            CompoundNBT tag = itemStack.getTag();
            jsonObject.addProperty(NBT, tag.toString());
        }
        jsonObject.addProperty(CHANCE, chance);

        return jsonObject;
    }

}
