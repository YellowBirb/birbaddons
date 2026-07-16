package yellowbirb.birbaddons;

import net.fabricmc.api.ClientModInitializer;
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
	public static final String MOD_ID = "birbaddons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// TODO: get link
	private static final String MODRINTH_PROJECT_VERSION_API_LINK = "";
	private static final AtomicBoolean lookedForUpdate = new AtomicBoolean(false);

	@Override
	public void onInitializeClient() {
		LOGGER.info("BirbAddons is initializing :3");

		Config.load();

		LevelRenderEvents.END_MAIN.register(RenderManager::draw);

		ClientReceiveMessageEvents.GAME.register((message, _) -> ReceiveGameMessageEvent.receiveMessage(message));

		Commands.init();
		DebugCommands.init();

		Sounds.init();

		Features.init();

		// TODO: get link
		/*ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> new Thread(() -> {
			if (!lookedForUpdate.get()) {
				lookedForUpdate.set(true);
				UpdateChecker.checkForUpdate(MODRINTH_PROJECT_VERSION_API_LINK);
			}
		}).start());*/
	}


}