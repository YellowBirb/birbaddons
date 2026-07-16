package yellowbirb.birbaddons.config;

import com.google.gson.JsonPrimitive;

public class ConfigBoolean extends ConfigValue<Boolean>{

    public ConfigBoolean(String featureKey, String valueKey, Boolean defaultValue) {
        super(featureKey, valueKey, new JsonPrimitive(defaultValue));
    }

    @Override
    public Boolean get() {
        load();
        return value.getAsBoolean();
    }

    @Override
    public void set(Boolean value) {
        set(new JsonPrimitive(value));
    }
}
