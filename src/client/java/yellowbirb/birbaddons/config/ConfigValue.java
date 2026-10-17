package yellowbirb.birbaddons.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.function.Consumer;

public abstract class ConfigValue<T> {

    protected final String featureKey;
    protected final String valueKey;
    private final T rawDefaultValue;
    private JsonElement defaultValue;
    protected JsonElement value;
    protected int hash = 0;

    public ConfigValue(String featureKey, String valueKey, T defaultValue) {
        this.featureKey = featureKey;
        this.valueKey = valueKey;
        this.rawDefaultValue = defaultValue;
    }

    protected JsonElement getDefaultJson() {
        if (defaultValue == null) {
            defaultValue = getAsJsonElement(rawDefaultValue);
        }
        return defaultValue;
    }

    public void load() {
        if (value == null || hash != Config.hash()) {
            if (Config.get().has(featureKey)) {
                JsonObject object = Config.get().getAsJsonObject(featureKey);
                value = object.has(valueKey) ? object.get(valueKey) : getDefaultJson();
            } else {
                value = getDefaultJson();
            }
            hash = Config.hash();
        }
    }

    public T get() {
        load();
        return getFromJsonElement(value);
    }

    abstract public T getFromJsonElement(JsonElement json);

    public void set(T value) {
        set(getAsJsonElement(value));
    }

    protected void set(JsonElement jsonElement) {
        if (!Config.get().has(featureKey)) {
            Config.get().add(featureKey, new JsonObject());
        }
        value = jsonElement;
        Config.get().getAsJsonObject(featureKey).add(valueKey, value);
        hash = Config.hash(true);
    }

    abstract public JsonElement getAsJsonElement(T value);

    public String getValueKey() {
        return valueKey;
    }

    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return getCommand((_) -> {});
    }

    abstract public LiteralArgumentBuilder<FabricClientCommandSource> getCommand(Consumer<T> consumer);
}
