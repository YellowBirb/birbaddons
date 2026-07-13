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
    private void swingArm(float attack, PoseStack poseStack, int invert, HumanoidArm arm, Operation<Void> original) {
        if (true /* TODO: isEnabled */) {
            CustomData customData = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND).get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                Tag tag = customData.copyTag().get("id");
                if (tag != null) {
                    if (!tag.toString().toLowerCase().contains("drill".toLowerCase())) {
                        original.call(attack, poseStack, invert, arm);
                    }
                } else {
                    original.call(attack, poseStack, invert, arm);
                }
            } else {
                original.call(attack, poseStack, invert, arm);
            }
        }
    }

    @WrapMethod(method = "renderItem")
    public void renderItem(LivingEntity mob, ItemStack itemStack, ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Operation<Void> original) {
        if (true /* TODO: isEnabled */) {
            CustomData customData = Minecraft.getInstance().player.getItemInHand(InteractionHand.MAIN_HAND).get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                Tag tag = customData.copyTag().get("id");
                if (tag != null) {
                    if (tag.toString().toLowerCase().endsWith("drill")) {
                        poseStack.translate(BirbAddonsClient.tx, BirbAddonsClient.ty, BirbAddonsClient.tz); // tx = 0.35 ; ty = ? ; tz = ?
                        poseStack.mulPose(Axis.YP.rotationDegrees(BirbAddonsClient.y));
                        poseStack.mulPose(Axis.XP.rotationDegrees(BirbAddonsClient.x));
                        poseStack.mulPose(Axis.ZP.rotationDegrees(BirbAddonsClient.z));
                    }
                }
            }
        }
        original.call(mob, itemStack, type, poseStack, submitNodeCollector, lightCoords);
    }
}
