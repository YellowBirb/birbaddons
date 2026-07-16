package yellowbirb.birbaddons.feature.impl;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import yellowbirb.birbaddons.config.ConfigBoolean;

public class NoDrillSwinging {

    public static final String ID = "NoDrillSwinging";
    public static ConfigBoolean enabled = new ConfigBoolean(ID, "enabled", true);

    // TODO: split to DoomDrill
    public static ConfigBoolean drillPosition = new ConfigBoolean(ID, "drillPosition", false);

    public static void init() {}

    public static boolean isDrill(ItemStack itemStack) {
        CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
        if (customData != null) {
            Tag tag = customData.copyTag().get("id");
            if (tag != null) {
                return tag.toString().toLowerCase().contains("drill".toLowerCase());
            }
        }
        return false;
    }

}
