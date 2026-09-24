package yellowbirb.birbaddons.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.Consumer;

public abstract class AbstractSubScreenWidget extends AbstractWidget {

    // TODO: dont put children in parent
    // TODO: look into ContainerEventHandler

    // Screen has <T extends GuiEventListener && Renderable> instead of AbstractWidget, how do in Consumer??
    private final Consumer<AbstractWidget> addWidgetConsumer;
    private final Consumer<AbstractWidget> removeWidgetConsumer;
    private final Consumer<Renderable> addRenderableConsumer;
    private final Consumer<Renderable> removeRenderableConsumer;
    private final List<AbstractWidget> children = Lists.newArrayList();
    private final List<Renderable> renderables = Lists.newArrayList();

    public AbstractSubScreenWidget(int x, int y, int width, int height,
                                   Consumer<AbstractWidget> addWidgetConsumer, Consumer<AbstractWidget> removeWidgetConsumer,
                                   Consumer<Renderable> addRenderableConsumer, Consumer<Renderable> removeRenderableConsumer) {
        super(x, y, width, height, Component.literal(""));
        this.addWidgetConsumer = addWidgetConsumer;
        this.removeWidgetConsumer = removeWidgetConsumer;
        this.addRenderableConsumer = addRenderableConsumer;
        this.removeRenderableConsumer = removeRenderableConsumer;
    }

    protected AbstractWidget addRenderableWidget(AbstractWidget widget) {
        this.renderables.add(widget);
        this.children.add(widget);
        this.addWidgetConsumer.accept(widget);
        return widget;
    }

    protected boolean removeRenderableWidget(AbstractWidget widget) {
        this.renderables.remove(widget);
        this.children.remove(widget);
        this.removeWidgetConsumer.accept(widget);
        return true;
    }

    protected Renderable addRenderableOnly(Renderable renderable) {
        this.renderables.add(renderable);
        this.addRenderableConsumer.accept(renderable);
        return renderable;
    }

    protected boolean removeRenderableOnly(Renderable renderable) {
        this.renderables.remove(renderable);
        this.removeRenderableConsumer.accept(renderable);
        return true;
    }

    protected void removeSelf() {
        clearWidgets();
        removeWidgetConsumer.accept(this);
    }

    protected void clearWidgets() {
        for (Renderable r : renderables) {
            removeRenderableConsumer.accept(r);
        }
        renderables.clear();
        children.forEach(removeWidgetConsumer);
        for (AbstractWidget w : children) {
            removeWidgetConsumer.accept(w);
        }
        children.clear();
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {}
}
