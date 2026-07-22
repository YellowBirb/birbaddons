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

    public String command;
    public String icon;
    private Screen screen;
    private boolean isDragging = false;

    public InventoryButton(int x, int y, int width, int height, Component message, String command, String icon) {
        super(x, y, width, height, message);
        this.command = command;
        this.icon = icon;
    }

    public InventoryButton(int x, int y, int width, int height, String command, String icon) {
        this(x, y, width, height, Component.literal(""), command, icon);
    }

    public InventoryButton(int x, int y, String command, String icon) {
        this(x, y, 18, 18, command, icon);
    }

    public InventoryButton(int x, int y) {
        this(x, y, 18, 18, "", "");
    }

    public Screen getScreen() {
        if (this.screen == null) {
            findScreen();
        }
        return this.screen;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    private void findScreen() {
        this.screen = Minecraft.getInstance().screen;
    }

    @Override
    public void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "widget/custombutton"), getX(), getY(), getWidth(), getHeight());
        graphics.centeredText(getScreen().getFont(), icon, getX() + getWidth()/2, getY() + getHeight()/2 -getScreen().getFont().lineHeight/2, ARGB.white(1F));
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
            Minecraft.getInstance().player.connection.sendCommand(!command.isEmpty() && command.charAt(0) == '/' ? command.substring(1) : command);
        }
    }

    public boolean mouseOnPopup(MouseButtonEvent event, InventoryButtonEditScreen ibes) {
        boolean flag = false;
        for (GuiEventListener lis : ibes.children()) {
            if (lis instanceof InventoryButtonEditPopup ibep) {
                if (event.x() > ibep.getX() && event.x() < ibep.getX()+ibep.getHeight() && event.y() > ibep.getY() && event.y() < ibep.getY()+ibep.getHeight()) {
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
                        (event.x() + InventoryButtonEditPopup.width > ibes.width) ? (int) event.x() - InventoryButtonEditPopup.width : (int) event.x(),
                        (event.y() + InventoryButtonEditPopup.height > ibes.height) ? (int) event.y() - InventoryButtonEditPopup.height : (int) event.y(),
                        this, ibes));
            } else {
                isDragging = false;
            }
        }
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {}
}
