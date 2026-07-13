package yellowbirb.birbaddons.client.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import yellowbirb.birbaddons.client.BirbAddonsClient;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @WrapMethod(method = "swingArm")
    private void swingArm(float f, PoseStack poseStack, int i, HumanoidArm humanoidArm, Operation<Void> original) {
        if (true /* TODO: isEnabled */) {
            CustomData customData = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND).get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                Tag tag = customData.copyTag().get("id");
                if (tag != null) {
                    if (!tag.toString().toLowerCase().contains("drill".toLowerCase())) {
                        original.call(f, poseStack, i, humanoidArm);
                    }
                } else {
                    original.call(f, poseStack, i, humanoidArm);
                }
            } else {
                original.call(f, poseStack, i, humanoidArm);
            }
        } else {
            original.call(f, poseStack, i, humanoidArm);
        }
    }

    @WrapMethod(method = "renderItem")
    public void renderItem(LivingEntity livingEntity, ItemStack itemStack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int i, Operation<Void> original) {
        if (true /* TODO: isEnabled */) {
            CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                Tag tag = customData.copyTag().get("id");
                if (tag != null) {
                    if (tag.toString().toLowerCase().contains("drill".toLowerCase())) {
                        poseStack.translate(BirbAddonsClient.tx, BirbAddonsClient.ty, BirbAddonsClient.tz); // tx = 0.35 ; ty = ? ; tz = ?
                        poseStack.mulPose(Axis.YP.rotationDegrees(BirbAddonsClient.y));
                        poseStack.mulPose(Axis.XP.rotationDegrees(BirbAddonsClient.x));
                        poseStack.mulPose(Axis.ZP.rotationDegrees(BirbAddonsClient.z));
                    }
                }
            }
        }
        original.call(livingEntity, itemStack, itemDisplayContext, poseStack, submitNodeCollector, i);
    }

    @WrapMethod(method = "shouldInstantlyReplaceVisibleItem")
    private boolean shouldInstantlyReplaceVisibleItem(ItemStack itemStack, ItemStack itemStack2, Operation<Boolean> original) {
        if (true /* TODO: isEnabled */) {
            CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                Tag tag = customData.copyTag().get("id");
                if (tag != null) {
                    if (tag.toString().toLowerCase().contains("drill".toLowerCase())) {
                        return false;
                    }
                }
            }
        }
        return original.call(itemStack, itemStack2);
    }
}
