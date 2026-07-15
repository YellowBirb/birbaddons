package yellowbirb.birbaddons.client.render.shapes;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import yellowbirb.birbaddons.client.render.CustomRenderPipelines;

public class Box implements RenderShape{
    public final float lengthX;
    public final float height;
    public final float lengthZ;
    public final float x;
    public final float y;
    public final float z;
    public final int r;
    public final int g;
    public final int b;
    public final int a;
    public final boolean visibleThroughWalls;

    public Box(float lengthX, float height, float lengthZ, float x, float y, float z, int r, int g, int b, int a, boolean visibleThroughWalls) {
        this.lengthX = lengthX;
        this.height = height;
        this.lengthZ = lengthZ;
        this.x = x;
        this.y = y;
        this.z = z;
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        this.visibleThroughWalls = visibleThroughWalls;
    }

    @Override
    public boolean isVisibleThroughWalls() {
        return visibleThroughWalls;
    }

    @Override
    public RenderPipeline getSeeThroughRenderPipeline() {
        return CustomRenderPipelines.TRIANGLE_STRIP_THROUGH_WALLS;
    }

    @Override
    public RenderPipeline getNonSeeThroughRenderPipeline() {
        return CustomRenderPipelines.TRIANGLE_STRIP;
    }

    @Override
    public void render(PoseStack matrixStack, VertexConsumer vertexConsumer) {
        Matrix4f matrix4f = matrixStack.last().pose();

        vertexConsumer.addVertex(matrix4f, x, y, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y, z).setColor(r, g, b, a);

        vertexConsumer.addVertex(matrix4f, x, y, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y + height, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y + height, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y + height, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y + height, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y + height, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y + height, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y + height, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y + height, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y + height, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y + height, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y + height, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y + height, z).setColor(r, g, b, a);

        vertexConsumer.addVertex(matrix4f, x + lengthX, y + height, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y + height, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y + height, z + lengthZ).setColor(r, g, b, a);

        vertexConsumer.addVertex(matrix4f, x + lengthX, y + height, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y + height, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y + height, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y + height, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y + height, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y + height, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y + height, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y + height, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x + lengthX, y + height, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y + height, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y + height, z + lengthZ).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y + height, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y, z + lengthZ).setColor(r, g, b, a);

        vertexConsumer.addVertex(matrix4f, x, y, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y, z).setColor(r, g, b, a);
        vertexConsumer.addVertex(matrix4f, x, y, z).setColor(r, g, b, a);
    }
}