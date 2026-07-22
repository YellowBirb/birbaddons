package yellowbirb.birbaddons.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public abstract class AbstractDraggableWidget extends AbstractWidget {

    protected double dragOffsetX = 0;
    protected double dragOffsetY = 0;

    public AbstractDraggableWidget(int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
    }

    abstract protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a);

    abstract protected void updateWidgetNarration(@NonNull NarrationElementOutput output);

    @Override
    protected void onDrag(@NonNull MouseButtonEvent event, double dx, double dy) {
        int ndx = (int) Math.round(dx+dragOffsetX);
        int ndy = (int) Math.round(dy+dragOffsetY);
        dragOffsetX = dx + dragOffsetX - ndx;
        dragOffsetY = dy + dragOffsetY - ndy;

        setX(getX()+ndx);
        setY(getY()+ndy);
    }

    @Override
    public void onRelease(@NonNull MouseButtonEvent event) {
        dragOffsetX = 0;
        dragOffsetY = 0;
    }
}
