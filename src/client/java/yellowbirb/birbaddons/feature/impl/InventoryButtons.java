package yellowbirb.birbaddons.feature.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import yellowbirb.birbaddons.config.ConfigInventoryButtonList;
import yellowbirb.birbaddons.feature.Feature;
import yellowbirb.birbaddons.gui.inventorybutton.InventoryButtonEditScreen;

import java.util.List;

public class InventoryButtons extends Feature {

    public ConfigInventoryButtonList buttons;

    public InventoryButtons() {
        super("InventoryButtons");
        buttons = new ConfigInventoryButtonList(ID, "list", List.of());
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        return super.getCommand().executes((_)->{
            Minecraft client = Minecraft.getInstance();
            client.schedule(() -> client.setScreen(new InventoryButtonEditScreen()));
            return 1;
        });
    }

    // TODO: icon
    // TODO: make positions work with dynamic screen dimensions
    // TODO: Editor show symmetry/parallel lines while dragging for satisfying arranging
}