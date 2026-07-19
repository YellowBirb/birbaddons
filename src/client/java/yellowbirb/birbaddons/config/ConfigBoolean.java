package yellowbirb.birbaddons.config;

import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import yellowbirb.birbaddons.util.Utils;

import java.util.function.Consumer;

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

    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return ClientCommands.literal("set"+getValueKey().toLowerCase())
                .then(ClientCommands.argument("boolean", BoolArgumentType.bool()).executes((ctx) -> {
                    boolean arg = BoolArgumentType.getBool(ctx, "boolean");
                    set(arg);
                    Utils.displayMessage(ctx.getSource().getPlayer(), "set " + getValueKey() + " to " + arg);
                    return 1;
                }));
    }

    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand(Consumer<Boolean> consumer) {
        return ClientCommands.literal("set"+getValueKey().toLowerCase())
                .then(ClientCommands.argument("boolean", BoolArgumentType.bool()).executes((ctx) -> {
                    boolean arg = BoolArgumentType.getBool(ctx, "boolean");
                    set(arg);
                    consumer.accept(arg);
                    Utils.displayMessage(ctx.getSource().getPlayer(), "set " + getValueKey() + " to " + arg);
                    return 1;
                }));
    }
}
