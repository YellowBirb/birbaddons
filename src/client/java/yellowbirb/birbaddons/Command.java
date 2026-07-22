package yellowbirb.birbaddons;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import yellowbirb.birbaddons.util.Utils;

public class Command {

    private final LiteralArgumentBuilder<FabricClientCommandSource> builder;

    public Command() {
        builder = ClientCommands.literal("ba").executes(/* TODO: open menu */ (_) -> {
            Utils.displayMessage("open menu (there is no menu yet)");
            return 1;
        });
    }

    public LiteralArgumentBuilder<FabricClientCommandSource> getBuilder() {
        return builder;
    }

    public void registerCommand(LiteralArgumentBuilder<FabricClientCommandSource> command) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
            var ba = dispatcher.register(command);
            dispatcher.register(ClientCommands.literal("birbaddons").executes(/* TODO: open menu */ (_) -> {
                Utils.displayMessage("open menu (there is no menu yet)");
                return 1;
            }).redirect(ba));
        });
    }

}
