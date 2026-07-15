package yellowbirb.birbaddons.client;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import yellowbirb.birbaddons.client.event.ReceiveGameMessageEvent;
import yellowbirb.birbaddons.client.hud.AdrenalineBar;

public class DebugCommands {

    public static void init() {

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) ->
                dispatcher.register(ClientCommands.literal("startAdrenalin").executes((_) -> {
                    AdrenalineBar.adrenalineUsed(40, 100);
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
        ReceiveGameMessageEvent.receiveMessage(Component.literal("The target is around " + deltay + " blocks above, at a " + angle + " degrees angle!"));
        if (deltay == 0) {
            context.getSource().getPlayer().sendSystemMessage(Component.literal("§3[Birb's Theodolite] §aYou are at the exact height!"));
        } else if (deltay > 0) {
            context.getSource().getPlayer().sendSystemMessage(Component.literal("§3[Birb's Theodolite] §aThe target is around " + deltay + " blocks above, at a " + angle + " degrees angle!"));
        } else {
            context.getSource().getPlayer().sendSystemMessage(Component.literal("§3[Birb's Theodolite] §aThe target is around " + (-deltay) + " blocks below, at a " + angle + " degrees angle!"));
        }

        return 1;
    }

}
