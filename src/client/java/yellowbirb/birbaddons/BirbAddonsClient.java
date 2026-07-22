package yellowbirb.birbaddons;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yellowbirb.birbaddons.config.Config;
import yellowbirb.birbaddons.event.ReceiveGameMessageEvent;
import yellowbirb.birbaddons.feature.Features;
import yellowbirb.birbaddons.render.RenderManager;

import java.util.concurrent.atomic.AtomicBoolean;


public class BirbAddonsClient implements ClientModInitializer {
	private static BirbAddonsClient instance;
	public static final String MOD_ID = "birbaddons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public Features features;

	// TODO: get link
	private static final String MODRINTH_PROJECT_VERSION_API_LINK = "";
	private static final AtomicBoolean lookedForUpdate = new AtomicBoolean(false);

	// TODO: save config more often?
	// TODO: menu

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

		// TODO: get link
		/*ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> new Thread(() -> {
			if (!lookedForUpdate.get()) {
				lookedForUpdate.set(true);
				UpdateChecker.checkForUpdate(MODRINTH_PROJECT_VERSION_API_LINK);
			}
		}).start());*/
	}

	public static BirbAddonsClient getInstance() {
		return instance;
	}
}