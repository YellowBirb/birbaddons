package yellowbirb.birbaddons.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.NonNull;
import yellowbirb.birbaddons.BirbAddonsClient;
import yellowbirb.birbaddons.config.Config;

import java.util.ArrayList;
import java.util.List;

public class InventoryButtonEditScreen extends Screen {
    public InventoryButtonEditScreen() {
        super(Component.literal(""));
    }

    @Override
    protected void init() {
        for (InventoryButton button : BirbAddonsClient.getInstance().features.inventoryButtons.buttons.get()) {
            button.setScreen(this);
            addRenderableWidget(button);
        }
    }

    public <T extends GuiEventListener & Renderable & NarratableEntry> void add(T widget) {
        this.addRenderableWidget(widget);
    }

    @Override
    public boolean mouseClicked(@NonNull MouseButtonEvent event, boolean doubleClick) {
        if (super.mouseClicked(event, doubleClick)) return true;
        if (!clearPopups()) {
            BirbAddonsClient.getInstance().features.inventoryButtons.buttons.add(new InventoryButton((int) Math.round(event.x()), (int) Math.round(event.y())));
        }
        this.rebuildWidgets();
        return true;
    }

    public boolean clearPopups() {
        boolean flag = false;
        List<InventoryButtonEditPopup> del = new ArrayList<>();
        for (GuiEventListener widget : this.children()) {
            if (widget instanceof InventoryButtonEditPopup ibep) {
                del.add(ibep);
                flag = true;
            }
        }
        for (InventoryButtonEditPopup ibep2 : del) {
            ibep2.onClose();
            this.removeWidget(ibep2);
        }
        return flag;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("textures/gui/container/inventory.png"), (this.width - 176) / 2, (this.height - 166) / 2, 0.0F, 0.0F, 176, 166, 256, 256);
        graphics.text(getFont(), "test", 10, 10, ARGB.white(1.0F));
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }

    @Override
    public void onClose() {
        clearPopups();
        BirbAddonsClient.getInstance().features.inventoryButtons.buttons.syncRepresentation();
        Config.save();
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
