package yellowbirb.birbaddons.client.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import yellowbirb.birbaddons.client.BirbAddonsClient;
import yellowbirb.birbaddons.client.Sounds;

public class AdrenalineBar {

    //TODO: ItemEvents#USE

    public static boolean enabled = false;

    private static int maxDurationTicks = 0;
    private static int durationTicks = 0;
    private static int maxCooldownTicks = 0;
    private static int cooldownTicks = -1;
    private static int animationTicks = 0;

    private static float lastDelta = 1;

    private static final Identifier adrenalineBarTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/adrenalinebar/adrenalinebar.png");
    private static final Identifier adrenalineBarBorderTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/adrenalinebar/adrenalinebarborder.png");
    private static final Identifier adrenalineBarBorderFullTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/adrenalinebar/adrenalinebarborderfull.png");
    private static final Identifier adrenalineBarFullAnimationTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/adrenalinebar/adrenalinefullanimation.png");

    public static void init() {

    }

    public static void extract(GuiGraphicsExtractor graphics, DeltaTracker ticktimer) {
        if (enabled) {
            int x = 100;
            int y = 100;
            float mult = 1.0F;

            float barProgress;
            if (durationTicks > 0 && cooldownTicks > 0) {
                barProgress = (float) durationTicks / maxDurationTicks;
            } else if (durationTicks <= 0 && cooldownTicks > 0) {
                barProgress = 1 - (float) cooldownTicks / (maxCooldownTicks - maxDurationTicks);
            } else {
                barProgress = 1.0F;
            }

            if (cooldownTicks == 0) {
                recharged();
            }

            if (barProgress == 1) {
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

    public static void adrenalineUsed(int newDurationTicks, int newCooldownTicks) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(Sounds.ADRENALINEACTIVATE, 0.5F, 1);
        }
        maxDurationTicks = newDurationTicks;
        durationTicks = newDurationTicks;
        maxCooldownTicks = newCooldownTicks;
        cooldownTicks = newCooldownTicks;
    }

    public static void recharged() {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            player.playSound(Sounds.FULLADRENALINE);
        }
        cooldownTicks = -1;
        animationTicks = 20;
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
        // Fuel Tank, Blue Cheese Omelette, (Bal/Crow), Skymall
        return -1;
    }

}
