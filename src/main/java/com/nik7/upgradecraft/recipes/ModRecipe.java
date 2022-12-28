package com.nik7.upgradecraft.recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public abstract class ModRecipe extends BaseRecipe {

    private final Map<String, RecipeComponent<?>> components = new HashMap<>();

    public ModRecipe(ResourceLocation recipeId) {
        super(recipeId);
    }

    public ModRecipe(ResourceLocation recipeId, RecipeComponent<?>... components) {
        this(recipeId);
        addComponent(components);
    }

    public void addComponent(RecipeComponent<?>... components) {
        for (RecipeComponent<?> component : components) {
            String name = component.name();
            if (this.components.containsKey(name)) {
                throw new RuntimeException("Impossible to set duplicate elements! name: " + name);
            }
            this.components.put(name, component);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getComponent(String name) {
        return (T) components.get(name);
    }

    public void toJson(JsonObject result) {
        components.forEach((s, component) -> {
            result.add(component.name(), component.toJson());
        });
    }

    public void toNetwork(FriendlyByteBuf buffer) {
        int size = components.size();
        buffer.writeVarInt(size);
        components.forEach((s, component) -> {
            component.toNetwork(buffer);
        });
    }

    public static List<RecipeComponent<?>> fromJson(JsonElement element) {
        JsonObject jsonObject = element.getAsJsonObject();
        Set<String> componentNames = jsonObject.keySet();
        List<RecipeComponent<?>> results = new ArrayList<>(componentNames.size());
        for (String componentName : componentNames) {
            JsonElement jsonElement = jsonObject.get(componentName);
            if (!jsonElement.isJsonObject()) {
                continue;
            }
            results.add(RecipeComponent.fromJson(jsonElement));
        }

        return results;
    }

    public static List<RecipeComponent<?>> fromNetwork(FriendlyByteBuf buffer) {
        int componentsNumber = buffer.readVarInt();
        List<RecipeComponent<?>> results = new ArrayList<>(componentsNumber);
        for (int i = 0; i < componentsNumber; i++) {
            results.add(RecipeComponent.fromNetwork(buffer));
        }
        return results;
    }

}
