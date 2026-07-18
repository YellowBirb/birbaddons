package yellowbirb.birbaddons.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import yellowbirb.birbaddons.BirbAddonsClient;
import yellowbirb.birbaddons.feature.impl.ChatTabs;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    @Shadow protected EditBox input;

    protected ChatScreenMixin(Component title) {
        super(title);
    }

    // Chat Tabs: Insert Tab Buttons into Chat Screen
    @Inject(at = @At("TAIL"), method = "init")
    private void onInit(CallbackInfo ci) {
        ChatTabs chatTabs = BirbAddonsClient.getInstance().features.chatTabs;
        if (chatTabs.enabled()) {
            Minecraft client = Minecraft.getInstance();
            ChatComponent hud = client.gui.getChat();
            for (ChatTabs.Tab chatTab : ChatTabs.Tab.values()) {
                String message = "X";
                switch (chatTab) {
                    case ALL -> message = "A";
                    case PARTY -> message = "P";
                    case GUILD -> message = "G";
                    case PRIVATE -> message = "PM";
                    case COOP -> message = "CC";
                }
                Button tabButton = Button.builder(Component.literal(message), (_) -> {
                    chatTabs.chatTab = chatTab;
                    hud.rescaleChat();
                    client.schedule(() -> setFocused(input));
                }).bounds(5 + chatTab.ordinal() * 22, this.height - ChatComponent.getHeight(Minecraft.getInstance().options.chatHeightFocused().get()) - 40 - 20 - 5, 20, 20).build();

                addRenderableWidget(tabButton);
            }
        }
    }

    // Chat Tabs: reset focus to chat field if arrow key was pressed
    //            otherwise Buttons would be focused, not allowing user to type
    @Inject(at = @At("HEAD"), method = "keyPressed")
    private void onKeyPressed(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        ChatTabs chatTabs = BirbAddonsClient.getInstance().features.chatTabs;
        if (chatTabs.enabled()) {
            setFocused(this.input);
        }
    }
}
