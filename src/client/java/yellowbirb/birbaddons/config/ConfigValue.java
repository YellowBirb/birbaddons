package yellowbirb.birbaddons.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public abstract class ConfigValue<T> {

    protected final String featureKey;
    protected final String valueKey;
    protected final JsonElement defaultValue;
    protected JsonElement value;
    protected int hash = 0;

    public ConfigValue(String featureKey, String valueKey, JsonElement defaultValue) {
        this.featureKey = featureKey;
        this.valueKey = valueKey;
        this.defaultValue = defaultValue;
        load();
    }

    public void load() {
        if (value == null || hash != Config.hash()) {
            if (Config.get().has(featureKey)) {
                JsonObject object = Config.get().getAsJsonObject(featureKey);
                value = object.has(valueKey) ? object.get(valueKey) : defaultValue;
            } else {
                value = defaultValue;
            }
            hash = Config.hash();
        }
    }

    abstract public T get();

    abstract public void set(T value);

    protected void set(JsonElement jsonElement) {
        if (!Config.get().has(featureKey)) {
            Config.get().add(featureKey, new JsonObject());
        }
        value = jsonElement;
        Config.get().getAsJsonObject(featureKey).add(valueKey, value);
        hash = Config.hash(true);
    }
}
