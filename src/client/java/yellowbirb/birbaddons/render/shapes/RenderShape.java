package yellowbirb.birbaddons.render.shapes;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;

public interface RenderShape {

    default RenderPipeline getRenderPipeline() {
        if (isVisibleThroughWalls()) {
            return getSeeThroughRenderPipeline();
        } else {
            return getNonSeeThroughRenderPipeline();
        }
    }

    boolean isVisibleThroughWalls();

    RenderPipeline getSeeThroughRenderPipeline();

    RenderPipeline getNonSeeThroughRenderPipeline();

    void render(PoseStack matrixStack, VertexConsumer vertexConsumer);

}
