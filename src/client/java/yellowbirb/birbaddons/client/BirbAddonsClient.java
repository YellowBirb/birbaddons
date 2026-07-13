package yellowbirb.birbaddons.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class BirbAddonsClient implements ClientModInitializer {
	public static final String MOD_ID = "birbaddons";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static ChatTab chatTab = ChatTab.ALL;

	@Override
	public void onInitializeClient() {
		LOGGER.info("BirbAddons is initializing :3");
	}

	private static String removeFormatting(String string) {
		StringBuilder stringBuilder = new StringBuilder(string);
		int deleted = 0;
		for (int i = 0; i < string.length(); i++) {
			while (string.charAt(i) == '§') {
				stringBuilder.deleteCharAt(i-deleted);
				deleted++;
				i++;
				stringBuilder.deleteCharAt(i-deleted);
				deleted++;
				i++;
			}
		}
		return stringBuilder.toString();
	}

	public static boolean chatFilter(Component text) {
		String message = removeFormatting(text.getString());

		return switch (chatTab) {
			case ALL -> true;
			case PARTY -> message.startsWith("Party > ") || message.startsWith("P > ") ||
					message.endsWith("has invited you to join their party!") ||
					message.endsWith("to the party! They have 60 seconds to accept.") ||
					message.equals("The party was disbanded because all invites expired and the party was empty") ||
					message.endsWith("has disbanded the party!") ||
					message.endsWith("has disconnected, they have 5 minutes to rejoin before they are removed from the party.") ||
					message.endsWith("joined the party.") || message.endsWith("has left the party.") ||
					message.endsWith("has been removed from the party.") || message.startsWith("The party was transferred to ") ||
					(message.startsWith("Kicked ") && message.endsWith(" because they were offline."));
			case GUILD -> message.startsWith("Guild > ") || message.startsWith("G > ");
			case PRIVATE -> message.startsWith("To ") || message.startsWith("From ") ||
					message.startsWith("Friend > ");
			case COOP -> message.startsWith("Co-op > ");
		};
	}

	public enum ChatTab {
		ALL,
		PARTY,
		GUILD,
		PRIVATE,
		COOP
	}
}