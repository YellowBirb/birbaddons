package yellowbirb.birbaddons.feature;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import yellowbirb.birbaddons.config.ConfigBoolean;

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

    public void toggle(){
        if (enabled()) {
            disable();
        } else {
            enable();
        }
    }

    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        LiteralArgumentBuilder<FabricClientCommandSource> command = ClientCommands.literal(ID.toLowerCase());
        LiteralArgumentBuilder<FabricClientCommandSource> enableCommand = ClientCommands.literal("enable").executes((_) -> {
            enable();
            return 1;
        });
        LiteralArgumentBuilder<FabricClientCommandSource> disableCommand = ClientCommands.literal("disable").executes((_) -> {
            disable();
            return 1;
        });
        LiteralArgumentBuilder<FabricClientCommandSource> toggleCommand = ClientCommands.literal("toggle").executes((_) -> {
            toggle();
            return 1;
        });

        command.then(enableCommand).then(disableCommand).then(toggleCommand);

        return command;
    }


}
