package yellowbirb.birbaddons.gui.mainmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.NonNull;
import yellowbirb.birbaddons.config.ConfigBoolean;

public class SettingsSwitch extends AbstractWidget  {

    ConfigBoolean bool;

    public SettingsSwitch(int x, int y, int width, int height, ConfigBoolean bool) {
        super(x, y, width, height, Component.literal(""));
        this.bool = bool;
    }

    @Override
    protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.fill(getX(), getY(), getRight(), getBottom(), bool.get() ? ARGB.color(255, 0, 255, 0) : ARGB.color(255, 255, 0, 9));
    }

    @Override
    public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
        bool.set(!bool.get());
    }

    @Override
    public boolean mouseClicked(@NonNull MouseButtonEvent event, boolean doubleClick) {
        if (!this.isActive()) {
            return false;
        }
        if (this.isValidClickButton(event.buttonInfo()) && (this.isMouseOver(event.x(), event.y()))) {
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            this.onClick(event, doubleClick);
            return true;
        }
        return false;
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {}
}
