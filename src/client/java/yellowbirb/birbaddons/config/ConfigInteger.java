package yellowbirb.birbaddons.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import yellowbirb.birbaddons.util.Utils;

import java.util.function.Consumer;

public class ConfigInteger extends ConfigValue<Integer>{
    public ConfigInteger(String featureKey, String valueKey, Integer defaultValue) {
        super(featureKey, valueKey, defaultValue);
    }

    @Override
    public Integer getFromJsonElement(JsonElement json) {
        return json.getAsInt();
    }

    @Override
    public JsonElement getAsJsonElement(Integer value) {
        return new JsonPrimitive(value);
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand(Consumer<Integer> consumer) {
        return ClientCommands.literal(getValueKey().toLowerCase())
                .then(ClientCommands.argument("integer", IntegerArgumentType.integer())
                        .executes((ctx) -> {
                            int arg = IntegerArgumentType.getInteger(ctx, "integer");
                            set(arg);
                            consumer.accept(arg);
                            Utils.displayMessage(ctx.getSource().getPlayer(), "set " + getValueKey() + " to " + arg);
                            return 1;
                        }));
    }
}
