package yellowbirb.birbaddons.feature;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
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
        enable(Minecraft.getInstance().player);
    }

    public void enable(LocalPlayer player) {
        Utils.displayMessage(player, "enabled " + ID);
        enabled.set(true);
    }

    public void disable() {
        disable(Minecraft.getInstance().player);
    }

    public void disable(LocalPlayer player) {
        Utils.displayMessage(player, "disabled " + ID);
        enabled.set(false);
    }

    public boolean enabled() {
        return enabled.get();
    }

    public void toggle(){
        toggle(Minecraft.getInstance().player);
    }

    public void toggle(LocalPlayer player){
        if (enabled()) {
            disable(player);
        } else {
            enable(player);
        }
    }

    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        LiteralArgumentBuilder<FabricClientCommandSource> command = ClientCommands.literal(ID.toLowerCase());
        LiteralArgumentBuilder<FabricClientCommandSource> enableCommand = ClientCommands.literal("enable").executes((ctx) -> {
            enable(ctx.getSource().getPlayer());
            return 1;
        });
        LiteralArgumentBuilder<FabricClientCommandSource> disableCommand = ClientCommands.literal("disable").executes((ctx) -> {
            disable(ctx.getSource().getPlayer());
            return 1;
        });
        LiteralArgumentBuilder<FabricClientCommandSource> toggleCommand = ClientCommands.literal("toggle").executes((ctx) -> {
            toggle(ctx.getSource().getPlayer());
            return 1;
        });

        command.then(enableCommand).then(disableCommand).then(toggleCommand);

        return command;
    }


}
