package yellowbirb.birbaddons.config;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import yellowbirb.birbaddons.BirbAddonsClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConfigList<T> extends ConfigValue<List<T>>{

    private final Type type;

    public ConfigList(String featureKey, String valueKey, List<T> defaultValue) {
        super(featureKey, valueKey, defaultValue);
        this.type = new TypeToken<T>(){}.getType();
    }

    // TODO: test
    // concern: getting an element, passing it and altering it will not affect actual value

    @Override
    public List<T> getFromJsonElement(JsonElement json) {
        if (!json.isJsonArray()) {
            BirbAddonsClient.LOGGER.warn("Could not load ConfigList {}:{} from config because it is not an array, returning empty list", this.featureKey, this.valueKey);
            return new ArrayList<>();
        }

        List<T> res = new ArrayList<>();
        Gson gson = new Gson();
        for (JsonElement element : json.getAsJsonArray()) {
            res.add(getElementFromJsonElement(gson, element));
        }
        return res;
    }

    public T getElementFromJsonElement(JsonElement element) {
        return getElementFromJsonElement(new Gson(), element);
    }

    public T getElementFromJsonElement(Gson gson, JsonElement element) {
        return gson.fromJson(element, type);
    }

    @Override
    public JsonElement getAsJsonElement(List<T> value) {
        JsonArray array = new JsonArray();
        Gson gson = new Gson();
        for (T element : value) {
            array.add(getElementAsJsonElement(gson, element));
        }
        return array;
    }

    public JsonElement getElementAsJsonElement(T value) {
        return getElementAsJsonElement(new Gson(), value);
    }

    public JsonElement getElementAsJsonElement(Gson gson, T value) {
        return gson.toJsonTree(value, type);
    }

    public void operation(Consumer<List<T>> consumer) {
        List<T> representation = getFromJsonElement(this.value);
        consumer.accept(representation);
        set(getAsJsonElement(representation));
    }

    public void add(T value) {
        this.value.getAsJsonArray().add(getElementAsJsonElement(value));
    }

    public void remove(T value) {
        this.value.getAsJsonArray().remove(getElementAsJsonElement(value));
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand(Consumer<List<T>> consumer) {
        return null;
    }
}
