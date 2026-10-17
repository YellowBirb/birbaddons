package yellowbirb.birbaddons.config;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import yellowbirb.birbaddons.BirbAddonsClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ConfigList<T> extends ConfigValue<List<T>>{

    private static final Gson GSON = new Gson();

    private final Type type;

    private List<T> representation;
    private int representationSourceHash;

    public ConfigList(String featureKey, String valueKey, Class<T> elementType, List<T> defaultValue) {
        this(featureKey, valueKey, (Type) elementType, defaultValue);
    }

    /**
     * For generic element types, e.g. {@code new TypeToken<List<String>>(){}.getType()}.
     */
    public ConfigList(String featureKey, String valueKey, Type elementType, List<T> defaultValue) {
        super(featureKey, valueKey, defaultValue);
        this.type = elementType;
    }

    @Override
    public List<T> get() {
        load();
        if (representation == null || representationSourceHash != value.hashCode()) {
            representation = getFromJsonElement(value);
            representationSourceHash = value.hashCode();
        }
        return representation;
    }

    @Override
    public void set(List<T> value) {
        super.set(value);
        representation = new ArrayList<>(value);
        representationSourceHash = this.value.hashCode();
    }

    public void sync() {
        set(get());
    }

    @Override
    public List<T> getFromJsonElement(JsonElement json) {
        List<T> res = new ArrayList<>();
        if (!json.isJsonArray()) {
            BirbAddonsClient.LOGGER.warn("Could not load ConfigList {}.{} from config because it is not an array, returning empty list", this.featureKey, this.valueKey);
            return res;
        }

        for (JsonElement element : json.getAsJsonArray()) {
            res.add(getElementFromJsonElement(element));
        }
        return res;
    }

    public T getElementFromJsonElement(JsonElement element) {
        return GSON.fromJson(element, type);
    }

    @Override
    public JsonElement getAsJsonElement(List<T> value) {
        JsonArray array = new JsonArray();
        for (T element : value) {
            array.add(getElementAsJsonElement(element));
        }
        return array;
    }

    public JsonElement getElementAsJsonElement(T value) {
        return GSON.toJsonTree(value, type);
    }

    public void operation(Consumer<List<T>> consumer) {
        consumer.accept(get());
        sync();
    }

    public void update(int index, Consumer<T> mutator) {
        mutator.accept(get().get(index));
        sync();
    }

    public void add(T value) {
        operation(l -> l.add(value));
    }

    public boolean remove(T value) {
        boolean[] removed = new boolean[1];
        operation(l -> removed[0] = l.remove(value));
        return removed[0];
    }

    public int indexOf(T element) {
        return get().indexOf(element);
    }

    public void set(int index, T element) {
        operation(l -> l.set(index, element));
    }

    @Override
    public LiteralArgumentBuilder<FabricClientCommandSource> getCommand(Consumer<List<T>> consumer) {
        return ClientCommands.literal(valueKey);
    }
}