package yellowbirb.birbaddons.feature.impl;

import net.minecraft.network.chat.Component;
import yellowbirb.birbaddons.config.ConfigBoolean;

public class ChatTabs {

    public static final String ID = "ChatTabs";
    public static final ConfigBoolean enabled = new ConfigBoolean(ID, "enabled", true);
    public static Tab chatTab = Tab.ALL;

    public static void init() {}

    private static String removeFormatting(String string) {
        StringBuilder stringBuilder = new StringBuilder(string);
        int deleted = 0;
        for (int i = 0; i < string.length(); i++) {
            while (string.charAt(i) == '§') {
                stringBuilder.deleteCharAt(i-deleted);
                deleted++;
                i++;
                stringBuilder.deleteCharAt(i-deleted);
                deleted++;
                i++;
            }
        }
        return stringBuilder.toString();
    }

    // Heavy "Inspiration" from  https://github.com/skytils

    public static boolean filter(Component text, Tab tab) {
        String message = removeFormatting(text.getString());

        return switch (tab) {
            case ALL -> true;
            case PARTY -> message.startsWith("Party > ") || message.startsWith("P > ") ||
                    message.endsWith("has invited you to join their party!") ||
                    message.endsWith("to the party! They have 60 seconds to accept.") ||
                    message.equals("The party was disbanded because all invites expired and the party was empty") ||
                    message.endsWith("has disbanded the party!") ||
                    message.endsWith("has disconnected, they have 5 minutes to rejoin before they are removed from the party.") ||
                    message.endsWith("joined the party.") || message.endsWith("has left the party.") ||
                    message.endsWith("has been removed from the party.") || message.startsWith("The party was transferred to ") ||
                    (message.startsWith("Kicked ") && message.endsWith(" because they were offline."));
            case GUILD -> message.startsWith("Guild > ") || message.startsWith("G > ");
            case PRIVATE -> message.startsWith("To ") || message.startsWith("From ") ||
                    message.startsWith("Friend > ");
            case COOP -> message.startsWith("Co-op > ");
        };
    }

    public enum Tab {
        ALL,
        PARTY,
        GUILD,
        PRIVATE,
        COOP
    }

}
