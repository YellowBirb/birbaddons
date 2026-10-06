package yellowbirb.birbaddons.feature;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.gui.components.events.GuiEventListener;
import yellowbirb.birbaddons.config.ConfigBoolean;
import yellowbirb.birbaddons.gui.mainmenu.FeatureSettingsPopup;
import yellowbirb.birbaddons.util.Utils;

import java.util.function.Consumer;

public abstract class Feature {

    public final String ID;
    public final String name;
    protected ConfigBoolean enabled;

    public Feature(String id, String name) {
        this.ID = id;
        this.name = name;
        this.enabled = new ConfigBoolean(id, "enabled", false);
    }

    public void enable() {
        enabled.set(true);
    }

    public void disable() {
        enabled.set(false);
        onDisable();
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

    public void onDisable() {}

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

    public FeatureSettingsPopup getSettingsPopup(int x, int y, int width, int height, Consumer<GuiEventListener> removeWidgetConsumer) {
        return null;
    }
}
