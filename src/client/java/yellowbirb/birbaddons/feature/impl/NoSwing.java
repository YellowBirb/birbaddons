package yellowbirb.birbaddons.feature.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import yellowbirb.birbaddons.config.ConfigBoolean;
import yellowbirb.birbaddons.feature.Feature;

public class NoSwing extends Feature {

    public ConfigBoolean onlyDrills;

    public NoSwing() {
        super("NoSwing");
        onlyDrills = new ConfigBoolean(ID, "onlyDrills", true);
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        LiteralArgumentBuilder<FabricClientCommandSource> command = super.getCommand();

        LiteralArgumentBuilder<FabricClientCommandSource> set = ClientCommands.literal("set");
        set.then(onlyDrills.getCommand());
        command.then(set);

        return command;
    }

}
