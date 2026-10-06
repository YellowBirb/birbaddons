package yellowbirb.birbaddons.gui.mainmenu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.NonNull;
import yellowbirb.birbaddons.config.ConfigBoolean;
import yellowbirb.birbaddons.gui.AbstractSubScreenWidget;

import java.util.function.Consumer;

public class FeatureSettingsPopup extends AbstractSubScreenWidget {

    public String title;
    private static final int margin = 2;
    private int initY;
    public Font font;

    public FeatureSettingsPopup(int x, int y, int width, int height, Consumer<GuiEventListener> removeWidgetConsumer, String title) {
        this.title = title;
        this.initY = margin;
        this.font = Minecraft.getInstance().font;
        super(x, y, width, height, removeWidgetConsumer);
    }

    @Override
    protected void init() {
        addTitle(title);
        initSettings();
    }

    protected void initSettings() {

    }

    protected void addTitle(String title) {
        int b = 25;
        int initYcopy = initY;
        addRenderableOnly((g, _, _, _)->g.fill(getX()+margin, getY()+initYcopy, getRight()-margin, getY()+initYcopy+b, ARGB.color(255, 70, 70, 90)));
        addRenderableOnly((g, _, _, _)->g.centeredText(font, title, getX()+getWidth()/2, getY()+initYcopy+(b-font.lineHeight)/2, ARGB.white(255)));
        addRenderableWidget(new DelButton(getRight()-margin-5-15, getY()+initYcopy+5, 15, 15));
        initY+=b;
    }

    protected void addSwitch(String name, ConfigBoolean bool) {
        int b = 25;
        int initYcopy = initY;
        addRenderableOnly((g, _, _, _)->g.text(font, name, getX()+margin+5, getY()+initYcopy+(b-font.lineHeight)/2, ARGB.white(255)));
        int switchWidth = 10;
        int switchHeight = 10;
        addRenderableWidget(new SettingsSwitch(getRight()-20, getY()+initYcopy + (b-switchHeight)/2 -1, switchWidth, switchHeight, bool));
        initY+=b;
    }

    protected void addScreenButton(String name, Screen screen) {
        int b = 25;
        int initYcopy = initY;
        addRenderableOnly((g, _, _, _)->g.text(font, name, getX()+margin+5, getY()+initYcopy+(b-font.lineHeight)/2, ARGB.white(255)));
        int switchWidth = 10;
        int switchHeight = 10;
        addRenderableWidget(new SettingsScreenButton(getRight()-20, getY()+initYcopy + (b-switchHeight)/2 -1, switchWidth, switchHeight, screen));
        initY+=b;
    }

    // TODO: EditBox for number values

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        graphics.fill(getX(), getY(), getX()+getWidth(), getY()+1, ARGB.color(255, 200, 200, 200));
        graphics.fill(getX(), getY()+1, getX()+1, getY()+getHeight()-1, ARGB.color(255, 200, 200, 200));
        graphics.fill(getX()+getWidth()-1, getY()+1, getX()+getWidth(), getY()+getHeight()-1, ARGB.color(255, 200, 200, 200));
        graphics.fill(getX(), getY()+getHeight()-1, getX()+getWidth(), getY()+getHeight(), ARGB.color(255, 200, 200, 200));
        graphics.fill(getX()+1, getY()+1, getX()+getWidth()-1, getY()+getHeight()-1, ARGB.color(220, 50, 50, 70));
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
                removeSelf();
                return true;
            }
            return false;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
            graphics.fill(getX(), getY(), getRight(), getBottom(), isMouseOver(mouseX, mouseY) ? ARGB.color(255, 255, 0, 0) : ARGB.color(255, 120, 120, 120));
            graphics.centeredText(font, "x", getX()+getWidth()/2, getY()+getHeight()/2-font.lineHeight/2, ARGB.white(255));
        }

        @Override
        protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {}
    }
}
