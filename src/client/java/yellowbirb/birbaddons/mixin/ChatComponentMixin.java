package yellowbirb.birbaddons.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yellowbirb.birbaddons.BirbAddonsClient;
import yellowbirb.birbaddons.feature.impl.ChatTabs;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {

    // Chat Tabs: filter messages from chat
    @WrapMethod(method = "addMessageToDisplayQueue")
    private void onAddVisibleMessage(GuiMessage message, Operation<Void> original) {
        ChatTabs chatTabs = BirbAddonsClient.getInstance().features.chatTabs;
        if (chatTabs.enabled()) {
            if (ChatTabs.filter(message.content(), chatTabs.chatTab)) {
                original.call(message);
            }
        }
    }

    // Chat Tabs: increase Chat History Size
    @ModifyExpressionValue(method =
            {"addMessageToDisplayQueue", "addMessageToQueue", "addRecentChat"},
            at = @At(value = "CONSTANT", args = "intValue=100"))
    public int modifyMaxHistorySize(int originalMaxSize) {
        ChatTabs chatTabs = BirbAddonsClient.getInstance().features.chatTabs;
        if (chatTabs.enabled()) {
            return 512;
        } else {
            return originalMaxSize;
        }
    }
}