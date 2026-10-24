package yellowbirb.birbaddons.feature.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import yellowbirb.birbaddons.config.ConfigBoolean;
import yellowbirb.birbaddons.config.ConfigInteger;
import yellowbirb.birbaddons.event.ReceiveGameMessageEvent;
import yellowbirb.birbaddons.feature.Feature;
import yellowbirb.birbaddons.gui.mainmenu.FeatureSettingsPopup;
import yellowbirb.birbaddons.render.RenderManager;
import yellowbirb.birbaddons.render.RenderUtils;
import yellowbirb.birbaddons.util.Utils;

import java.util.function.Consumer;

public class Theodolite extends Feature {

    private final ConfigInteger maxResults;
    private final ConfigBoolean allowInputZero;

    private static final String THEODOLITE_MESSAGE_A = "The target is around \\d+ blocks (below|above), at a \\d+ degrees angle!";
    private static final String THEODOLITE_MESSAGE_B = "You are at the exact height!";
    private static final String TASK_COMPLETED_MESSAGE = "Return to the Trapper soon to get a new animal to hunt!";

    private int boxyRingCounter = 0;

    public Theodolite() {
        super("Theodolite", "Theodolite");

        maxResults = new ConfigInteger(ID, "maxResults", 2);
        allowInputZero = new ConfigBoolean(ID, "allowInputZero", false);

        ReceiveGameMessageEvent.register(THEODOLITE_MESSAGE_A, (msg) -> {
            if (enabled.get()) {
                String[] words = msg.split("\\s");
                int rawDeltaY = Integer.parseInt(words[4]);
                int rawAngle = Integer.parseInt(words[9]);

                if (!allowInputZero.get() && rawAngle == 0) {
                    Utils.displayMessage("Skipping 0 degree angle");
                    return;
                }

                float deltaY = rawDeltaY;
                float deltaY1 = rawDeltaY + 2.5F;
                float deltaY2 = rawDeltaY == 5 ? 0.5F : rawDeltaY - 2.5F;

                if (words[6].equals("below,")) {
                    deltaY = -deltaY;
                    deltaY1 = -deltaY1;
                    deltaY2 = -deltaY2;
                }

                int angle = 90 - rawAngle;

                double alpha = Math.toRadians(angle);
                double alpha1 = Math.toRadians(Math.min(angle + 2.5, 90));
                double alpha2 = Math.toRadians(Math.max(angle - 2.5, 0));

                process(deltaY, deltaY1, deltaY2, alpha, alpha1, alpha2);
            }
        });

        ReceiveGameMessageEvent.register(THEODOLITE_MESSAGE_B, (_) -> {
            if (enabled.get()) {
                if (!allowInputZero.get()) {
                    Utils.displayMessage("Skipping exact height");
                    return;
                }

                process(0.0F, 0.5F, -0.5F,
                        0, Math.toRadians(90), Math.toRadians(0));
            }
        });

        ReceiveGameMessageEvent.register(TASK_COMPLETED_MESSAGE, (_) -> RenderManager.clear(ID));

        ClientPlayConnectionEvents.DISCONNECT.register((_, _) -> RenderManager.clear(ID));
        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((_, _) -> RenderManager.clear(ID));

        // there could be a hud indicating the current minus target Y level
    }

    private void process(float deltaY, float deltaY1, float deltaY2,
                         double alpha, double alpha1, double alpha2) {

        LocalPlayer player = Minecraft.getInstance().player;
        // can only be called in-game, so there must for sure be a player riiiiight
        assert player != null;

        int boxyRingInnerColor = 0xFFFF0000;
        boolean boxyRingInnerVisibleThroughWalls = true;

        int boxyRingOuterCornerColor = 0xFF00FF00;
        boolean boxyRingOuterCornerVisibleThroughWalls = false;

        int boxyRingOuterPlaneColor = 0x3200FF00;
        boolean boxyRingOuterPlaneVisibleThroughWalls = false;

        boxyRingCounter = (boxyRingCounter + 1) % maxResults.get();

        RenderManager.add(RenderUtils.createBoxyRing(ID, "boxyRing" + boxyRingCounter,
                (float) player.getX(), (float) player.getY(), (float) player.getZ(),
                alpha, alpha1, alpha2,
                deltaY, deltaY1, deltaY2,
                boxyRingInnerColor, boxyRingInnerVisibleThroughWalls,
                boxyRingOuterCornerColor, boxyRingOuterCornerVisibleThroughWalls,
                boxyRingOuterPlaneColor, boxyRingOuterPlaneVisibleThroughWalls));
    }

    @Override
    public void onDisable() {
        RenderManager.clear(ID);
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        LiteralArgumentBuilder<FabricClientCommandSource> command = super.getCommand();

        LiteralArgumentBuilder<FabricClientCommandSource> set = ClientCommands.literal("set");

        set.then(maxResults.getCommand());
        set.then(allowInputZero.getCommand());

        command.then(set);

        LiteralArgumentBuilder<FabricClientCommandSource> clear = ClientCommands.literal("clear").executes((context) -> {
            RenderManager.clear(ID);
            Utils.displayMessage(context.getSource().getPlayer(), "Cleared all Theodolite objects drawn in the world");
            return 1;
        });

        command.then(clear);

        LiteralArgumentBuilder<FabricClientCommandSource> debug = ClientCommands.literal("debug");

        LiteralArgumentBuilder<FabricClientCommandSource> testtheodolite = ClientCommands.literal("testtheodolite")
                .then(ClientCommands.argument("deltay", IntegerArgumentType.integer())
                        .then(ClientCommands.argument("angle", IntegerArgumentType.integer())
                                .executes((context -> {
                                    if (!enabled()) {
                                        Utils.displayMessage("Feature is not enabled!");
                                    } else {
                                        int deltay = IntegerArgumentType.getInteger(context, "deltay");
                                        int angle = IntegerArgumentType.getInteger(context, "angle");
                                        if (deltay == 0) {
                                            Utils.displayMessage(context.getSource().getPlayer(), "You are at the exact height!");
                                            ReceiveGameMessageEvent.receiveMessage(Component.literal("You are at the exact height!"));
                                        } else if (deltay > 0) {
                                            Utils.displayMessage(context.getSource().getPlayer(), "The target is around " + deltay + " blocks above, at a " + angle + " degrees angle!");
                                            ReceiveGameMessageEvent.receiveMessage(Component.literal("The target is around " + deltay + " blocks above, at a " + angle + " degrees angle!"));
                                        } else {
                                            Utils.displayMessage(context.getSource().getPlayer(), "The target is around " + (-deltay) + " blocks below, at a " + angle + " degrees angle!");
                                            ReceiveGameMessageEvent.receiveMessage(Component.literal("The target is around " + (-deltay) + " blocks below, at a " + angle + " degrees angle!"));
                                        }
                                    }
                                    return 1;
                                }))));

        debug.then(testtheodolite);

        command.then(debug);

        return command;
    }

    @Override
    public FeatureSettingsPopup getSettingsPopup(int x, int y, int width, int height, Consumer<GuiEventListener> removeWidgetConsumer) {
        return new FeatureSettingsPopup(x, y, width, height, removeWidgetConsumer, this.name){
            @Override
            protected void initSettings() {
                addSwitch("Allow Input Zero", allowInputZero);
            }
        };
    }

}
