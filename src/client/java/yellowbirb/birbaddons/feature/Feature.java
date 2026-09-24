package yellowbirb.birbaddons.feature;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import yellowbirb.birbaddons.config.ConfigBoolean;
import yellowbirb.birbaddons.util.Utils;

public abstract class Feature {

    public String ID;
    protected ConfigBoolean enabled;

    public Feature(String id) {
        this.ID = id;
        this.enabled = new ConfigBoolean(id, "enabled", false);
    }

    public void enable() {
        enabled.set(true);
    }

    public void disable() {
        enabled.set(false);
    }

    public boolean enabled() {
        return enabled.get();
    }

    public boolean toggle(){
        if (enabled()) {
            disable();
            return false;
        } else {
            enable();
            return true;
        }
    }

    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        LiteralArgumentBuilder<FabricClientCommandSource> command = ClientCommands.literal(ID.toLowerCase());
        LiteralArgumentBuilder<FabricClientCommandSource> enableCommand = ClientCommands.literal("enable").executes((ctx) -> {
            enable();
            Utils.displayMessage(ctx.getSource().getPlayer(), "enabled " + ID);
            return 1;
        });
        LiteralArgumentBuilder<FabricClientCommandSource> disableCommand = ClientCommands.literal("disable").executes((ctx) -> {
            disable();
            Utils.displayMessage(ctx.getSource().getPlayer(), "disabled " + ID);
            return 1;
        });
        LiteralArgumentBuilder<FabricClientCommandSource> toggleCommand = ClientCommands.literal("toggle").executes((ctx) -> {
            if (toggle()) {
                Utils.displayMessage(ctx.getSource().getPlayer(), "enabled " + ID);
            } else {
                Utils.displayMessage(ctx.getSource().getPlayer(), "disabled " + ID);
            }
            return 1;
        });

        command.then(enableCommand).then(disableCommand).then(toggleCommand);

        return command;
    }


}
