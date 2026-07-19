package yellowbirb.birbaddons.config;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import org.joml.Vector2d;
import yellowbirb.birbaddons.util.Utils;

import java.util.function.Consumer;

public class ConfigVec2d extends ConfigValue<Vector2d>{
    public ConfigVec2d(String featureKey, String valueKey, Vector2d defaultValue) {
        super(featureKey, valueKey, defaultValue);
    }

    @Override
    public Vector2d getFromJsonElement(JsonElement json) {
        return new Gson().fromJson(value, Vector2d.class);
    }

    @Override
    public JsonElement getAsJsonElement(Vector2d value) {
        return new Gson().toJsonTree(value, Vector2d.class);
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand(Consumer<Vector2d> consumer) {
        return ClientCommands.literal(getValueKey().toLowerCase())
                .then(ClientCommands.argument("x", IntegerArgumentType.integer())
                        .then(ClientCommands.argument("y", IntegerArgumentType.integer())
                                .executes((ctx) -> {
                                    int x = IntegerArgumentType.getInteger(ctx, "x");
                                    int y = IntegerArgumentType.getInteger(ctx, "y");
                                    Vector2d arg = new Vector2d(x, y);
                                    set(arg);
                                    consumer.accept(arg);
                                    Utils.displayMessage(ctx.getSource().getPlayer(), "set " + getValueKey() + " to " + x + ", " + y);
                                    return 1;
                                })));
    }
}
