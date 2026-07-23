package yellowbirb.birbaddons.gui;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import yellowbirb.birbaddons.config.Config;

import java.util.List;

public class HUDEditScreen extends Screen {

    List<FakeHUDWidget> widgets;

    public HUDEditScreen(FakeHUDWidget... widgets) {
        super(Component.literal(""));
        this.widgets = List.of(widgets);
    }

    @Override
    public void onClose() {
        Config.save();
        super.onClose();
    }

    @Override
    protected void init() {
        for (FakeHUDWidget widget : widgets) {
            this.addRenderableWidget(widget);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }


}
