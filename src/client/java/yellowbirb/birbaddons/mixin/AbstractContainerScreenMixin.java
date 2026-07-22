package yellowbirb.birbaddons.mixin;

import net.minecraft.client.gui.ItemSlotMouseAction;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yellowbirb.birbaddons.BirbAddonsClient;
import yellowbirb.birbaddons.feature.impl.InventoryButtons;
import yellowbirb.birbaddons.gui.InventoryButton;

import java.util.ArrayList;
import java.util.List;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin<T extends AbstractContainerMenu>
        extends Screen
        implements MenuAccess<T> {


    @Shadow protected final T menu;
    @Shadow protected final Component playerInventoryTitle;
    @Shadow protected final int imageWidth;
    @Shadow protected final int imageHeight;
    @Shadow private boolean skipNextRelease;
    @Shadow protected int titleLabelX;
    @Shadow protected int titleLabelY;
    @Shadow protected int inventoryLabelX;
    @Shadow protected int inventoryLabelY;
    @Shadow private final List<ItemSlotMouseAction> itemSlotMouseActions;

    public AbstractContainerScreenMixin(T menu, Inventory inventory, Component title) {
        this(menu, inventory, title, 176, 166);
    }

    public AbstractContainerScreenMixin(T menu, Inventory inventory, Component title, int imageWidth, int imageHeight) {
        super(title);
        this.menu = menu;
        this.playerInventoryTitle = inventory.getDisplayName();
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.skipNextRelease = true;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
        this.inventoryLabelX = 8;
        this.inventoryLabelY = imageHeight - 94;
        this.itemSlotMouseActions = new ArrayList<>();
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void onInit(CallbackInfo ci) {
        InventoryButtons inventoryButtons = BirbAddonsClient.getInstance().features.inventoryButtons;
        if (inventoryButtons.enabled()) {
            for (InventoryButton button : inventoryButtons.buttons.get()) {
                button.setScreen(this);
                addRenderableWidget(button);
            }
        }
    }
}
