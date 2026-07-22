package yellowbirb.birbaddons.config;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import yellowbirb.birbaddons.gui.InventoryButton;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConfigInventoryButtonList extends ConfigValue<List<InventoryButton>>{

    // TODO: for the love of god redo this

    private List<InventoryButton> representation;

    public ConfigInventoryButtonList(String featureKey, String valueKey, List<InventoryButton> defaultValue) {
        super(featureKey, valueKey, defaultValue);
        representation = getFromJsonElement(value);
    }

    @Override
    public List<InventoryButton> get() {
        if (hash != Config.hash()) {
            representation = super.get();
        }
        return representation;
    }

    @Override
    public void set(List<InventoryButton> value) {
        representation = value;
        super.set(representation);
    }

    @Override
    public List<InventoryButton> getFromJsonElement(JsonElement json) {
        List<InventoryButton> res = new ArrayList<>();
        JsonArray arr = json.getAsJsonArray();
        for (JsonElement ele : arr.asList()) {
            JsonObject obj = ele.getAsJsonObject();
            res.add(new InventoryButton(
                    obj.get("x").getAsInt(),
                    obj.get("y").getAsInt(),
                    obj.get("width").getAsInt(),
                    obj.get("height").getAsInt(),
                    obj.get("command").getAsString(),
                    obj.get("icon").getAsString()
            ));
        }
        return res;
    }

    @Override
    public JsonElement getAsJsonElement(List<InventoryButton> value) {
        JsonArray res = new JsonArray();
        for (InventoryButton button : value) {
            JsonObject obj = new JsonObject();
            obj.add("x", new JsonPrimitive(button.getX()));
            obj.add("y", new JsonPrimitive(button.getY()));
            obj.add("width", new JsonPrimitive(button.getWidth()));
            obj.add("height", new JsonPrimitive(button.getHeight()));
            obj.add("command", new JsonPrimitive(button.command));
            obj.add("icon", new JsonPrimitive(button.icon));
            res.add(obj);
        }
        return res;
    }

    public void add(InventoryButton button) {
        representation.add(button);
        super.set(representation);
    }

    public void remove(InventoryButton button) {
        representation.remove(button);
        super.set(representation);
    }

    public void syncRepresentation() {
        super.set(representation);
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand(Consumer<List<InventoryButton>> consumer) {
        return null;
    }
}
