package yellowbirb.birbaddons;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import yellowbirb.birbaddons.gui.mainmenu.MenuScreen;

public class Command {

    private final LiteralArgumentBuilder<FabricClientCommandSource> builder;

    public Command() {
        builder = ClientCommands.literal("ba").executes((_) -> {
            Minecraft client = Minecraft.getInstance();
            client.schedule(()-> client.setScreen(new MenuScreen()));
            return 1;
        });
    }

    public LiteralArgumentBuilder<FabricClientCommandSource> getBuilder() {
        return builder;
    }

    public void registerCommand(LiteralArgumentBuilder<FabricClientCommandSource> command) {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
            var ba = dispatcher.register(command);
            dispatcher.register(ClientCommands.literal("birbaddons").executes((_) -> {
                Minecraft client = Minecraft.getInstance();
                client.schedule(()-> client.setScreen(new MenuScreen()));
                return 1;
            }).redirect(ba));
        });
    }

}
