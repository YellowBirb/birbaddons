package yellowbirb.birbaddons.feature.impl;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import yellowbirb.birbaddons.event.ReceiveGameMessageEvent;
import yellowbirb.birbaddons.render.RenderManager;
import yellowbirb.birbaddons.render.RenderUtils;

public class Theodolite {

    private static final String THEODOLITE_MESSAGE = "The target is around [0-9]+ blocks (below|above), at a [1-9][0-9]? degrees angle!";
    private static final String PELT_REWARD_MESSAGE = "Killing the animal rewarded you [1-9][0-9]? pelts.";

    public static void init() {
        ReceiveGameMessageEvent.register(THEODOLITE_MESSAGE, (msg) -> {
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
        });

        ReceiveGameMessageEvent.register(PELT_REWARD_MESSAGE, (_) -> RenderManager.clear());

        ClientPlayConnectionEvents.DISCONNECT.register((_, _) -> RenderManager.clear());
        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((_, _) -> RenderManager.clear());
    }

}
