package yellowbirb.birbaddons.event;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ReceiveGameMessageEvent {

    private static final List<String> relevantMessages = new ArrayList<>();
    private static final Map<String, Consumer<String>> messagesMap = new HashMap<>();

    public static void register(String regex, Consumer<String> consumer) {
        if (!relevantMessages.contains(regex)) {
            relevantMessages.add(regex);
            messagesMap.putIfAbsent(regex, consumer);
        } else {
            messagesMap.computeIfPresent(regex, (_, oldConsumer) -> oldConsumer.andThen(consumer));
        }
    }

    public static void receiveMessage(Component component) {
        String message = component.getString();

        int messageType = isRelevant(message);
        if (messageType < 0) return;

        messagesMap.get(relevantMessages.get(messageType)).accept(message);
    }

    private static int isRelevant(String msg) {
        if (msg == null || msg.isEmpty() || msg.startsWith(" ")) return -1;
        for (int i = 0; i < relevantMessages.size(); i++) {
            if (msg.matches(relevantMessages.get(i))) {
                return i;
            }
        }
        return -1;
    }
}
