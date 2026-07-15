package yellowbirb.birbaddons.client.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import yellowbirb.birbaddons.client.BirbAddonsClient;
import yellowbirb.birbaddons.client.util.ChatTabs;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {

    // Chat Tabs: filter messages from chat
    @WrapMethod(method = "addMessageToDisplayQueue")
    private void onAddVisibleMessage(GuiMessage message, Operation<Void> original) {
        if (true /* TODO: isEnabled */) {
            if (ChatTabs.filter(message.content(), BirbAddonsClient.chatTab)) {
                original.call(message);
            }
        }
    }

    // Chat Tabs: increase Chat History Size
    @ModifyExpressionValue(method =
            {"addMessageToDisplayQueue", "addMessageToQueue", "addRecentChat"},
            at = @At(value = "CONSTANT", args = "intValue=100"))
    public int modifyMaxHistorySize(int originalMaxSize) {
        if (true /* TODO: isEnabled */) {
            return 512;
        } else {
            return originalMaxSize;
        }
    }
}