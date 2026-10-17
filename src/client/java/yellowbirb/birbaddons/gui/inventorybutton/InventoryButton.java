package yellowbirb.birbaddons.gui.inventorybutton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.NonNull;
import yellowbirb.birbaddons.BirbAddonsClient;
import yellowbirb.birbaddons.gui.AbstractDraggableWidget;

public class InventoryButton extends AbstractDraggableWidget {

    private final InventoryButtonData data;
    private final Screen screen;
    private boolean isDragging = false;

    public InventoryButton(InventoryButtonData data, Screen screen) {
        this.data = data;
        this.screen = screen;
        super(data.x, data.y, data.width, data.height, Component.literal(""));
    }

    public InventoryButtonData data() {
        return data;
    }

    public Screen getScreen() {
        return this.screen;
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        if (data != null) data.x = x;
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        if (data != null) data.y = y;
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        if (data != null) data.width = width;
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        if (data != null) data.height = height;
    }

    @Override
    public void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "widget/custombutton"), getX(), getY(), getWidth(), getHeight());
        graphics.centeredText(getScreen().getFont(), data.icon, getX() + getWidth()/2, getY() + getHeight()/2 -getScreen().getFont().lineHeight/2, ARGB.white(1F));
    }

    protected void onDrag(@NonNull MouseButtonEvent event, double dx, double dy) {
        if (getScreen() instanceof InventoryButtonEditScreen) {
            isDragging = true;
            super.onDrag(event, dx, dy);
        }
    }

    public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
        if (getScreen() instanceof InventoryButtonEditScreen ibes) {
            ibes.clearPopups();
        } else {
            Minecraft.getInstance().player.connection.sendCommand(!data.command.isEmpty() && data.command.charAt(0) == '/' ? data.command.substring(1) : data.command);
        }
    }

    public boolean mouseOnPopup(MouseButtonEvent event, InventoryButtonEditScreen ibes) {
        boolean flag = false;
        for (GuiEventListener lis : ibes.children()) {
            if (lis instanceof InventoryButtonEditPopup ibep) {
                if (event.x() > ibep.getX() && event.x() < ibep.getX()+ibep.getWidth() && event.y() > ibep.getY() && event.y() < ibep.getY()+ibep.getHeight()) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    @Override
    public boolean mouseClicked(@NonNull MouseButtonEvent event, boolean doubleClick) {
        if (getScreen() instanceof InventoryButtonEditScreen ibes) {
            if (!this.isActive()) {
                return false;
            }
            if (this.isValidClickButton(event.buttonInfo()) && this.isMouseOver(event.x(), event.y())) {
                if (!mouseOnPopup(event, ibes)) {
                    this.onClick(event, doubleClick);
                    return true;
                }
            }
            return false;
        }
        else {
            return super.mouseClicked(event, doubleClick);
        }
    }

    public void onRelease(@NonNull MouseButtonEvent event) {
        super.onRelease(event);
        if (getScreen() instanceof InventoryButtonEditScreen ibes) {
            if (!isDragging && !mouseOnPopup(event, ibes)) {
                ibes.addRenderableWidget(new InventoryButtonEditPopup(
                        (event.x() + InventoryButtonEditPopup.initWidth > ibes.width) ? (int) event.x() - InventoryButtonEditPopup.initWidth : (int) event.x(),
                        (event.y() + InventoryButtonEditPopup.initHeight > ibes.height) ? (int) event.y() - InventoryButtonEditPopup.initHeight : (int) event.y(),
                        this, ibes, ibes::removeWidget));
            } else {
                isDragging = false;
            }
        }
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {}
}
