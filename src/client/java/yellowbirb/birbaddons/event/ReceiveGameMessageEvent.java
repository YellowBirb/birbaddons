package yellowbirb.birbaddons.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import yellowbirb.birbaddons.hud.AdrenalineBar;
import yellowbirb.birbaddons.render.RenderUtils;

public class ReceiveGameMessageEvent {

    private static final String THEODOLITE_MESSAGE = "The target is around [0-9]+ blocks (below|above), at a [1-9][0-9]? degrees angle!";
    private static final String PELT_REWARD_MESSAGE = "Killing the animal rewarded you [1-9][0-9]? pelts.";
    private static final String MA_USED_MESSAGE = "You used your (Mining Speed Boost|Pickobulus|Tunnel Vision|Maniac Miner|Gemstone Infusion|Sheer Force) Pickaxe Ability!";
    private static final String MA_EXPIRED_MESSAGE = "Your (Mining Speed Boost|Pickobulus|Tunnel Vision|Maniac Miner|Gemstone Infusion|Sheer Force) has expired!";
    private static final String MA_AVAILABLE_MESSAGE = "(Mining Speed Boost|Pickobulus|Tunnel Vision|Maniac Miner|Gemstone Infusion|Sheer Force) is now available!";
    // TODO: entered Mineshaft

    private static final String[] RELEVANT_MESSAGES = {THEODOLITE_MESSAGE, PELT_REWARD_MESSAGE, MA_USED_MESSAGE, MA_EXPIRED_MESSAGE, MA_AVAILABLE_MESSAGE};


    public static void receiveMessage(Component component) {
        String message = component.getString();
        int messageType = isRelevant(message);

        if (messageType < 0) return;

        switch (messageType) {
            case 0 -> onReceiveTheodoliteMessage(message);
            case 1 -> onReceivePeltRewardMessage();
            case 2 -> onReceiveMAUsedMessage(message);
            case 3 -> onReceiveMAExpiredMessage();
            case 4 -> onReceiveMAAvailableMessage();
        }
    }

    private static int isRelevant(String msg) {

        if (msg == null || msg.isEmpty() || msg.startsWith(" ")) return -1;

        for (int i = 0; i < RELEVANT_MESSAGES.length; i++) {
            if (msg.charAt(0) == RELEVANT_MESSAGES[i].charAt(0)) {
                if (msg.matches(RELEVANT_MESSAGES[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    private static void onReceiveTheodoliteMessage(String msg) {
        String[] words = msg.split("\\s");

        int deltaY = Integer.parseInt(words[4]);
        double alpha = Math.toRadians(90 - Integer.parseInt(words[9]));

        LocalPlayer player = Minecraft.getInstance().player;
        // can only be called in-game, so there must for sure be a player riiiiight
        assert player != null;

        if (Integer.parseInt(words[9]) == 0) {
            Minecraft.getInstance().player.sendSystemMessage(Component.literal("§3[BirbAddons] §cCannot calculate with 0 degree angle"));
            return;
        }

        if (words[6].equals("below,")) {
            deltaY = -deltaY;
        }

        RenderUtils.drawBoxyRing(alpha, deltaY, player);
    }

    private static void onReceivePeltRewardMessage() {}

    private static void onReceiveMAUsedMessage(String msg) {
        if (AdrenalineBar.enabled) {
            String[] words = msg.split("\\s");
            String ability = "";
            int duration = 0;
            int cooldown = 0;
            switch (words[3]) {
                case "Mining" -> {
                    ability = "Mining Speed Boost";
                    duration = AdrenalineBar.getMiningAbilityDuration("Mining Speed Boost", 3);
                    cooldown = AdrenalineBar.getMiningAbilityCooldown("Mining Speed Boost", 3);
                }
                case "Pickobulus" -> {
                    ability = "Pickobulus";
                    duration = AdrenalineBar.getMiningAbilityDuration("Pickobulus", 3);
                    cooldown = AdrenalineBar.getMiningAbilityCooldown("Pickobulus", 3);
                }
                case "Tunnel" -> {
                    ability = "Tunnel Vision";
                    duration = AdrenalineBar.getMiningAbilityDuration("Tunnel Vision", 3);
                    cooldown = AdrenalineBar.getMiningAbilityCooldown("Tunnel Vision", 3);
                }
                case "Maniac" -> {
                    ability = "Maniac Miner";
                    duration = AdrenalineBar.getMiningAbilityDuration("Maniac Miner", 3);
                    cooldown = AdrenalineBar.getMiningAbilityCooldown("Maniac Miner", 3);
                }
                case "Gemstone" -> {
                    ability = "Gemstone Infusion";
                    duration = AdrenalineBar.getMiningAbilityDuration("Gemstone Infusion", 3);
                    cooldown = AdrenalineBar.getMiningAbilityCooldown("Gemstone Infusion", 3);
                }
                case "Sheer" -> {
                    ability = "Sheer Force";
                    duration = AdrenalineBar.getMiningAbilityDuration("Sheer Force", 3);
                    cooldown = AdrenalineBar.getMiningAbilityCooldown("Sheer Force", 3);
                }
            }
            AdrenalineBar.adrenalineUsed(ability, duration*20, cooldown*20);
        }
    }

    private static void onReceiveMAExpiredMessage() {
        AdrenalineBar.expired();
    }

    private static void onReceiveMAAvailableMessage() {
        AdrenalineBar.recharged();
    }

}
