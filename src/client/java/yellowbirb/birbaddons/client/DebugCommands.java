package yellowbirb.birbaddons.client;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import yellowbirb.birbaddons.client.hud.AdrenalineBar;

public class DebugCommands {

    public static void init() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommands.literal("startAdrenalin").executes((context) -> {
                    AdrenalineBar.adrenalineUsed(40, 100);
                    return 1;
                }))
        );

    }

}
