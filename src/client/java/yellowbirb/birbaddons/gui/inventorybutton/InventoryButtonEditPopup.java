package yellowbirb.birbaddons.gui.inventorybutton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.NonNull;
import yellowbirb.birbaddons.BirbAddonsClient;

import java.util.ArrayList;
import java.util.List;

public class InventoryButtonEditPopup extends AbstractWidget {

    public static final int width = 150;
    public static int height = 100;
    public static final int margin = 5;
    public static final int smallMargin = 2;
    private final InventoryButton button;
    private final InventoryButtonEditScreen screen;

    public List<AbstractWidget> widgets;
    public List<Renderable> renderables;

    public Renderable background;
    public Renderable icon;
    public Renderable iconText;
    public DelButton delButton;
    public IBEPUpdatingEditBox iconBox;
    public Renderable commandText;
    public Renderable slash;
    public IBEPUpdatingEditBox commandBox;
    public Renderable widthText;
    public IBEPUpdatingEditBox widthBox;
    public Renderable heightText;
    public IBEPUpdatingEditBox heightBox;


    public InventoryButtonEditPopup(int x, int y, InventoryButton button, InventoryButtonEditScreen screen) {
        super(x, y, width, height, Component.literal(""));
        this.button = button;
        this.screen = screen;
        this.renderables = new ArrayList<>();
        this.widgets = new ArrayList<>();

        Font font = this.screen.getFont();
        int editBoxHeight = Math.round(this.screen.getFont().lineHeight*1.5F);

        background = (graphics, _, _, _) -> graphics.blitSprite(RenderPipelines.GUI_TEXTURED, Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "widget/custombutton"), getX(), getY(), getWidth(), getHeight());

        int iconX0 = getX()+margin;
        int iconY0 = getY()+margin;
        int iconX1 = getX()+margin+18;
        int iconY1 = getY()+margin+18;
        icon = (graphics, _, _, _) -> graphics.fill(iconX0, iconY0, iconX1, iconY1, 0xFF00FF00);

        iconText = (graphics, _, _, _) -> graphics.text(font, "Icon", getX() + margin, iconY1 - font.lineHeight, ARGB.white(1.0F));

        int delButtonX = getX()+width-16-margin;
        int delButtonY = getY() + margin;
        int delButtonWidth = 16;
        int delButtonHeight = 16;
        this.delButton = new DelButton(delButtonX, delButtonY, delButtonWidth, delButtonHeight);

        int iconBoxX = getX() + margin;
        int iconBoxY = iconY1 + smallMargin;
        int iconBoxWidth = width-2*margin;
        int iconBoxHeight = editBoxHeight;
        this.iconBox = new IBEPUpdatingEditBox(font, iconBoxX, iconBoxY, iconBoxWidth, iconBoxHeight, Component.literal(""), this);

        int commandTextX = this.getX() + margin;
        int commandTextY = iconBoxY + iconBoxHeight + margin;
        commandText = (graphics, _, _, _) -> graphics.text(font, "Command", commandTextX, commandTextY, ARGB.white(1.0F));

        int slashX = this.getX() + margin + smallMargin;
        int slashY = commandTextY + font.lineHeight + smallMargin + (editBoxHeight/2 - font.lineHeight/2);
        slash = (graphics, _, _, _) -> graphics.text(font, "/", slashX, slashY, ARGB.white(1.0F));

        int commandBoxX = slashX + font.width("/") + smallMargin;
        int commandBoxY = commandTextY + font.lineHeight + smallMargin;
        int commandBoxWidth = width - (commandBoxX-getX()) - margin;
        int commandBoxHeight = editBoxHeight;
        this.commandBox = new IBEPUpdatingEditBox(font, commandBoxX,  commandBoxY, commandBoxWidth, commandBoxHeight, Component.literal(""), this);

        int widthTextX = this.getX() + margin + smallMargin;
        int widthTextY = commandBoxY + commandBoxHeight + margin + (editBoxHeight/2 - font.lineHeight/2);
        widthText = (graphics, _, _, _) -> graphics.text(font, "Width", widthTextX, widthTextY, ARGB.white(1.0F));

        int widthBoxX = widthTextX + font.width("Width") + smallMargin;
        int widthBoxY = commandBoxY + commandBoxHeight + margin;
        int widthBoxWidth = 30;
        int widthBoxHeight = editBoxHeight;
        this.widthBox = new IBEPUpdatingEditBox(font, widthBoxX,  widthBoxY, widthBoxWidth, widthBoxHeight, Component.literal(""), this);

        int heightTextX = widthBoxX + widthBoxWidth + margin + smallMargin;
        int heightTextY = widthTextY;
        heightText = (graphics, _, _, _) -> graphics.text(font, "Height", heightTextX, heightTextY, ARGB.white(1.0F));

        int heightBoxX = heightTextX + font.width("Height") + smallMargin;
        int heightBoxY = widthBoxY;
        int heightBoxWidth = 30;
        int heightBoxHeight = editBoxHeight;
        this.heightBox = new IBEPUpdatingEditBox(font, heightBoxX,  heightBoxY, heightBoxWidth, heightBoxHeight, Component.literal(""), this);

        height = (heightBoxY + heightBoxHeight + margin) - getY();
        setHeight(height);

        init();
    }

    public void init() {

        renderables.add(background);
        // TODO: icon
        // renderables.add(icon);
        renderables.add(iconText);
        renderables.add(commandText);
        renderables.add(slash);
        renderables.add(widthText);
        renderables.add(heightText);

        commandBox.setValue(button.command);
        iconBox.setValue(button.icon);
        widthBox.setValue(String.valueOf(button.getWidth()));
        heightBox.setValue(String.valueOf(button.getHeight()));

        widgets.add(commandBox);
        widgets.add(iconBox);
        widgets.add(delButton);
        widgets.add(widthBox);
        widgets.add(heightBox);

        for (AbstractWidget lis : widgets) {
            screen.addRenderableWidget(lis);
        }


    }

    public void saveButton() {
        button.command = commandBox.getValue();
        button.icon = iconBox.getValue();
        try {
            button.setWidth(Integer.parseInt(widthBox.getValue()));
            button.setHeight(Integer.parseInt(heightBox.getValue()));
        } catch (NumberFormatException _) {
        }
    }

    @Override
    protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        for (Renderable ren : renderables) {
            ren.extractRenderState(graphics, mouseX, mouseY, a);
        }
        for (AbstractWidget lis : widgets) {
            lis.extractRenderState(graphics, mouseX, mouseY, a);
        }
    }

    @Override
    public boolean mouseClicked(@NonNull MouseButtonEvent event, boolean doubleClick) {
        if (!this.isActive()) {
            return false;
        }
        if (this.isValidClickButton(event.buttonInfo()) && this.isMouseOver(event.x(), event.y())) {
            this.onClick(event, doubleClick);
            //return true;
        }
        return false;
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {

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
                BirbAddonsClient.getInstance().features.inventoryButtons.buttons.remove(button);
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
