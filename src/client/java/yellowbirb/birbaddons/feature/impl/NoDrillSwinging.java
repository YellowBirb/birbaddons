package yellowbirb.birbaddons.feature.impl;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;

public class NoDrillSwinging {

    public static boolean enabled = true;

    // TODO: split to DoomDrill
    public static boolean drillPosition = false;

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
