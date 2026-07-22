package yellowbirb.birbaddons.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class InventoryButtonEditPopup extends AbstractWidget {

    public static final int width = 100;
    public static final int height = 50;
    private final InventoryButton button;
    private final Screen screen;

    public EditBox commandBox;
    public EditBox iconBox;

    public InventoryButtonEditPopup(int x, int y, InventoryButton button, InventoryButtonEditScreen screen) {
        super(x, y, width, height, Component.literal(""));
        this.button = button;
        this.screen = screen;
        this.commandBox = new EditBox(this.screen.getFont(), getX() + 5, getY() + this.screen.getFont().lineHeight*2 + 10 , width-10, this.screen.getFont().lineHeight*2, Component.literal("lol"));
        this.iconBox = new EditBox(this.screen.getFont(), getX() + 5, getY() + 5, width-10, this.screen.getFont().lineHeight*2, Component.literal("lol2"));
        commandBox.setValue(button.command);
        iconBox.setValue(button.icon);
        screen.add(commandBox);
        screen.add(iconBox);
    }

    public void onClose() {
        button.command = commandBox.getValue();
        button.icon = iconBox.getValue();
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.fill(getX(), getY(), getX()+getWidth(), getY()+getHeight(), 0xFF0000AA);
        commandBox.extractWidgetRenderState(graphics, mouseX, mouseY, a);
        iconBox.extractWidgetRenderState(graphics, mouseX, mouseY, a);
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {

    }
}
