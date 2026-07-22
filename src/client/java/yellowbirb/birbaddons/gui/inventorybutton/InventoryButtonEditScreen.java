package yellowbirb.birbaddons.gui.inventorybutton;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
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

    @Override
    public <T extends GuiEventListener & Renderable & NarratableEntry> @NonNull T addRenderableWidget(@NonNull T widget) {
        return super.addRenderableWidget(widget);
    }

    @Override
    public void rebuildWidgets() {
        super.rebuildWidgets();
    }

    @Override
    public boolean mouseClicked(@NonNull MouseButtonEvent event, boolean doubleClick) {
        List<GuiEventListener> children = getChildrenAt(event.x(), event.y());
        if (!children.isEmpty()) {
            for (GuiEventListener widget : children) {
                if (widget.mouseClicked(event, doubleClick)) {
                    if (widget.shouldTakeFocusAfterInteraction()) {
                        this.setFocused(widget);
                    }
                    if (event.button() == 0) {
                        this.setDragging(true);
                    }
                    return true;
                }
            }
        }
        else {
            if (!clearPopups()) {
                BirbAddonsClient.getInstance().features.inventoryButtons.buttons.add(new InventoryButton((int) Math.round(event.x()), (int) Math.round(event.y())));
            }
            this.rebuildWidgets();
        }
        return true;
    }

    public List<GuiEventListener> getChildrenAt(double x, double y) {
        List<GuiEventListener> res = new ArrayList<>();
        for (GuiEventListener guiEventListener : this.children().reversed()) {
            if (guiEventListener.isMouseOver(x, y)) {
                res.add(guiEventListener);
            }
        }
        return res;
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
            ibep2.saveButton();
            for (AbstractWidget widget : ibep2.widgets) {
                this.removeWidget(widget);
            }
            this.removeWidget(ibep2);
        }
        return flag;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, Identifier.withDefaultNamespace("textures/gui/container/inventory.png"), (this.width - 176) / 2, (this.height - 166) / 2, 0.0F, 0.0F, 176, 166, 256, 256);
        graphics.text(getFont(), "Click anywhere to create a button", 10, 10, ARGB.white(1.0F));
        super.extractRenderState(graphics, mouseX, mouseY, a);
    }

    @Override
    public void onClose() {
        for (GuiEventListener widget : this.children()) {
            if (widget instanceof InventoryButtonEditPopup ibep) {
                ibep.saveButton();
            }
        }
        BirbAddonsClient.getInstance().features.inventoryButtons.buttons.syncRepresentation();
        Config.save();
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

}
