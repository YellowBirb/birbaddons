package yellowbirb.birbaddons.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import yellowbirb.birbaddons.util.Utils;

import java.util.function.Consumer;

public class ConfigFloat extends ConfigValue<Float>{
    public ConfigFloat(String featureKey, String valueKey, Float defaultValue) {
        super(featureKey, valueKey, defaultValue);
    }

    @Override
    public Float getFromJsonElement(JsonElement json) {
        return json.getAsFloat();
    }

    @Override
    public JsonElement getAsJsonElement(Float value) {
        return new JsonPrimitive(value);
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand(Consumer<Float> consumer) {
        return ClientCommands.literal(getValueKey().toLowerCase())
                .then(ClientCommands.argument("float", FloatArgumentType.floatArg())
                        .executes((ctx) -> {
                            float arg = FloatArgumentType.getFloat(ctx, "float");
                            set(arg);
                            consumer.accept(arg);
                            Utils.displayMessage(ctx.getSource().getPlayer(), "set " + getValueKey() + " to " + arg);
                            return 1;
                        }));
    }
}
