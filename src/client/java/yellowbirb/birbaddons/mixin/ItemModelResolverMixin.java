package yellowbirb.birbaddons.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ItemModelResolver.class)
public class ItemModelResolverMixin {
    // disable drill moving in hand: stop swap animation that plays when fuel/durability decreases
    @WrapMethod(method = "swapAnimationScale")
    public float swapAnimationScale(ItemStack stack, Operation<Float> original) {
        if (true /* TODO: isEnabled */) {
            CustomData customData = stack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                Tag tag = customData.copyTag().get("id");
                if (tag != null) {
                    if (tag.toString().toLowerCase().contains("drill".toLowerCase())) {
                        return 0.0F;
                    }
                }
            }
        }
        return original.call(stack);
    }
}
