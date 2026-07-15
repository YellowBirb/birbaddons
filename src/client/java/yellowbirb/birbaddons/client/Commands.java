package yellowbirb.birbaddons.client;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import yellowbirb.birbaddons.client.hud.AdrenalineBar;

public class Commands {

    public static void init() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommands.literal("ping").executes((context) -> {
                    LocalPlayer player = context.getSource().getPlayer();
                    player.sendSystemMessage(Component.literal("§3[BirbAddons] §aPing >> " + player.connection.getPlayerInfo(player.getUUID()).getLatency() + "ms"));
                    return 1;
                }))
        );

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommands.literal("holddrill").executes((context) -> {
                    BirbAddonsClient.drillPosition = !BirbAddonsClient.drillPosition;
                    return 1;
                }))
        );

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
                dispatcher.register(ClientCommands.literal("toggleadrenaline").executes((context) -> {
                    AdrenalineBar.enabled = !AdrenalineBar.enabled;
                    return 1;
                }))
        );

    }

}
