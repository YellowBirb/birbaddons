package yellowbirb.birbaddons.config;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import yellowbirb.birbaddons.BirbAddonsClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {

    // Heavy "Inspiration" from https://github.com/WhatYouThing/NoFrills

    private static final Path configDir = FabricLoader.getInstance().getConfigDir().resolve(BirbAddonsClient.MOD_ID);
    private static final Path configFile = configDir.resolve("config.json");
    private static JsonObject configJson = new JsonObject();
    private static int hash = 0;

    public static void load() {
        if (Files.isReadable(configFile)) {
            try {
                configJson = JsonParser.parseString(Files.readString(configFile)).getAsJsonObject();
            } catch (IOException e) {
                BirbAddonsClient.LOGGER.error("An error occurred while trying to parse config file", e);
            }
        }
        else {
            save();
        }
        hash(true);
    }

    public static void save() {
        if (!Files.exists(configDir)) {
            try {
                Files.createDirectory(configDir);
            } catch (IOException e) {
                BirbAddonsClient.LOGGER.error("An error occurred while trying to create config directory", e);
            }
        }
        try {
            Files.writeString(configFile, new GsonBuilder().setPrettyPrinting().create().toJson(configJson));
        } catch (IOException e) {
            BirbAddonsClient.LOGGER.error("An error occurred while trying to write to config file", e);
        }
    }

    public static int hash(boolean recompute) {
        if (recompute) {
            hash = configJson.hashCode();
        }
        return hash;
    }

    public static int hash() {
        return hash;
    }

    public static JsonObject get() {
        return configJson;
    }
}
