package yellowbirb.birbaddons.feature.impl;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import yellowbirb.birbaddons.BirbAddonsClient;
import yellowbirb.birbaddons.Sounds;
import yellowbirb.birbaddons.event.ReceiveGameMessageEvent;

public class AdrenalineBar {

    //TODO: ItemEvents#USE
    // TODO: ? Fuel Tank, Blue Cheese Omelette, (Bal/Crow), Skymall

    // TODO: figure out how to do this like ughhhhh

    // TODO: isEnabled (but like better than this)
    public static boolean enabled = false;

    private static boolean available = true;
    private static boolean inUse = false;

    private static int maxDurationTicks = 0;
    private static int durationTicks = 0;
    private static int maxCooldownTicks = 0;
    private static int cooldownTicks = -1;
    private static int animationTicks = 0;

    private static float lastDelta = 1;

    private static final String MA_USED_MESSAGE = "You used your (Mining Speed Boost|Pickobulus|Tunnel Vision|Maniac Miner|Gemstone Infusion|Sheer Force) Pickaxe Ability!";
    private static final String MA_EXPIRED_MESSAGE = "Your (Mining Speed Boost|Pickobulus|Tunnel Vision|Maniac Miner|Gemstone Infusion|Sheer Force) has expired!";
    private static final String MA_AVAILABLE_MESSAGE = "(Mining Speed Boost|Pickobulus|Tunnel Vision|Maniac Miner|Gemstone Infusion|Sheer Force) is now available!";
    // TODO: entered Mineshaft

    private static final Identifier adrenalineBarTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/adrenalinebar/adrenalinebar.png");
    private static final Identifier adrenalineBarBorderTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/adrenalinebar/adrenalinebarborder.png");
    private static final Identifier adrenalineBarBorderFullTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/adrenalinebar/adrenalinebarborderfull.png");
    private static final Identifier adrenalineBarFullAnimationTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/adrenalinebar/adrenalinefullanimation.png");

    public static void init() {
        ReceiveGameMessageEvent.register(MA_USED_MESSAGE, (msg) -> {
            if (enabled) {
                String[] words = msg.split("\\s");
                String ability = "";
                switch (words[3]) {
                    case "Mining" -> ability = "Mining Speed Boost";
                    case "Pickobulus" -> ability = "Pickobulus";
                    case "Tunnel" -> ability = "Tunnel Vision";
                    case "Maniac" -> ability = "Maniac Miner";
                    case "Gemstone" -> ability = "Gemstone Infusion";
                    case "Sheer" -> ability = "Sheer Force";
                }
                int duration = getMiningAbilityDuration(ability, 3);
                int cooldown = getMiningAbilityCooldown(ability, 3);
                adrenalineUsed(ability, duration*20, cooldown*20);
            }
        });

        ReceiveGameMessageEvent.register(MA_EXPIRED_MESSAGE, (_) -> expired());

        ReceiveGameMessageEvent.register(MA_AVAILABLE_MESSAGE, (_) -> recharged());

        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "before_chat"), AdrenalineBar::extract);
    }

    public static void extract(GuiGraphicsExtractor graphics, DeltaTracker ticktimer) {
        if (enabled) {
            int x = 100;
            int y = 100;
            float mult = 1.0F;

            float barProgress;
            if (inUse) {
                barProgress = (float) durationTicks / maxDurationTicks;
            } else if (maxCooldownTicks - maxDurationTicks != 0) {
                barProgress = 1 - (float) cooldownTicks / (maxCooldownTicks - maxDurationTicks);
            } else {
                barProgress = 1.0F;
            }

            if (available) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarBorderFullTexture, x, y, 0, 6, Math.round(104*mult), Math.round(32*mult), 104, 32, 104, 240);
            } else {
                graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarBorderTexture, x, y, 0, 6, Math.round(104*mult), Math.round(32*mult), 104, 32, 104, 480);
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarTexture, x+12, y+14, 0, 18, Math.round(80*mult*barProgress), Math.round(8*mult), Math.round(80*barProgress), 8, 80, 36);

            if (animationTicks > 0) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarFullAnimationTexture, x-34, y-19, 0, (9 - (int) ((double) (animationTicks / 2)))*70, Math.round(172*mult), Math.round(70*mult), 172, 70, 172, 700);
            }
        }

        // TODO: Util.getMillis()
        if (lastDelta > ticktimer.getGameTimeDeltaPartialTick(false)) {
            if (durationTicks > 0) {
                durationTicks--;
            }
            if (cooldownTicks > 0) {
                cooldownTicks--;
            }
            if (animationTicks > 0) {
                animationTicks--;
            }
        }
        lastDelta = ticktimer.getGameTimeDeltaPartialTick(false);
    }

    public static void adrenalineUsed(String ability, int newDurationTicks, int newCooldownTicks) {
        if (enabled) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                if (ability.equals("Pickobulus")) {
                    player.playSound(Sounds.ADRENALINEACTIVATE);
                } else {
                    player.playSound(Sounds.ADRENALINESTART, 1F, 1);
                }
            }
        }
        available = false;
        inUse = true;
        maxDurationTicks = newDurationTicks;
        durationTicks = newDurationTicks;
        maxCooldownTicks = newCooldownTicks;
        cooldownTicks = newCooldownTicks;
    }

    public static void expired() {
        if (enabled) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                player.playSound(Sounds.ADRENALINEEND);
            }
        }
        available = false;
        inUse = false;
        durationTicks = 0;
    }

    public static void recharged() {
        if (!available) {
            if (enabled) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
                    player.playSound(Sounds.FULLADRENALINE);
                }
            }
            available = true;
            inUse = false;
            maxDurationTicks = 0;
            durationTicks = 0;
            maxCooldownTicks = 0;
            cooldownTicks = 0;
            animationTicks = 20;
        }
    }

    public static int getMiningAbilityDuration(String ability, int level) {
        switch (ability) {
            case "Mining Speed Boost" -> {return 5+5*level;}
            case "Pickobulus" -> {return 0;}
            case "Tunnel Vision" -> {return 30;}
            case "Maniac Miner" -> {return 20+5*level;}
            case "Gemstone Infusion", "Sheer Force" -> {return 15+5*level;}
        }
        return -1;
    }

    public static int getMiningAbilityCooldown(String ability, int level) {
        switch (ability) {
            case "Mining Speed Boost", "Gemstone Infusion", "Sheer Force", "Maniac Miner" -> {return 120;}
            case "Pickobulus" -> {return 70-10*level;}
            case "Tunnel Vision" -> {return 130-10*level;}
        }
        return -1;
    }

}
