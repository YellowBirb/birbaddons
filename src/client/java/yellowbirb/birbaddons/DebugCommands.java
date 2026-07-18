package yellowbirb.birbaddons;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import yellowbirb.birbaddons.event.ReceiveGameMessageEvent;

public class DebugCommands {

    public static void init() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("startAdrenalin").executes((_) -> {
                    BirbAddonsClient.getInstance().features.adrenalineBar.adrenalineUsed("", 5000, 15000);
                    return 1;
                }))
        );

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("expireAdrenalin").executes((_) -> {
                    BirbAddonsClient.getInstance().features.adrenalineBar.expired();
                    return 1;
                }))
        );

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("rechargeAdrenalin").executes((_) -> {
                    BirbAddonsClient.getInstance().features.adrenalineBar.recharged();
                    return 1;
                }))
        );

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("bttesttheodolite")
                        .then(ClientCommands.argument("deltay", IntegerArgumentType.integer())
                                .then(ClientCommands.argument("angle", IntegerArgumentType.integer())
                                        .executes(DebugCommands::executeTestTheodolite))))
        );

    }

    private static int executeTestTheodolite(CommandContext<FabricClientCommandSource> context) {
        int deltay = IntegerArgumentType.getInteger(context, "deltay");
        int angle = IntegerArgumentType.getInteger(context, "angle");
        if (deltay == 0) {
            context.getSource().getPlayer().sendSystemMessage(Component.literal("§3[BirbAddons] §rYou are at the exact height!"));
            ReceiveGameMessageEvent.receiveMessage(Component.literal("You are at the exact height!"));
        } else if (deltay > 0) {
            context.getSource().getPlayer().sendSystemMessage(Component.literal("§3[BirbAddons] §rThe target is around " + deltay + " blocks above, at a " + angle + " degrees angle!"));
            ReceiveGameMessageEvent.receiveMessage(Component.literal("The target is around " + deltay + " blocks above, at a " + angle + " degrees angle!"));
        } else {
            context.getSource().getPlayer().sendSystemMessage(Component.literal("§3[BirbAddons] §rThe target is around " + (-deltay) + " blocks below, at a " + angle + " degrees angle!"));
            ReceiveGameMessageEvent.receiveMessage(Component.literal("The target is around " + (-deltay) + " blocks below, at a " + angle + " degrees angle!"));
        }

        return 1;
    }

}
