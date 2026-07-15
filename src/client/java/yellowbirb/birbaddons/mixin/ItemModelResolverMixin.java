package yellowbirb.birbaddons.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import yellowbirb.birbaddons.feature.impl.NoDrillSwinging;

@Mixin(ItemModelResolver.class)
public class ItemModelResolverMixin {
    // disable drill moving in hand: stop swap animation that plays when fuel/durability decreases
    @WrapMethod(method = "swapAnimationScale")
    public float swapAnimationScale(ItemStack stack, Operation<Float> original) {
        if (true /* TODO: isEnabled */) {
            if (NoDrillSwinging.isDrill(stack)) {
                return 0.0F;
            }
        }
        return original.call(stack);
    }
}
