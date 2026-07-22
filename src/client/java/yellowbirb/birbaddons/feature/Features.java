package yellowbirb.birbaddons.feature;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import yellowbirb.birbaddons.feature.impl.*;

import java.util.List;

public class Features {

    public final AdrenalineBar adrenalineBar;
    public final ChatTabs chatTabs;
    public final DoomDrill doomDrill;
    public final InventoryButtons inventoryButtons;
    public final NoSwing noSwing;
    public final Theodolite theodolite;

    public List<Feature> featureList;

    public Features() {
        adrenalineBar = new AdrenalineBar();
        chatTabs = new ChatTabs();
        doomDrill = new DoomDrill();
        inventoryButtons = new InventoryButtons();
        noSwing = new NoSwing();
        theodolite = new Theodolite();

        featureList = List.of(adrenalineBar, chatTabs, doomDrill, inventoryButtons, noSwing, theodolite);
    }

    public void buildCommands(LiteralArgumentBuilder<FabricClientCommandSource> commandBuilder) {

        LiteralArgumentBuilder<FabricClientCommandSource> enable = ClientCommands.literal("enable");
        LiteralArgumentBuilder<FabricClientCommandSource> disable = ClientCommands.literal("disable");
        LiteralArgumentBuilder<FabricClientCommandSource> toggle = ClientCommands.literal("toggle");

        for (Feature f : featureList) {
            commandBuilder.then(f.getCommand());
            enable.then(ClientCommands.literal(f.ID.toLowerCase()).executes((_) -> {
                f.enable();
                return 1;
            }));
            disable.then(ClientCommands.literal(f.ID.toLowerCase()).executes((_) -> {
                f.disable();
                return 1;
            }));
            disable.then(ClientCommands.literal(f.ID.toLowerCase()).executes((_) -> {
                f.toggle();
                return 1;
            }));
        }

        commandBuilder.then(enable);
        commandBuilder.then(disable);
        commandBuilder.then(toggle);

    }

}
