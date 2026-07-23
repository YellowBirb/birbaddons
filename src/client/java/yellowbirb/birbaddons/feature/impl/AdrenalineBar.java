package yellowbirb.birbaddons.feature.impl;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import org.joml.Vector2d;
import org.jspecify.annotations.NonNull;
import yellowbirb.birbaddons.BirbAddonsClient;
import yellowbirb.birbaddons.Sounds;
import yellowbirb.birbaddons.config.ConfigBoolean;
import yellowbirb.birbaddons.config.ConfigFloat;
import yellowbirb.birbaddons.config.ConfigVec2d;
import yellowbirb.birbaddons.event.ReceiveGameMessageEvent;
import yellowbirb.birbaddons.feature.Feature;
import yellowbirb.birbaddons.gui.FakeHUDWidget;
import yellowbirb.birbaddons.gui.HUDEditScreen;
import yellowbirb.birbaddons.util.Utils;

public class AdrenalineBar extends Feature {

    // TODO: ? ItemEvents#USE
    // TODO: ? Fuel Tank, Blue Cheese Omelette, (Bal/Crow), Skymall, CotM
    // TODO: ? make bar shake?

    public final ConfigVec2d pos;
    public final ConfigFloat mult;
    public final ConfigBoolean replayFullSound;

    public boolean available = true;
    public boolean inUse = false;

    public long abilityStartMillis = 0;
    public long durationEndMillis = 0;
    public long durationEndConfirmedMillis = 0;
    public long cooldownEndMillis = 0;
    public long animationStartMillis = 0;

    private static final String MA_USED_MESSAGE = "You used your (Mining Speed Boost|Pickobulus|Tunnel Vision|Maniac Miner|Gemstone Infusion|Sheer Force) Pickaxe Ability!";
    private static final String MA_EXPIRED_MESSAGE = "Your (Mining Speed Boost|Pickobulus|Tunnel Vision|Maniac Miner|Gemstone Infusion|Sheer Force) has expired!";
    private static final String MA_AVAILABLE_MESSAGE = "(Mining Speed Boost|Pickobulus|Tunnel Vision|Maniac Miner|Gemstone Infusion|Sheer Force) is now available!";
    // TODO: entered Mineshaft, reset

    private static final Identifier adrenalineBarTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/gui/adrenalinebar/adrenalinebar.png");
    private static final Identifier adrenalineBarBorderTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/gui/adrenalinebar/adrenalinebarborder.png");
    private static final Identifier adrenalineBarBorderFullTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/gui/adrenalinebar/adrenalinebarborderfull.png");
    private static final Identifier adrenalineBarFullAnimationTexture = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "textures/gui/adrenalinebar/adrenalinefullanimation.png");

    public AdrenalineBar() {
        super("AdrenalineBar");

        pos = new ConfigVec2d(ID, "pos", new Vector2d(100, 100));
        mult = new ConfigFloat(ID, "mult", 1.0F);
        replayFullSound = new ConfigBoolean(ID, "replayFullSound", false);

        ReceiveGameMessageEvent.register(MA_USED_MESSAGE, (msg) -> {
            String[] words = msg.split("\\s");
            String ability = Utils.getMAFromFirstWord(words[3]);
            int duration = Utils.getMiningAbilityDuration(ability, 3);
            int cooldown = Utils.getMiningAbilityCooldown(ability, 3);
            adrenalineUsed(ability, duration*1000, cooldown*1000);
        });

        ReceiveGameMessageEvent.register(MA_EXPIRED_MESSAGE, (_) -> expired());

        ReceiveGameMessageEvent.register(MA_AVAILABLE_MESSAGE, (_) -> recharged());

        HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "before_chat"), AdrenalineBar::extract);
    }

    public static void extract(GuiGraphicsExtractor graphics, DeltaTracker deltaTracker) {
        AdrenalineBar adrenalineBar = BirbAddonsClient.getInstance().features.adrenalineBar;
        if (adrenalineBar.enabled.get()) {
            int x = (int) adrenalineBar.pos.get().x;
            int y = (int) adrenalineBar.pos.get().y;
            float mult = adrenalineBar.mult.get();

            float barProgress = adrenalineBar.getBarProgress();

            if (adrenalineBar.available) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarBorderFullTexture, x, y, 0, 6, Math.round(104*mult), Math.round(32*mult), 104, 32, 104, 240);
            } else {
                graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarBorderTexture, x, y, 0, 6, Math.round(104*mult), Math.round(32*mult), 104, 32, 104, 480);
            }
            graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarTexture, x+Math.round(12*mult), y+Math.round(14*mult), 0, 18, Math.round(80*mult*barProgress), Math.round(8*mult), Math.round(80*barProgress), 8, 80, 36);

            int animFrame = (int) Math.floor((double) (Util.getMillis() - adrenalineBar.animationStartMillis) / 100);
            if (animFrame < 10) {
                graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarFullAnimationTexture, x-Math.round(34*mult), y-Math.round(19*mult), 0, animFrame*70, Math.round(172*mult), Math.round(70*mult), 172, 70, 172, 700);
            }
        }
    }

    private float getBarProgress() {
        float barProgress;
        long durationDivisor = durationEndMillis - abilityStartMillis;
        long cooldownDivisor = cooldownEndMillis - durationEndConfirmedMillis;
        if (available || (durationDivisor == 0 && inUse) || cooldownDivisor <= 0) {
            barProgress = 1.0F;
        } else if (inUse) {
            barProgress = Math.max(0, (float) (durationEndMillis - Util.getMillis()) / (durationDivisor));
        } else {
            barProgress = Math.min(1, (float) (Util.getMillis() - durationEndConfirmedMillis) / (cooldownDivisor));
        }
        return barProgress;
    }

    public void adrenalineUsed(String ability, int durationMillis, int cooldownMillis) {
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

    public void expired() {
        if (enabled.get()) {
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null) {
                player.playSound(Sounds.ADRENALINEEND);
            }
        }
        inUse = false;
        durationEndConfirmedMillis = Util.getMillis();
    }

    public void recharged() {
        if (!available || replayFullSound.get()) {
            if (enabled.get()) {
                LocalPlayer player = Minecraft.getInstance().player;
                if (player != null) {
                    player.playSound(Sounds.FULLADRENALINE);
                }
            }
            available = true;
            animationStartMillis = Util.getMillis();
        }
    }

    public static void openPosMenu(Minecraft client) {
        client.schedule(()-> {
            AdrenalineBar adrenalineBar = BirbAddonsClient.getInstance().features.adrenalineBar;
            float multt = adrenalineBar.mult.get();
            client.setScreen(new HUDEditScreen(new FakeHUDWidget((int) adrenalineBar.pos.get().x(), (int) adrenalineBar.pos.get().y(), Math.round(104*multt), Math.round(32*multt), adrenalineBar.pos) {
                @Override
                protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {

                    graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarBorderFullTexture, getX(), getY(), 0, 6, getWidth(), getHeight(), 104, 32, 104, 240);
                    graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarTexture, getX()+Math.round(12*multt), getY()+Math.round(14*multt), 0, 18, Math.round(80*multt), Math.round(8*multt), 80, 8, 80, 36);
                    graphics.blit(RenderPipelines.GUI_TEXTURED, adrenalineBarFullAnimationTexture, getX()-Math.round(34*multt), getY()-Math.round(19*multt), 0, 350, Math.round(172*multt), Math.round(70*multt), 172, 70, 172, 700);
                }
            }));
        });
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand() {
        LiteralArgumentBuilder<FabricClientCommandSource> command = super.getCommand().executes((_) -> {
            openPosMenu(Minecraft.getInstance());
            return 1;
        });

        LiteralArgumentBuilder<FabricClientCommandSource> set = ClientCommands.literal("set");
        set.then(ClientCommands.literal("pos").executes((_) -> {
            openPosMenu(Minecraft.getInstance());
            return 1;
        }));
        set.then(mult.getCommand());
        set.then(replayFullSound.getCommand());
        command.then(set);

        LiteralArgumentBuilder<FabricClientCommandSource> debug = ClientCommands.literal("debug");

        LiteralArgumentBuilder<FabricClientCommandSource> startAdrenaline = ClientCommands.literal("startAdrenaline")
                .then(ClientCommands.argument("ability", StringArgumentType.string()).suggests((_, builder) -> {
                    builder.suggest("Mining");
                    builder.suggest("Pickobulus");
                    builder.suggest("Tunnel");
                    builder.suggest("Maniac");
                    builder.suggest("Gemstone");
                    builder.suggest("Sheer");
                    return builder.buildFuture();
                }).executes((context) -> {
                    String ability = Utils.getMAFromFirstWord(StringArgumentType.getString(context, "ability"));
                    adrenalineUsed(Utils.getMAFromFirstWord(ability), Utils.getMiningAbilityDuration(ability, 3)*1000, Utils.getMiningAbilityCooldown(ability, 3)*1000);
                    return 1;
                })
                        .then(ClientCommands.argument("durationMillis", IntegerArgumentType.integer())
                                .then(ClientCommands.argument("cooldownMillis", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            if (!enabled()) {
                                                Utils.displayMessage("Feature is not enabled!");
                                            } else {
                                                String ability = StringArgumentType.getString(context, "ability");
                                                int durationMillis = IntegerArgumentType.getInteger(context, "durationMillis");
                                                int cooldownMillis = IntegerArgumentType.getInteger(context, "cooldownMillis");
                                                adrenalineUsed(Utils.getMAFromFirstWord(ability), durationMillis, cooldownMillis);
                                            }
                                            return 1;
                                        }))));

        LiteralArgumentBuilder<FabricClientCommandSource> expireAdrenaline = ClientCommands.literal("expireAdrenaline").executes((_) -> {
            if (!enabled()) {
                Utils.displayMessage("Feature is not enabled!");
            } else {
                expired();
            }
            return 1;
        });

        LiteralArgumentBuilder<FabricClientCommandSource> rechargeAdrenaline = ClientCommands.literal("rechargeAdrenaline").executes((_) -> {
            if (!enabled()) {
                Utils.displayMessage("Feature is not enabled!");
            } else {
                recharged();
            }
            return 1;
        });

        debug.then(startAdrenaline);
        debug.then(expireAdrenaline);
        debug.then(rechargeAdrenaline);

        command.then(debug);

        return command;
    }



}
