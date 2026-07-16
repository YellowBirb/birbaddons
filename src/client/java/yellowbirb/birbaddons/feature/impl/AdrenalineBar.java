package yellowbirb.birbaddons.feature.impl;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import yellowbirb.birbaddons.BirbAddonsClient;
import yellowbirb.birbaddons.Sounds;
import yellowbirb.birbaddons.config.ConfigBoolean;
import yellowbirb.birbaddons.event.ReceiveGameMessageEvent;

public class AdrenalineBar {

    //TODO: ? ItemEvents#USE
    // TODO: ? Fuel Tank, Blue Cheese Omelette, (Bal/Crow), Skymall, CotM

    public static final String ID = "AdrenalineBar";
    public static final ConfigBoolean enabled = new ConfigBoolean(ID, "enabled", false);
    public static final ConfigBoolean replayFullSound = new ConfigBoolean(ID, "replayFullSound", false);

    private static boolean available = true;
    private static boolean inUse = false;

    private static long abilityStartMillis = 0;
    private static long durationEndMillis = 0;
    private static long cooldownEndMillis = 0;
    private static long animationStartMillis = 0;

    private static final String MA_USED_MESSAGE = "You used your (Mining Speed Boost|Pickobulus|Tunnel Vision|Maniac Miner|Gemstone Infusion|Sheer Force) Pickaxe Ability!";
    private static final String MA_EXPIRED_MESSAGE = "Your (Mining Speed Boost|Pickobulus|Tunnel Vision|Maniac Miner|Gemstone Infusion|Sheer Force) has expired!";
    private static final String MA_AVAILABLE_MESSAGE = "(Mining Speed Boost|Pickobulus|Tunnel Vision|Maniac Miner|Gemstone Infusion|Sheer Force) is now available!";
    // TODO: entered Mineshaft, reset

    private static final Identifier adrenalineBarTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/adrenalinebar/adrenalinebar.png");
    private static final Identifier adrenalineBarBorderTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/adrenalinebar/adrenalinebarborder.png");
    private static final Identifier adrenalineBarBorderFullTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/adrenalinebar/adrenalinebarborderfull.png");
    private static final Identifier adrenalineBarFullAnimationTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/adrenalinebar/adrenalinefullanimation.png");

    public static void init() {
        ReceiveGameMessageEvent.register(MA_USED_MESSAGE, (msg) -> {
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
            adrenalineUsed(ability, duration*1000, cooldown*1000);
        });

        ReceiveGameMessageEvent.register(MA_EXPIRED_MESSAGE, (_) -> expired());

        ReceiveGameMessageEvent.register(MA_AVAILABLE_MESSAGE, (_) -> recharged());

        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "before_chat"), AdrenalineBar::extract);
    }

    public static void extract(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        if (enabled.get()) {
            int x = 100;
            int y = 100;
            float mult = 1.0F;

            float barProgress = getBarProgress();

            if (available) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarBorderFullTexture, x, y, 0, 6, Math.round(104*mult), Math.round(32*mult), 104, 32, 104, 240);
            } else {
                graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarBorderTexture, x, y, 0, 6, Math.round(104*mult), Math.round(32*mult), 104, 32, 104, 480);
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarTexture, x+12, y+14, 0, 18, Math.round(80*mult*barProgress), Math.round(8*mult), Math.round(80*barProgress), 8, 80, 36);

            int animFrame = (int) Math.floor((double) (Util.getMillis() - animationStartMillis) / 100);
            if (animFrame < 10) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarFullAnimationTexture, x-34, y-19, 0, animFrame*70, Math.round(172*mult), Math.round(70*mult), 172, 70, 172, 700);
            }
        }
    }

    private static float getBarProgress() {
        float barProgress;
        long durationDivisor = durationEndMillis - abilityStartMillis;
        long cooldownDivisor = cooldownEndMillis - durationEndMillis;
        if (available || durationDivisor == 0 || cooldownDivisor == 0) {
            barProgress = 1.0F;
        } else if (inUse) {
            barProgress = Math.max(0, (float) (durationEndMillis - Util.getMillis()) / (durationDivisor));
        } else {
            barProgress = Math.min(1, (float) (Util.getMillis() - durationEndMillis) / (cooldownDivisor));
        }
        return barProgress;
    }

    public static void adrenalineUsed(String ability, int durationMillis, int cooldownMillis) {
        if (enabled.get()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                if (ability.equals("Pickobulus")) {
                    player.playSound(Sounds.ADRENALINEACTIVATE);
                } else {
                    player.playSound(Sounds.ADRENALINESTART, 1F, 1);
                }
            }
        }

        long currentMillis = Util.getMillis();

        available = false;
        abilityStartMillis = currentMillis;
        cooldownEndMillis = currentMillis + cooldownMillis;

        if (ability.equals("Pickobulus")) {
            inUse = false;
            durationEndMillis = currentMillis;
        } else {
            inUse = true;
            durationEndMillis = currentMillis + durationMillis;
        }
    }

    public static void expired() {
        if (enabled.get()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                player.playSound(Sounds.ADRENALINEEND);
            }
        }
        available = false;
        inUse = false;
    }

    public static void recharged() {
        if (!available || replayFullSound.get()) {
            if (enabled.get()) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
                    player.playSound(Sounds.FULLADRENALINE);
                }
            }
            available = true;
            inUse = false;
            animationStartMillis = Util.getMillis();
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
