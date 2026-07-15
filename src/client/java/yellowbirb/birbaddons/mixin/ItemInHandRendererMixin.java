package yellowbirb.birbaddons.mixin;

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
import yellowbirb.birbaddons.BirbAddonsClient;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    // disable drill moving in hand: no swinging the drill when mining
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
        } else {
            original.call(attack, poseStack, invert, arm);
        }
    }

    // disable drill moving in hand: doomdrill functionality
    @WrapMethod(method = "renderItem")
    public void renderItem(LivingEntity mob, ItemStack itemStack, ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Operation<Void> original) {
        if (BirbAddonsClient.drillPosition /* TODO: isEnabled */) {
            CustomData customData = itemStack.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                Tag tag = customData.copyTag().get("id");
                if (tag != null) {
                    if (tag.toString().toLowerCase().contains("drill".toLowerCase())) {
                        poseStack.translate(-0.2815F, 0.125F, -0.3F);
                        poseStack.mulPose(Axis.YP.rotationDegrees(-21.318F));
                        poseStack.mulPose(Axis.XP.rotationDegrees(19.3F));
                        poseStack.mulPose(Axis.ZP.rotationDegrees(82.7F));
                    }
                }
            }
        }
        original.call(mob, itemStack, type, poseStack, submitNodeCollector, lightCoords);
    }

    // disable drill moving in hand: fixed small visual bug if doomdrill is active and switched off of
    //                               Would otherwise flash into regular position just before changing
    @WrapMethod(method = "shouldInstantlyReplaceVisibleItem")
    private boolean shouldInstantlyReplaceVisibleItem(ItemStack currentlyVisibleItem, ItemStack expectedItem, Operation<Boolean> original) {
        if (true /* TODO: isEnabled */) {
            CustomData customData = currentlyVisibleItem.get(DataComponents.CUSTOM_DATA);
            if (customData != null) {
                Tag tag = customData.copyTag().get("id");
                if (tag != null) {
                    if (tag.toString().toLowerCase().contains("drill".toLowerCase())) {
                        return false;
                    }
                }
            }
        }
        return original.call(currentlyVisibleItem, expectedItem);
    }
}
