package yellowbirb.birbaddons.gui.mainmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

public class SettingsScreenButton extends AbstractWidget {

    public final Screen screen;

    public SettingsScreenButton(int x, int y, int width, int height, Screen screen) {
        super(x, y, width, height, Component.literal(""));
        this.screen = screen;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.fill(getX(), getY(), getRight(), getBottom(), ARGB.color(255, 150, 150, 150));
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        if (!this.isActive()) {
            return false;
        }
        if (this.isValidClickButton(event.buttonInfo()) && (this.isMouseOver(event.x(), event.y()))) {
            Minecraft.getInstance().setScreen(screen);
            return true;
        }
        return false;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {}
}
