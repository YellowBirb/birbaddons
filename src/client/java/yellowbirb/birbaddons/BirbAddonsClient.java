package yellowbirb.birbaddons;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yellowbirb.birbaddons.config.Config;
import yellowbirb.birbaddons.event.ReceiveGameMessageEvent;
import yellowbirb.birbaddons.feature.Features;
import yellowbirb.birbaddons.render.RenderManager;
import yellowbirb.birbaddons.util.UpdateChecker;

import java.util.concurrent.atomic.AtomicBoolean;


public class BirbAddonsClient implements ClientModInitializer {
	private static BirbAddonsClient instance;
	public static final String MOD_ID = "birbaddons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public Features features;

	private static final String MODRINTH_PROJECT_ID = "brU03tAB";
	private static final String MODRINTH_PROJECT_VERSION_API_LINK = "https://api.modrinth.com/v2/project/" + MODRINTH_PROJECT_ID + "/version";
	private static final AtomicBoolean lookedForUpdate = new AtomicBoolean(false);

	// TODO: menu
	// TODO: chatpeek?

	@Override
	public void onInitializeClient() {
		instance = this;

		LOGGER.info("BirbAddons is initializing :3");

		Config.load();

		LevelRenderEvents.END_MAIN.register(RenderManager::draw);

		ClientReceiveMessageEvents.GAME.register((message, _) -> ReceiveGameMessageEvent.receiveMessage(message));

		Sounds.init();

		features = new Features();

		Command command = new Command();
		LiteralArgumentBuilder<FabricClientCommandSource> commandBuilder = command.getBuilder();
		features.buildCommands(commandBuilder);
		command.registerCommand(commandBuilder);

		ClientPlayConnectionEvents.JOIN.register((_, _, _) -> new Thread(() -> {
			if (!lookedForUpdate.get()) {
				lookedForUpdate.set(true);
				UpdateChecker.checkForUpdate(MODRINTH_PROJECT_VERSION_API_LINK);
			}
		}).start());
	}

	public static BirbAddonsClient getInstance() {
		return instance;
	}
}