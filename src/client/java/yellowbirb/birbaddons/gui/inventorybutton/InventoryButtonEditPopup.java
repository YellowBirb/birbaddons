package yellowbirb.birbaddons.gui.inventorybutton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.NonNull;
import yellowbirb.birbaddons.BirbAddonsClient;
import yellowbirb.birbaddons.gui.AbstractSubScreenWidget;

import java.util.function.Consumer;

public class InventoryButtonEditPopup extends AbstractSubScreenWidget {

    public static final int initWidth = 150;
    public static final int initHeight = 100;
    public static final int margin = 5;
    public static final int smallMargin = 2;
    private final InventoryButton button;
    private final InventoryButtonEditScreen screen;

    public Renderable icon;
    public Renderable iconText;
    public DelButton delButton;
    public EditBox iconBox;
    public Renderable commandText;
    public Renderable slash;
    public EditBox commandBox;
    public Renderable widthText;
    public EditBox widthBox;
    public Renderable heightText;
    public EditBox heightBox;


    public InventoryButtonEditPopup(int x, int y, InventoryButton button, InventoryButtonEditScreen screen, Consumer<GuiEventListener> removeWidgetConsumer) {
        this.button = button;
        this.screen = screen;
        super(x, y, initWidth, initHeight, removeWidgetConsumer);
    }

    protected void init() {

        Font font = Minecraft.getInstance().font;
        int editBoxHeight = Math.round(this.screen.getFont().lineHeight*1.5F);

        // Renderables are not moved when the popup is dragged, so they are drawn at their initial position plus how far the popup has moved
        final int originX = getX();
        final int originY = getY();

        int iconX0 = getX()+margin;
        int iconY0 = getY()+margin;
        int iconX1 = getX()+margin+18;
        int iconY1 = getY()+margin+18;
        icon = (graphics, _, _, _) -> graphics.fill(iconX0 + getX() - originX, iconY0 + getY() - originY, iconX1 + getX() - originX, iconY1 + getY() - originY, 0xFF00FF00);

        iconText = (graphics, _, _, _) -> graphics.text(font, "Icon", getX() + margin, iconY1 + getY() - originY - font.lineHeight, ARGB.white(1.0F));

        int delButtonX = getX()+ getWidth() -16-margin;
        int delButtonY = getY() + margin;
        int delButtonWidth = 16;
        int delButtonHeight = 16;
        this.delButton = new DelButton(delButtonX, delButtonY, delButtonWidth, delButtonHeight);

        int iconBoxX = getX() + margin;
        int iconBoxY = iconY1 + smallMargin;
        int iconBoxWidth = getWidth() -2*margin;
        int iconBoxHeight = editBoxHeight;
        this.iconBox = new EditBox(font, iconBoxX, iconBoxY, iconBoxWidth, iconBoxHeight, Component.literal(""));

        int commandTextX = this.getX() + margin;
        int commandTextY = iconBoxY + iconBoxHeight + margin;
        commandText = (graphics, _, _, _) -> graphics.text(font, "Command", commandTextX + getX() - originX, commandTextY + getY() - originY, ARGB.white(1.0F));

        int slashX = this.getX() + margin + smallMargin;
        int slashY = commandTextY + font.lineHeight + smallMargin + (editBoxHeight/2 - font.lineHeight/2);
        slash = (graphics, _, _, _) -> graphics.text(font, "/", slashX + getX() - originX, slashY + getY() - originY, ARGB.white(1.0F));

        int commandBoxX = slashX + font.width("/") + smallMargin;
        int commandBoxY = commandTextY + font.lineHeight + smallMargin;
        int commandBoxWidth = getWidth() - (commandBoxX-getX()) - margin;
        int commandBoxHeight = editBoxHeight;
        this.commandBox = new EditBox(font, commandBoxX,  commandBoxY, commandBoxWidth, commandBoxHeight, Component.literal(""));

        int widthTextX = this.getX() + margin + smallMargin;
        int widthTextY = commandBoxY + commandBoxHeight + margin + (editBoxHeight/2 - font.lineHeight/2);
        widthText = (graphics, _, _, _) -> graphics.text(font, "Width", widthTextX + getX() - originX, widthTextY + getY() - originY, ARGB.white(1.0F));

        int widthBoxX = widthTextX + font.width("Width") + smallMargin;
        int widthBoxY = commandBoxY + commandBoxHeight + margin;
        int widthBoxWidth = 30;
        int widthBoxHeight = editBoxHeight;
        this.widthBox = new EditBox(font, widthBoxX,  widthBoxY, widthBoxWidth, widthBoxHeight, Component.literal(""));

        int heightTextX = widthBoxX + widthBoxWidth + margin + smallMargin;
        int heightTextY = widthTextY;
        heightText = (graphics, _, _, _) -> graphics.text(font, "Height", heightTextX + getX() - originX, heightTextY + getY() - originY, ARGB.white(1.0F));

        int heightBoxX = heightTextX + font.width("Height") + smallMargin;
        int heightBoxY = widthBoxY;
        int heightBoxWidth = 30;
        int heightBoxHeight = editBoxHeight;
        this.heightBox = new EditBox(font, heightBoxX,  heightBoxY, heightBoxWidth, heightBoxHeight, Component.literal(""));

        int finalHeight = (heightBoxY + heightBoxHeight + margin) - getY();
        setHeight(finalHeight);

        // TODO: icon
        // renderables.add(icon);
        addRenderableOnly(iconText);
        addRenderableOnly(commandText);
        addRenderableOnly(slash);
        addRenderableOnly(widthText);
        addRenderableOnly(heightText);

        commandBox.setValue(button.data().command);
        commandBox.setResponder(_ -> button.data().command = commandBox.getValue());
        iconBox.setValue(button.data().icon);
        iconBox.setResponder(_ -> button.data().icon = iconBox.getValue());
        widthBox.setValue(String.valueOf(button.getWidth()));
        widthBox.setResponder(_ -> {
            try {
                button.setWidth(Integer.parseInt(widthBox.getValue()));
            } catch (NumberFormatException _) {
            }
        });
        heightBox.setValue(String.valueOf(button.getHeight()));
        heightBox.setResponder(_ -> {
            try {
                button.setHeight(Integer.parseInt(heightBox.getValue()));
            } catch (NumberFormatException _) {
            }
        });

        addRenderableWidget(commandBox);
        addRenderableWidget(iconBox);
        addRenderableWidget(delButton);
        addRenderableWidget(widthBox);
        addRenderableWidget(heightBox);
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "widget/custombutton"), getX(), getY(), getWidth(), getHeight());
    }

    private class DelButton extends AbstractWidget {

        public DelButton(int x, int y, int width, int height) {
            super(x, y, width, height, Component.literal(""));
        }

        @Override
        public boolean mouseClicked(@NonNull MouseButtonEvent event, boolean doubleClick) {
            if (!this.isActive()) {
                return false;
            }
            if (this.isValidClickButton(event.buttonInfo()) && (this.isMouseOver(event.x(), event.y()))) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                BirbAddonsClient.getInstance().features.inventoryButtons.buttons.remove(button.data());
                screen.rebuildWidgets();
                return true;
            }
            return false;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
            graphics.blit(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/gui/sprites/widget/delete.png"),
                    this.getX(), this.getY(), 0, 0, 16, 16, 16, 16, 16, 16);
        }

        @Override
        protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {}
    }
}
