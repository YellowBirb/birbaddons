package yellowbirb.birbaddons.mixin;

import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import yellowbirb.birbaddons.render.Renderer;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    // Renderer
    @Inject(at = @At("RETURN"), method = "close")
    private void onGameRendererClose(CallbackInfo info) {
        Renderer.close();
    }
}
