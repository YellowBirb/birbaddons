package yellowbirb.birbaddons.mixin;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yellowbirb.birbaddons.config.Config;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "close", at = @At("HEAD"))
    // save config on closing the game
    public void onClose(CallbackInfo ci) {
        Config.save();
    }
}
