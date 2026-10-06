package yellowbirb.birbaddons.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;

import java.util.List;
import java.util.function.Consumer;

public class ConfigList<T> extends ConfigValue<List<T>>{

    public ConfigList(String featureKey, String valueKey, List<T> defaultValue) {
        super(featureKey, valueKey, defaultValue);
    }

    // TODO: test

    @Override
    public List<T> getFromJsonElement(JsonElement json) {
        return new Gson().fromJson(json, List.class);
    }

    @Override
    public JsonElement getAsJsonElement(List<T> value) {
        return JsonParser.parseString(new Gson().toJson(value));
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand(Consumer<List<T>> consumer) {
        return null;
    }
}
