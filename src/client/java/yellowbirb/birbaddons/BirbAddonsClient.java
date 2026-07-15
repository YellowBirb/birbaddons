package yellowbirb.birbaddons;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLevelEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yellowbirb.birbaddons.event.ReceiveGameMessageEvent;
import yellowbirb.birbaddons.hud.AdrenalineBar;
import yellowbirb.birbaddons.render.RenderManager;
import yellowbirb.birbaddons.util.ChatTabs;

import java.util.concurrent.atomic.AtomicBoolean;


public class BirbAddonsClient implements ClientModInitializer {
	public static final String MOD_ID = "birbaddons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// TODO: get link
	private static final String MODRINTH_PROJECT_VERSION_API_LINK = "";
	private static final AtomicBoolean lookedForUpdate = new AtomicBoolean(false);

	public static ChatTabs.Tab chatTab = ChatTabs.Tab.ALL;

	public static boolean drillPosition = false;

	// TODO: try to completely isolate features?

	@Override
	public void onInitializeClient() {
		LOGGER.info("BirbAddons is initializing :3");

		LevelRenderEvents.END_MAIN.register(RenderManager::draw);

		ClientReceiveMessageEvents.GAME.register((message, _) -> ReceiveGameMessageEvent.receiveMessage(message));

		ClientPlayConnectionEvents.DISCONNECT.register((_, _) -> RenderManager.clear());
		ClientLevelEvents.AFTER_CLIENT_LEVEL_CHANGE.register((_, _) -> RenderManager.clear());

		Commands.init();
		DebugCommands.init();

		Sounds.init();

		HudElementRegistry.attachElementBefore(VanillaHudElements.CHAT, Identifier.fromNamespaceAndPath(MOD_ID, "before_chat"), AdrenalineBar::extract);

		AdrenalineBar.init();

		// TODO: get link
		/*ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> new Thread(() -> {
			if (!lookedForUpdate.get()) {
				lookedForUpdate.set(true);
				UpdateChecker.checkForUpdate(MODRINTH_PROJECT_VERSION_API_LINK);
			}
		}).start());*/
	}


}