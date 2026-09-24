package yellowbirb.birbaddons.gui.mainmenu;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import org.jspecify.annotations.NonNull;
import yellowbirb.birbaddons.gui.AbstractSubScreenWidget;

import java.util.function.Consumer;

public class FeatureSettingsPopup extends AbstractSubScreenWidget {
    public FeatureSettingsPopup(int x, int y, int width, int height, Consumer<AbstractWidget> addWidgetConsumer, Consumer<AbstractWidget> removeWidgetConsumer, Consumer<Renderable> addRenderableConsumer, Consumer<Renderable> removeRenderableConsumer) {
        super(x, y, width, height, addWidgetConsumer, removeWidgetConsumer, addRenderableConsumer, removeRenderableConsumer);
    }

    @Override
    protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

    }
}
