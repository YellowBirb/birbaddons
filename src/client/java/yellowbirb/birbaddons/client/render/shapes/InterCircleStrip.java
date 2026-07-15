package yellowbirb.birbaddons.client.render.shapes;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Vector3f;
import yellowbirb.birbaddons.client.render.CustomRenderPipelines;

import static java.lang.Math.*;

public class InterCircleStrip implements RenderShape{

    public final float radius1;
    public final float radius2;
    public final int segmentAmount;
    public final float x;
    public final float y1; // assumes same center and segmentAmount for both circles
    public final float y2;
    public final float z;
    public final int r;
    public final int g;
    public final int b;
    public final int a;
    public final boolean visibleThroughWalls;

    public InterCircleStrip(float radius1, float radius2, int segmentAmount, float x, float y1, float y2, float z, int r, int g, int b, int a, boolean visibleThroughWalls) {
        this.radius1 = radius1;
        this.radius2 = radius2;
        this.segmentAmount = segmentAmount;
        this.x = x;
        this.y1 = y1;
        this.y2 = y2;
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
        PoseStack.Pose entry = matrixStack.last();

        int i1 = 0;
        int i2 = 0;
        Vector3f c1Vertex;
        Vector3f c2Vertex;

        float vy1 = y1;
        float vy2 = y2;
        float vradius1 = radius1;
        float vradius2 = radius2;

        while (i1 <= segmentAmount || i2 <= segmentAmount) {
            c1Vertex = getCircleVertex(i1, segmentAmount, vradius1, x, vy1, z);
            c2Vertex = getCircleVertex(i2, segmentAmount, vradius2, x, vy2, z);
            vertexConsumer.addVertex(entry, c1Vertex).setColor(r, g, b, a);
            vertexConsumer.addVertex(entry, c2Vertex).setColor(r, g, b, a);
            i1++;
            i2++;
        }

        //back face, so its visible from both sides

        i1 = 0;
        i2 = 0;

        vy1 = y2;
        vy2 = y1;
        vradius1 = radius2;
        vradius2 = radius1;

        while (i1 <= segmentAmount || i2 <= segmentAmount) {
            c1Vertex = getCircleVertex(i1, segmentAmount, vradius1, x, vy1, z);
            c2Vertex = getCircleVertex(i2, segmentAmount, vradius2, x, vy2, z);
            vertexConsumer.addVertex(entry, c1Vertex).setColor(r, g, b, a);
            vertexConsumer.addVertex(entry, c2Vertex).setColor(r, g, b, a);
            i1++;
            i2++;
        }
    }

    private Vector3f getCircleVertex(int i, int segmentAmount, float radius, float x, float y, float z) {
        return new Vector3f(circleX(i, segmentAmount, radius, x), y, circleZ(i, segmentAmount, radius, z));
    }

    private float circleX(int i, int segmentAmount, float radius, float x) {
        float angle = (float) (((double) i /segmentAmount) * (2 * PI));
        float xOffset = (float) (cos(angle) * radius);
        return x + xOffset;
    }

    private float circleZ(int i, int segmentAmount, float radius, float z) {
        float angle = (float) (((double) i /segmentAmount) * (2 * PI));
        float zOffset = (float) (sin(angle) * radius);
        return z + zOffset;
    }
}