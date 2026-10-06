package yellowbirb.birbaddons.feature.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.gui.components.events.GuiEventListener;
import yellowbirb.birbaddons.config.ConfigBoolean;
import yellowbirb.birbaddons.feature.Feature;
import yellowbirb.birbaddons.gui.mainmenu.FeatureSettingsPopup;

import java.util.function.Consumer;

public class NoSwing extends Feature {

    public ConfigBoolean onlyDrills;

    public NoSwing() {
        super("NoSwing", "No Swing");
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

    @Override
    public FeatureSettingsPopup getSettingsPopup(int x, int y, int width, int height, Consumer<GuiEventListener> removeWidgetConsumer) {
        return new FeatureSettingsPopup(x, y, width, height, removeWidgetConsumer, this.name){
            @Override
            protected void initSettings() {
                addSwitch("Only Drills", onlyDrills);
            }
        };
    }

}
