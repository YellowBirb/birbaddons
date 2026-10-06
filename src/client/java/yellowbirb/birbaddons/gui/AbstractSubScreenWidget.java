package yellowbirb.birbaddons.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractSubScreenWidget extends AbstractDraggableWidget implements ContainerEventHandler {

    private final Consumer<GuiEventListener> removeWidgetConsumer;
    private final List<GuiEventListener> children = Lists.newArrayList();
    private final List<Renderable> renderables = Lists.newArrayList();
    private @Nullable GuiEventListener focused;
    private boolean isDragging;

    public AbstractSubScreenWidget(int x, int y, int width, int height, Consumer<GuiEventListener> removeWidgetConsumer) {
        super(x, y, width, height, Component.literal(""));
        this.removeWidgetConsumer = removeWidgetConsumer;
        init();
    }

    protected void init(){}

    @Override
    protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        extractBackground(graphics, mouseX, mouseY, a);
        for (Renderable renderable : this.renderables) {
            renderable.extractRenderState(graphics, mouseX, mouseY, a);
        }
    }

    abstract public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a);

    protected void removeSelf() {
        clearWidgets();
        removeWidgetConsumer.accept(this);
    }

    protected <T extends GuiEventListener & Renderable> T addRenderableWidget(T widget) {
        this.renderables.add(widget);
        return this.addWidget(widget);
    }

    protected <T extends Renderable> T addRenderableOnly(T renderable) {
        this.renderables.add(renderable);
        return renderable;
    }

    protected <T extends GuiEventListener> T addWidget(T widget) {
        this.children.add(widget);
        return widget;
    }

    protected void removeWidget(GuiEventListener widget) {
        if (widget instanceof Renderable) {
            this.renderables.remove((Renderable) widget);
        }
        if (this.getFocused() == widget) {
            this.clearFocus();
        }
        this.children.remove(widget);
    }

    protected void clearWidgets() {
        renderables.clear();
        children.clear();
    }

    public void clearFocus() {
        ComponentPath componentPath = this.getCurrentFocusPath();
        if (componentPath != null) {
            componentPath.applyFocus(false);
        }
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {}

    @Override
    public @NonNull List<? extends GuiEventListener> children() {
        return children;
    }

    @Override
    public @NonNull Optional<GuiEventListener> getChildAt(double x, double y) {
        List<GuiEventListener> res = getChildrenAt(x, y);
        if (!res.isEmpty()) {
            return Optional.of(res.getFirst());
        } else {
            return Optional.empty();
        }
    }

    public List<GuiEventListener> getChildrenAt(double x, double y) {
        List<GuiEventListener> res = new ArrayList<>();
        for (GuiEventListener guiEventListener : this.children().reversed()) {
            if (!guiEventListener.isMouseOver(x, y)) continue;
            res.add(guiEventListener);
        }
        return res;
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        Optional<GuiEventListener> child = this.getChildAt(event.x(), event.y());
        if (child.isPresent()) {
            GuiEventListener widget = child.get();
            if (widget.mouseClicked(event, doubleClick) && widget.shouldTakeFocusAfterInteraction()) {
                this.setFocused(widget);

            }
        }
        if (event.button() == InputConstants.MOUSE_BUTTON_LEFT) {
            this.setDragging(true);
        }
        return true;
    }

    @Override
    public boolean mouseReleased(MouseButtonEvent event) {
        if (event.button() == InputConstants.MOUSE_BUTTON_LEFT && this.isDragging()) {
            this.setDragging(false);
            if (this.getFocused() != null) {
                return this.getFocused().mouseReleased(event);
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(@NonNull MouseButtonEvent event, double dx, double dy) {
        if (this.isDragging() && event.button() == InputConstants.MOUSE_BUTTON_LEFT) {
            if (getChildAt(event.x(), event.y()).isEmpty()) {
                this.onDrag(event, dx, dy);
                return true;
            }
            else if (this.getFocused() != null) {
                return this.getFocused().mouseDragged(event, dx, dy);
            }
        }
        return false;
    }

    @Override
    protected void onDrag(@NonNull MouseButtonEvent event, double dx, double dy) {
        int ndx = (int) Math.round(dx+dragOffsetX);
        int ndy = (int) Math.round(dy+dragOffsetY);
        dragOffsetX = dx + dragOffsetX - ndx;
        dragOffsetY = dy + dragOffsetY - ndy;

        setX(getX()+ndx);
        setY(getY()+ndy);

        for (GuiEventListener gui : children) {
            if (gui instanceof LayoutElement ele) {
                ele.setX(ele.getX()+ndx);
                ele.setY(ele.getY()+ndy);
            }
        }
    }

    @Override
    public boolean isDragging() {
        return this.isDragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.isDragging = dragging;
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
        return this.getChildAt(x, y).filter(child -> child.mouseScrolled(x, y, scrollX, scrollY)).isPresent();
    }

    @Override
    public boolean keyPressed(@NonNull KeyEvent event) {
        return this.getFocused() != null && this.getFocused().keyPressed(event);
    }

    @Override
    public boolean keyReleased(@NonNull KeyEvent event) {
        return this.getFocused() != null && this.getFocused().keyReleased(event);
    }

    @Override
    public boolean charTyped(@NonNull CharacterEvent event) {
        return this.getFocused() != null && this.getFocused().charTyped(event);
    }

    @Override
    public @Nullable GuiEventListener getFocused() {
        return this.focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener focused) {
        if (this.focused == focused) {
            return;
        }
        if (this.focused != null) {
            this.focused.setFocused(false);
        }
        if (focused != null) {
            focused.setFocused(true);
        }
        this.focused = focused;
    }
}
