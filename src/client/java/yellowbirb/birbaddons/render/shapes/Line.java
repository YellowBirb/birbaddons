package yellowbirb.birbaddons.render.shapes;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Vector3f;
import yellowbirb.birbaddons.render.CustomRenderPipelines;

public class Line implements RenderShape {

    public final float x0;
    public final float y0;
    public final float z0;
    public final float x1;
    public final float y1;
    public final float z1;
    public final int r;
    public final int g;
    public final int b;
    public final int a;
    public final boolean visibleThroughWalls;

    public Line(float x0, float y0, float z0, float x1, float y1, float z1,
                int r, int g, int b, int a, boolean visibleThroughWalls) {
        this.x0 = x0;
        this.y0 = y0;
        this.z0 = z0;
        this.x1 = x1;
        this.y1 = y1;
        this.z1 = z1;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.visibleThroughWalls = visibleThroughWalls;
    }

    @Override
    public boolean isVisibleThroughWalls() {
        return this.visibleThroughWalls;
    }

    @Override
    public RenderPipeline getSeeThroughRenderPipeline() {
        return CustomRenderPipelines.LINES_THROUGH_WALLS;
    }

    @Override
    public RenderPipeline getNonSeeThroughRenderPipeline() {
        return CustomRenderPipelines.LINES;
    }

    @Override
    public void render(PoseStack matrixStack, VertexConsumer vertexConsumer) {
        PoseStack.Pose entry = matrixStack.last();

        vertexConsumer.addVertex(entry, x0, y0, z0).setColor(r, g, b, a).setNormal(entry, new Vector3f(x1-x0, y1-y0, z1-z0).normalize());
        vertexConsumer.addVertex(entry, x1, y1, z1).setColor(r, g, b, a).setNormal(entry, new Vector3f(x1-x0, y1-y0, z1-z0).normalize());
    }
}
