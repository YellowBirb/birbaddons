package yellowbirb.birbaddons.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import yellowbirb.birbaddons.BirbAddonsClient;

public class Utils {

    public static void displayMessage(LocalPlayer player, Component msg) {
        if (player != null) {
            player.sendSystemMessage(Component.literal("§e§l[BirbAddons]§r ").append(msg));
        }
    }

    public static void displayMessage(Component msg) {
        displayMessage(Minecraft.getInstance().player, msg);
    }

    public static void displayMessage(LocalPlayer player, String msg) {
        displayMessage(player, Component.literal(msg));
    }

    public static void displayMessage(String msg) {
        displayMessage(Minecraft.getInstance().player, msg);
    }

    public static boolean isDrill(ItemStack itemStack) {
        CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            Tag tag = customData.copyTag().get("id");
            if (tag != null) {
                return tag.toString().toLowerCase().contains("drill".toLowerCase());
            }
        }
        return false;
    }

    public static String getMAFromFirstWord(String first) {
        String ability = "";
        switch (first) {
            case "Mining" -> ability = "Mining Speed Boost";
            case "Pickobulus" -> ability = "Pickobulus";
            case "Tunnel" -> ability = "Tunnel Vision";
            case "Maniac" -> ability = "Maniac Miner";
            case "Gemstone" -> ability = "Gemstone Infusion";
            case "Sheer" -> ability = "Sheer Force";
            default -> BirbAddonsClient.LOGGER.error("Unknown Ability in getMAFromFirstWord: {}", first);
        }
        return ability;
    }

    public static int getMiningAbilityDuration(String ability, int level) {
        switch (ability) {
            case "Mining Speed Boost" -> {return 5+5*level;}
            case "Pickobulus" -> {return 0;}
            case "Tunnel Vision" -> {return 30;}
            case "Maniac Miner" -> {return 20+5*level;}
            case "Gemstone Infusion", "Sheer Force" -> {return 15+5*level;}
            default -> BirbAddonsClient.LOGGER.error("Unknown Ability in getMiningAbilityDuration: {}", ability);
        }
        return -1;
    }

    public static int getMiningAbilityCooldown(String ability, int level) {
        switch (ability) {
            case "Mining Speed Boost", "Gemstone Infusion", "Sheer Force", "Maniac Miner" -> {return 120;}
            case "Pickobulus" -> {return 70-10*level;}
            case "Tunnel Vision" -> {return 130-10*level;}
            default -> BirbAddonsClient.LOGGER.error("Unknown Ability in getMiningAbilityCooldown: {}", ability);
        }
        return -1;
    }

}
