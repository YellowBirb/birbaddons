package yellowbirb.birbaddons.mixin;

import net.fabricmc.loader.api.FabricLoader;
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

    /**
     * @author  yellowbirb - birbaddons
     * @reason  ChatTabs: Insert Tab Buttons into Chat Screen
     */
    @Inject(at = @At("TAIL"), method = "init")
    private void onInit(CallbackInfo ci) {
        ChatTabs chatTabs = BirbAddonsClient.getInstance().features.chatTabs;
        if (chatTabs.enabled()) {
            Minecraft client = Minecraft.getInstance();
            ChatComponent hud = client.gui.getChat();
            for (ChatTabs.Tab chatTab : ChatTabs.Tab.values()) {
                String message = switch (chatTab) {
                    case ALL -> "A";
                    case PARTY -> "P";
                    case GUILD -> "G";
                    case PRIVATE -> "PM";
                    case COOP -> "CC";
                };
                int extra = FabricLoader.getInstance().isModLoaded("chatpatches") ? 28 : 0;
                Button tabButton = Button.builder(Component.literal(message), (_) -> {
                    chatTabs.chatTab = chatTab;
                    hud.rescaleChat();
                    client.schedule(() -> setFocused(input));
                }).bounds(5 + chatTab.ordinal() * 22, this.height - ChatComponent.getHeight(minecraft.options.chatHeightFocused().get()) - 40 - 20 - extra - 5, 20, 20).build();

                addRenderableWidget(tabButton);
            }
        }
    }

    /**
     * @author  yellowbirb - birbaddons
     * @reason  ChatTabs: automatically send messages to selected channel - Thanks to Pinkcommando for the idea
     */
    @Inject(method = "handleChatInput", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientPacketListener;sendChat(Ljava/lang/String;)V"), cancellable = true)
    public void handleChatInput(String msg, boolean addToRecent, CallbackInfo ci) {
        ChatTabs chatTabs = BirbAddonsClient.getInstance().features.chatTabs;
        if (chatTabs.enabled() && !msg.startsWith("/")) {
            String prefix = switch (chatTabs.chatTab) {
                case ALL, PRIVATE -> null;
                case PARTY -> "/pc ";
                case GUILD -> "/gc ";
                case COOP -> "/cc ";
            };
            if (prefix != null) {
                ci.cancel();
                this.minecraft.player.connection.sendChat(prefix + msg);
            }
        }
    }

    /**
     * @author  yellowbirb - birbaddons
     * @reason  Chat Tabs: reset focus to chat field if arrow key was pressed
     */
    @Inject(at = @At("TAIL"), method = "keyPressed")
    private void onKeyPressed(KeyEvent event, CallbackInfoReturnable<Boolean> cir) {
        ChatTabs chatTabs = BirbAddonsClient.getInstance().features.chatTabs;
        if (chatTabs.enabled()) {
            setFocused(this.input);
        }
    }
}
