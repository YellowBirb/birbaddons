package yellowbirb.birbaddons.feature.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import yellowbirb.birbaddons.config.ConfigInventoryButtonList;
import yellowbirb.birbaddons.feature.Feature;
import yellowbirb.birbaddons.gui.inventorybutton.InventoryButtonEditScreen;
import yellowbirb.birbaddons.gui.mainmenu.FeatureSettingsPopup;

import java.util.List;
import java.util.function.Consumer;

public class InventoryButtons extends Feature {

    public ConfigInventoryButtonList buttons;

    public InventoryButtons() {
        super("InventoryButtons", "Inventory Buttons");
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

    @Override
    public FeatureSettingsPopup getSettingsPopup(int x, int y, int width, int height, Consumer<GuiEventListener> removeWidgetConsumer) {
        return new FeatureSettingsPopup(x, y, width, height, removeWidgetConsumer, this.name){
            @Override
            protected void initSettings() {
                addScreenButton("Open Edit Screen", new InventoryButtonEditScreen());
            }
        };
    }

    // TODO: icon
    // TODO: make positions work with dynamic screen dimensions
    // TODO: Editor show symmetry/parallel lines while dragging for satisfying arranging
}