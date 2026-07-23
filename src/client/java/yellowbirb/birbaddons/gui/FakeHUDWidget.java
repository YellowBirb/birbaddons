package yellowbirb.birbaddons.gui;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.joml.Vector2d;
import org.jspecify.annotations.NonNull;
import yellowbirb.birbaddons.config.ConfigVec2d;

public abstract class FakeHUDWidget extends AbstractDraggableWidget {

    private final ConfigVec2d realValue;

    public FakeHUDWidget(int x, int y, int width, int height, ConfigVec2d realValue) {
        super(x, y, width, height, Component.literal(""));
        this.realValue = realValue;
    }

    @Override
    protected void onDrag(@NonNull MouseButtonEvent event, double dx, double dy) {
        super.onDrag(event, dx, dy);
        realValue.set(new Vector2d(getX(), getY()));
    }

    abstract protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a);

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {}
}
