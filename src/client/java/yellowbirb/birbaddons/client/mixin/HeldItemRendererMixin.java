package yellowbirb.birbaddons.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import org.spongepowered.asm.mixin.Mixin;
import yellowbirb.birbaddons.client.BirbAddonsClient;

@Mixin(ItemInHandRenderer.class)
public class HeldItemRendererMixin {

    @WrapMethod(method = "swingArm")
    private void swingArm(float f, PoseStack poseStack, int i, HumanoidArm humanoidArm, Operation<Void> original) {
        if (false) {
            original.call(f, poseStack, i, humanoidArm);
        }
    }

    @WrapMethod(method = "renderItem")
    public void renderItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, Operation<Void> original) {
        if (true) {
            poseStack.translate(BirbAddonsClient.tx, BirbAddonsClient.ty, BirbAddonsClient.tz);
            poseStack.mulPose(Axis.YP.rotationDegrees(BirbAddonsClient.y));
            poseStack.mulPose(Axis.XP.rotationDegrees(BirbAddonsClient.x));
            poseStack.mulPose(Axis.ZP.rotationDegrees(BirbAddonsClient.z));
        }
        original.call(livingEntity, itemStack, itemDisplayContext, poseStack, submitNodeCollector, i);
    }

}
