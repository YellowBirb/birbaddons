package yellowbirb.birbaddons.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.ChatFormatting;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import org.apache.commons.io.IOUtils;
import yellowbirb.birbaddons.BirbAddonsClient;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class UpdateChecker {

    public static void checkForUpdate(String apiLink) {
        LocalPlayer player = Minecraft.getInstance().player;
        // can only be called in-game, so there must for sure be a player riiiiight
        assert player != null;

        try {
            // get Versions from API
            String jsonString = IOUtils.toString(new URI(apiLink).toURL().openStream(), StandardCharsets.UTF_8);
            JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
            String mcVer = SharedConstants.getCurrentVersion().name();
            String highestVer = "";
            String highestVerID = "";

            // find latest version
            // assumes latest version is always first in Modrinth API
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonArray gameVers = jsonArray.get(i).getAsJsonObject().get("game_versions").getAsJsonArray();
                for (int j = 0; j < gameVers.size(); j++) {
                    if (gameVers.get(j).getAsString().equals(mcVer)) {
                        highestVer = jsonArray.get(i).getAsJsonObject().get("version_number").getAsString();
                        highestVerID = jsonArray.get(i).getAsJsonObject().get("id").getAsString();
                        break;
                    }
                }
                if (!highestVer.isEmpty()) {
                    break;
                }
            }

            if (highestVer.isEmpty()) {
                player.sendSystemMessage(Component.literal("§3[BirbAddons] §cCould not find any version of the mod for this Minecraft Version in Modrinth API!"));
                return;
            }

            // give player link to highest found version if it is greater than version found in Mod's Metadata
            if (isLesserVerThan(FabricLoader.getInstance().getModContainer(BirbAddonsClient.MOD_ID).get().getMetadata().getVersion().getFriendlyString(), highestVer)) {
                // TODO: new link
                String newVerLink = "https://modrinth.com/mod/birbaddons/version/" + highestVerID;
                URI verURI = new URI(newVerLink);
                MutableComponent link = Component.literal("here");
                link.setStyle(link.getStyle()
                        .applyFormats(ChatFormatting.BLUE, ChatFormatting.UNDERLINE)
                        .withClickEvent(new ClickEvent.OpenUrl(verURI))
                        .withHoverEvent(new HoverEvent.ShowText(Component.literal(newVerLink))));
                player.sendSystemMessage(Component.literal("§3[BirbAddons] §aFound new version §r§e" + highestVer + "§a of BirbAddons §r").append(link));
            }
        } catch (URISyntaxException | IOException e) {
            BirbAddonsClient.LOGGER.error(e.toString());
            player.sendSystemMessage(Component.literal("§3[BirbAddons] §cAn error occurred while trying to check for updates!"));
        }
    }

    // TODO: make better
    // assumes no characters other than numbers and dots in version string
    private static boolean isLesserVerThan(String ver1, String ver2) {
        String[] ver1arr = ver1.split("\\.");
        String[] ver2arr = ver2.split("\\.");

        for (int i = 0; i < ver1arr.length; i++) {
            if (Integer.parseInt(ver1arr[i]) > Integer.parseInt(ver2arr[i])) {
                return false;
            } else if (Integer.parseInt(ver1arr[i]) < Integer.parseInt(ver2arr[i])) {
                return true;
            }
        }
        return ver1arr.length < ver2arr.length;
    }

}
