package yellowbirb.birbaddons.client;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;

public class Sounds {
    // TODO: split
    public static final SoundEvent ADRENALINEACTIVATE = registerSound("adrenalineactivate");
    public static final SoundEvent FULLADRENALINE = registerSound("fulladrenaline");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, identifier, SoundEvent.createVariableRangeEvent(identifier));
    }

    public static void init() {

    }
}
