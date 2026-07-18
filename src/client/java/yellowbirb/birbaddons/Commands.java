package yellowbirb.birbaddons;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.minecraft.network.chat.Component;
import yellowbirb.birbaddons.render.RenderManager;

// TODO: subcommand system
public class Commands {

    public static void init() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("holddrill").executes((_) -> {
                    BirbAddonsClient.getInstance().features.doomDrill.toggle();
                    return 1;
                }))
        );

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("toggleadrenaline").executes((_) -> {
                    BirbAddonsClient.getInstance().features.adrenalineBar.toggle();
                    return 1;
                }))
        );

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("toggletheodolite").executes((_) -> {
                    BirbAddonsClient.getInstance().features.theodolite.toggle();
                    return 1;
                }))
        );

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("btclear").executes((context) -> {
                    RenderManager.clear();
                    context.getSource().getPlayer().sendSystemMessage(Component.literal("§3[BirbAddons] §rCleared all objects drawn in the world"));
                    return 1;
                }))
        );

    }

}
