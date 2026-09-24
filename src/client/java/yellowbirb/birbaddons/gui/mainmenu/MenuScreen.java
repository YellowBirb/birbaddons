package yellowbirb.birbaddons.gui.mainmenu;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.jspecify.annotations.NonNull;
import yellowbirb.birbaddons.BirbAddonsClient;
import yellowbirb.birbaddons.config.Config;
import yellowbirb.birbaddons.feature.Feature;
import yellowbirb.birbaddons.feature.Features;

import java.util.ArrayList;
import java.util.List;

public class MenuScreen extends Screen {
    private final List<Category> categories;

    private final int categoryWidth = 100;
    private final int categoryHeight = 20;
    private final int categoryMargin = 5;

    private final int featureHeight = 20;

    public MenuScreen() {
        super(Component.literal(""));
        categories = new ArrayList<>();

        Category generalCategory = new Category("General");
        Category miningCategory = new Category("Mining");
        Category farmingCategory = new Category("Farming");

        Features features = BirbAddonsClient.getInstance().features;

        generalCategory.add(features.chatTabs);
        generalCategory.add(features.inventoryButtons);

        miningCategory.add(features.adrenalineBar);
        miningCategory.add(features.doomDrill);
        miningCategory.add(features.noSwing);

        farmingCategory.add(features.theodolite);

        categories.add(generalCategory);
        categories.add(miningCategory);
        categories.add(farmingCategory);
    }

    @Override
    protected void init() {
        for (int i = 0; i < categories.size(); i++) {
            Category category = categories.get(i);
            int x1 = (i+1)*categoryMargin + i*categoryWidth;
            addRenderableOnly((graphics, _, _, _) -> graphics.fill(x1, categoryMargin, x1 +categoryWidth, categoryMargin+categoryHeight, ARGB.color(255, 0, 0, 0)));
            addRenderableOnly((graphics, _, _, _) -> graphics.centeredText(font, "§l"+category.name, x1 + categoryWidth/2, categoryMargin + categoryHeight/2 - font.lineHeight/2, ARGB.color(255, 255, 255, 255)));

            for (int j = 0; j < category.getFeatures().size(); j++) {
                Feature feature = category.getFeatures().get(j);
                int y1 = categoryMargin+categoryHeight+ j*featureHeight;
                addRenderableWidget(new FeatureButton(x1, y1, categoryWidth, featureHeight, feature));
            }
        }
    }

    @Override
    public void onClose() {
        Config.save();
        super.onClose();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private class FeatureButton extends AbstractWidget {
        private final Feature feature;

        public FeatureButton(int x, int y, int width, int height, Feature feature) {
            super(x, y, width, height, Component.literal(""));
            this.feature = feature;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
            int x2 = 4;

            int enabledColor1 = ARGB.color(255, 255, 255, 0);
            int enabledColor2 = ARGB.color(170, 255, 255, 0);
            int enabledColor3 = ARGB.color(255, 255, 255, 255);
            int disabledColor1 = ARGB.color(255, 80, 80, 120);
            int disabledColor2 = ARGB.color(170, 80, 80, 120);
            int disabledColor3 = ARGB.color(255, 255, 255, 255);

            graphics.fill(getX(), getY(), getX()+x2, getY()+getHeight(), feature.enabled() ? enabledColor1 : disabledColor1);
            graphics.fill(getX()+ x2, getY(), getX()+getWidth(), getY()+getHeight(), feature.enabled() ? enabledColor2 : disabledColor2);
            graphics.text(font, feature.ID, getX() + 2* x2, getY()+getHeight()/2 - font.lineHeight/2, feature.enabled() ? enabledColor3 : disabledColor3);
        }

        @Override
        public void onClick(MouseButtonEvent event, boolean doubleClick) {
            if (event.button() == InputConstants.MOUSE_BUTTON_LEFT) {
                feature.toggle();
            } else if (event.button() == InputConstants.MOUSE_BUTTON_RIGHT) {
                // TODO: popup
            }
        }

        @Override
        public boolean mouseClicked(@NonNull MouseButtonEvent event, boolean doubleClick) {
            if (!this.isActive()) {
                return false;
            }
            if (this.isValidClickButton(event.buttonInfo()) && this.isMouseOver(event.x(), event.y())) {
                this.onClick(event, doubleClick);
                return true;
            }
            return false;
        }

        @Override
        protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
            return buttonInfo.button() == InputConstants.MOUSE_BUTTON_LEFT || buttonInfo.button() == InputConstants.MOUSE_BUTTON_RIGHT;
        }

        @Override
        protected void updateWidgetNarration(@NonNull NarrationElementOutput output) {}
    }
}
