package yellowbirb.birbaddons;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.network.chat.Component;
import yellowbirb.birbaddons.feature.impl.AdrenalineBar;
import yellowbirb.birbaddons.feature.impl.NoDrillSwinging;
import yellowbirb.birbaddons.render.RenderManager;

// TODO: subcommand system
public class Commands {

    public static void init() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("holddrill").executes((_) -> {
                    NoDrillSwinging.drillPosition = !NoDrillSwinging.drillPosition;
                    return 1;
                }))
        );

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("toggleadrenaline").executes((_) -> {
                    AdrenalineBar.enabled = !AdrenalineBar.enabled;
                    return 1;
                }))
        );

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("btclear").executes((context) -> {
                    RenderManager.clear();
                    context.getSource().getPlayer().sendSystemMessage(Component.literal("§3[BirbAddons] §aCleared all objects drawn in the world"));
                    return 1;
                }))
        );

    }

}
