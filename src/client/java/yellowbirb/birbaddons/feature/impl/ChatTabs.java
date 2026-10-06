package yellowbirb.birbaddons.feature.impl;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import yellowbirb.birbaddons.config.ConfigBoolean;
import yellowbirb.birbaddons.feature.Feature;
import yellowbirb.birbaddons.gui.mainmenu.FeatureSettingsPopup;

import java.util.function.Consumer;

public class ChatTabs extends Feature {

    public Tab chatTab = Tab.ALL;
    public ConfigBoolean redirect = new ConfigBoolean(ID, "redirect", false);



    public ChatTabs() {
        super("ChatTabs", "Chat Tabs");
    }

    // Thanks to Riccio for providing a less error-prone method
    private static String removeFormatting(String string) {
        return string == null ? "" : string.replaceAll("§.?", "");
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
            case PRIVATE -> message.contains(":") && (message.startsWith("To ") || message.startsWith("From ") ||
                    message.startsWith("Friend > "));
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

    @Override
    public void onDisable() {
        chatTab = Tab.ALL;
        Minecraft.getInstance().gui.getChat().rescaleChat();
    }

    @Override
    public FeatureSettingsPopup getSettingsPopup(int x, int y, int width, int height, Consumer<GuiEventListener> removeWidgetConsumer) {
        return new FeatureSettingsPopup(x, y, width, height, removeWidgetConsumer, this.name){
            @Override
            protected void initSettings() {
                addSwitch("redirect messages", redirect);
            }
        };
    }

}
