package yellowbirb.birbaddons.feature.impl;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import yellowbirb.birbaddons.event.ReceiveGameMessageEvent;
import yellowbirb.birbaddons.feature.Feature;
import yellowbirb.birbaddons.render.RenderManager;
import yellowbirb.birbaddons.render.RenderUtils;

public class Theodolite extends Feature {

    private static final String THEODOLITE_MESSAGE = "The target is around [0-9]+ blocks (below|above), at a [1-9][0-9]? degrees angle!";
    private static final String PELT_REWARD_MESSAGE = "Killing the animal rewarded you [1-9][0-9]? pelts.";

    public Theodolite() {
        super("Theodolite");

        ReceiveGameMessageEvent.register(THEODOLITE_MESSAGE, (msg) -> {
            if (enabled.get()) {
                String[] words = msg.split("\\s");

                int deltaY = Integer.parseInt(words[4]);
                double alpha = Math.toRadians(90 - Integer.parseInt(words[9]));

                LocalPlayer player = Minecraft.getInstance().player;
                // can only be called in-game, so there must for sure be a player riiiiight
                assert player != null;

                if (Integer.parseInt(words[9]) == 0) {
                    Minecraft.getInstance().player.sendSystemMessage(Component.literal("§3[BirbAddons] §rCannot calculate with 0 degree angle"));
                    return;
                }

                if (words[6].equals("below,")) {
                    deltaY = -deltaY;
                }

                float angleMargin = 2.5F;
                float heightMargin = 2.5F;

                int boxyRingInnerColorR = 255;
                int boxyRingInnerColorG = 0;
                int boxyRingInnerColorB = 0;
                int boxyRingInnerColorA = 255;
                boolean boxyRingInnerVisibleThroughWalls = true;

                int boxyRingOuterCornerColorR = 0;
                int boxyRingOuterCornerColorG = 255;
                int boxyRingOuterCornerColorB = 0;
                int boxyRingOuterCornerColorA = 255;
                boolean boxyRingOuterCornerVisibleThroughWalls = false;

                int boxyRingOuterPlaneColorR = 0;
                int boxyRingOuterPlaneColorG = 255;
                int boxyRingOuterPlaneColorB = 0;
                int boxyRingOuterPlaneColorA = 50;
                boolean boxyRingOuterPlaneVisibleThroughWalls = false;

                RenderUtils.drawBoxyRing(alpha, deltaY, (float) player.getX(), (float) player.getY(), (float) player.getZ(),
                        angleMargin, heightMargin,
                        boxyRingInnerColorR, boxyRingInnerColorG, boxyRingInnerColorB, boxyRingInnerColorA, boxyRingInnerVisibleThroughWalls,
                        boxyRingOuterCornerColorR, boxyRingOuterCornerColorG, boxyRingOuterCornerColorB, boxyRingOuterCornerColorA, boxyRingOuterCornerVisibleThroughWalls,
                        boxyRingOuterPlaneColorR, boxyRingOuterPlaneColorG, boxyRingOuterPlaneColorB, boxyRingOuterPlaneColorA, boxyRingOuterPlaneVisibleThroughWalls);
            }
        });

        ReceiveGameMessageEvent.register(PELT_REWARD_MESSAGE, (_) -> clearIfEnabled());

        ClientPlayConnectionEvents.DISCONNECT.register((_, _) -> clearIfEnabled());
        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((_, _) -> clearIfEnabled());
    }

    @Override
    public void disable() {
        RenderManager.clear();
        super.disable();
    }

    private void clearIfEnabled() {
        if (enabled()) {
            RenderManager.clear();
        }
    }

}
