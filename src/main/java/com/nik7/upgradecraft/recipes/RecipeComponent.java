package com.nik7.upgradecraft.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.fluids.FluidStack;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public record RecipeComponent<T>(String name,
                                 T component,
                                 RecipeComponent.Type type) {

    public JsonElement toJson() {
        JsonObject result = new JsonObject();
        result.addProperty("name", this.name);
        result.addProperty("type", this.type.getId());
        JsonElement jsonComponent = switch (type) {
            case INGREDIENT -> RecipeHelper.serializeIngredient((Ingredient) this.component);
            case FLUID_STACK -> RecipeHelper.serializeFluidStack((FluidStack) this.component);
            case STRING -> new JsonPrimitive((String) this.component);
            case INTEGER -> new JsonPrimitive((Integer) this.component);
            case ITEM_STACK -> RecipeHelper.serializeItemStack((ItemStack) this.component);
            case FLOAT -> new JsonPrimitive((Float) this.component);
        };
        result.add("component", jsonComponent);
        return result;
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeBytes(this.name.getBytes(StandardCharsets.UTF_8));
        buffer.writeVarInt(this.type.getId());
        switch (type) {
            case INGREDIENT -> ((Ingredient) this.component).toNetwork(buffer);
            case FLUID_STACK -> buffer.writeFluidStack((FluidStack) this.component);
            case STRING -> buffer.writeBytes(((String) this.component).getBytes(StandardCharsets.UTF_8));
            case INTEGER -> buffer.writeVarInt((Integer) this.component);
            case ITEM_STACK -> buffer.writeItem((ItemStack) this.component);
            case FLOAT -> buffer.writeFloat((Float) this.component);
        }
    }

    @SuppressWarnings("unchecked")
    public static <U> RecipeComponent<U> fromJson(JsonElement element) {
        JsonObject jsonObject = element.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        int typeId = jsonObject.get("type").getAsInt();
        Type type = Type.idToType(typeId);
        JsonElement jsonComponent = jsonObject.get("component");
        U component = (U) switch (type) {
            case INGREDIENT -> RecipeHelper.parseIngredient(jsonComponent);
            case FLUID_STACK -> RecipeHelper.parseFluidStack(element);
            case STRING -> jsonComponent.getAsString();
            case INTEGER -> jsonComponent.getAsInt();
            case ITEM_STACK -> RecipeHelper.parseItemStack(element);
            case FLOAT -> jsonComponent.getAsFloat();
        };
        return new RecipeComponent<>(name, component, type);
    }

    @SuppressWarnings("unchecked")
    public static <U> RecipeComponent<U> fromNetwork(FriendlyByteBuf buffer) {
        String name = new String(buffer.readByteArray());
        Type type = Type.idToType(buffer.readVarInt());
        U component = (U) switch (type) {
            case INGREDIENT -> Ingredient.fromNetwork(buffer);
            case FLUID_STACK -> buffer.readFluidStack();
            case STRING -> new String(buffer.readByteArray());
            case INTEGER -> buffer.readVarInt();
            case ITEM_STACK -> buffer.readItem();
            case FLOAT -> buffer.readFloat();
        };

        return new RecipeComponent<>(name, component, type);
    }

    public enum Type {
        INGREDIENT(0),
        FLUID_STACK(1),
        STRING(2),
        INTEGER(3),
        ITEM_STACK(4),
        FLOAT(5)
        ;

        public static Type idToType(int id) {
            return Arrays.stream(Type.values())
                    .filter(t -> t.id == id)
                    .findFirst()
                    .orElseThrow();
        }

        private final int id;

        Type(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
