package yellowbirb.birbaddons.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import yellowbirb.birbaddons.util.Utils;

import java.util.function.Consumer;

public class ConfigString extends ConfigValue<String>{
    public ConfigString(String featureKey, String valueKey, String defaultValue) {
        super(featureKey, valueKey, defaultValue);
    }

    @Override
    public String getFromJsonElement(JsonElement json) {
        return json.getAsString();
    }

    @Override
    public JsonElement getAsJsonElement(String value) {
        return new JsonPrimitive(value);
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand(Consumer<String> consumer) {
        return ClientCommands.literal(getValueKey().toLowerCase())
                .then(ClientCommands.argument("string", StringArgumentType.string())
                        .executes((ctx) -> {
                            String arg = StringArgumentType.getString(ctx, "string");
                            set(arg);
                            consumer.accept(arg);
                            Utils.displayMessage(ctx.getSource().getPlayer(), "set " + getValueKey() + " to " + arg);
                            return 1;
                        }));
    }
}
