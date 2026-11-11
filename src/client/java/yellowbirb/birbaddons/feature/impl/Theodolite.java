package yellowbirb.birbaddons.feature.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.StringTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import yellowbirb.birbaddons.config.ConfigBoolean;
import yellowbirb.birbaddons.config.ConfigInteger;
import yellowbirb.birbaddons.event.ReceiveGameMessageEvent;
import yellowbirb.birbaddons.feature.Feature;
import yellowbirb.birbaddons.gui.mainmenu.FeatureSettingsPopup;
import yellowbirb.birbaddons.render.RenderManager;
import yellowbirb.birbaddons.render.RenderUtils;
import yellowbirb.birbaddons.util.Utils;

import java.awt.*;
import java.util.function.Consumer;

public class Theodolite extends Feature {

    private final ConfigInteger maxResults;
    private final ConfigBoolean allowInputZero;
    private final ConfigBoolean colorfulRings;

    private static final String THEODOLITE_MESSAGE_A = "The target is around \\d+ blocks (below|above), at a \\d+ degrees angle!";
    private static final String THEODOLITE_MESSAGE_B = "You are at the exact height!";
    private static final String TASK_COMPLETED_MESSAGE = "Return to the Trapper soon to get a new animal to hunt!";

    private Vec3 lastUsedAt = Vec3.ZERO;
    private int boxyRingCounter = 0;

    public Theodolite() {
        super("Theodolite", "Theodolite");

        maxResults = new ConfigInteger(ID, "maxResults", 3);
        allowInputZero = new ConfigBoolean(ID, "allowInputZero", false);
        colorfulRings = new ConfigBoolean(ID, "colorfulRings", false);

        UseItemCallback.EVENT.register((player, level, hand) -> {
            if (isTheodoliteItem(player.getItemInHand(hand)))
                lastUsedAt = player.position();
            return InteractionResult.PASS;
        });

        UseBlockCallback.EVENT.register((player, level, hand, result) -> {
            if (isTheodoliteItem(player.getItemInHand(hand)))
                lastUsedAt = player.position();
            return InteractionResult.PASS;
        });

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

                double angle = 90 - rawAngle;
                double angle1 = Math.min(angle + 2.5, 90);
                double angle2 = Math.max(angle - 2.5, 0);

                process(deltaY, deltaY1, deltaY2, angle, angle1, angle2);
            }
        });

        ReceiveGameMessageEvent.register(THEODOLITE_MESSAGE_B, (_) -> {
            if (enabled.get()) {
                if (!allowInputZero.get()) {
                    Utils.displayMessage("Skipping exact height");
                    return;
                }

                process(0.0F, 0.5F, -0.5F,
                        0, 90, 0);
            }
        });

        ReceiveGameMessageEvent.register(TASK_COMPLETED_MESSAGE, (_) -> clear());

        ClientPlayConnectionEvents.DISCONNECT.register((_, _) -> clear());
        ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((_, _) -> clear());
    }

    private static boolean isTheodoliteItem(ItemStack itemStack) {
        var tmp = itemStack.get(DataComponents.CUSTOM_DATA);
        if (tmp != null) {
            var tmp2 = tmp.copyTag().get("id");
            if (tmp2 instanceof StringTag(String value)) {
                return "TALBOTS_THEODOLITE".equals(value);
            }
        }
        return false;
    }

    private void process(float deltaY, float deltaY1, float deltaY2,
                         double angle, double angle1, double angle2) {

        int boxyRingInnerColor, boxyRingOuterCornerColor, boxyRingOuterPlaneColor;

        if (colorfulRings.get()) {
            float hue = (float) boxyRingCounter / maxResults.get() + 1.0F / 3;

            boxyRingInnerColor = Color.HSBtoRGB(hue, 1.0F, 0.8F);
            boxyRingOuterCornerColor = Color.HSBtoRGB(hue, 1.0F, 1.0F);
            boxyRingOuterPlaneColor = Color.HSBtoRGB(hue, 0.8F, 1.0F) & 0x00FFFFFF | 0x32000000;
        } else {
            boxyRingInnerColor = 0xFFFF0000;
            boxyRingOuterCornerColor = 0xFF00FF00;
            boxyRingOuterPlaneColor = 0x3200FF00;
        }

        boolean boxyRingInnerVisibleThroughWalls = true;
        boolean boxyRingOuterCornerVisibleThroughWalls = false;
        boolean boxyRingOuterPlaneVisibleThroughWalls = false;

        boxyRingCounter = (boxyRingCounter + 1) % maxResults.get();

        RenderManager.add(RenderUtils.createBoxyRing(ID, "boxyRing" + boxyRingCounter,
                (float) lastUsedAt.x, (float) lastUsedAt.y, (float) lastUsedAt.z,
                Math.toRadians(angle), Math.toRadians(angle1), Math.toRadians(angle2),
                deltaY, deltaY1, deltaY2,
                boxyRingInnerColor, boxyRingInnerVisibleThroughWalls,
                boxyRingOuterCornerColor, boxyRingOuterCornerVisibleThroughWalls,
                boxyRingOuterPlaneColor, boxyRingOuterPlaneVisibleThroughWalls));
    }

    private void clear() {
        RenderManager.clear(ID);
        boxyRingCounter = 0;
    }

    @Override
    public void onDisable() {
        clear();
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        LiteralArgumentBuilder<FabricClientCommandSource> command = super.getCommand();

        LiteralArgumentBuilder<FabricClientCommandSource> set = ClientCommands.literal("set");

        set.then(maxResults.getCommand());
        set.then(allowInputZero.getCommand());
        set.then(colorfulRings.getCommand());

        command.then(set);

        LiteralArgumentBuilder<FabricClientCommandSource> clear = ClientCommands.literal("clear").executes((context) -> {
            clear();
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
                                        lastUsedAt = context.getSource().getPlayer().position();
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
                addSwitch("Allow 0 as input", allowInputZero);
                addSwitch("Colorful Rings", colorfulRings);
            }
        };
    }

}
