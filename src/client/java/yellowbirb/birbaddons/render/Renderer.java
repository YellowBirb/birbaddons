package yellowbirb.birbaddons.render;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.ScissorState;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import yellowbirb.birbaddons.BirbAddonsClient;
import yellowbirb.birbaddons.render.shapes.RenderShape;

import java.util.OptionalDouble;
import java.util.OptionalInt;

public class Renderer {

    private static final ByteBufferBuilder allocator = new ByteBufferBuilder(RenderType.SMALL_BUFFER_SIZE);

    public static void drawShape(LevelRenderContext ctx, RenderShape shape) {
        PoseStack matrices = ctx.poseStack();
        Vec3 cam = ctx.levelState().cameraRenderState.pos;

        matrices.pushPose();
        matrices.translate(-cam.x, -cam.y, -cam.z);

        RenderPipeline pipeline = shape.getRenderPipeline();

        BufferBuilder bufferBuilder =
                new BufferBuilder(allocator, pipeline.getVertexFormatMode(), pipeline.getVertexFormat());

        shape.render(matrices, bufferBuilder);

        draw(shape.getRenderPipeline(), bufferBuilder.buildOrThrow());

        matrices.popPose();
    }

    private static void draw(RenderPipeline pipeline, MeshData buffer) {
        GpuBuffer vertices = pipeline.getVertexFormat().uploadImmediateVertexBuffer(buffer.vertexBuffer());

        GpuBuffer indices;
        VertexFormat.IndexType indexType;

        if (buffer.indexBuffer() == null) {
            RenderSystem.AutoStorageIndexBuffer shapeIndexBuffer = RenderSystem.getSequentialBuffer(buffer.drawState().mode());
            indices = shapeIndexBuffer.getBuffer(buffer.drawState().indexCount());
            indexType = shapeIndexBuffer.type();
        } else {
            indices = pipeline.getVertexFormat().uploadImmediateIndexBuffer(buffer.indexBuffer());
            indexType = buffer.drawState().indexType();
        }

        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                .writeTransform(RenderSystem.getModelViewMatrix(), new Vector4f(1f, 1f, 1f, 1f), new Vector3f(), new Matrix4f());

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> BirbAddonsClient.MOD_ID + "rendering", Minecraft.getInstance().getMainRenderTarget().getColorTextureView(), OptionalInt.empty(), Minecraft.getInstance().getMainRenderTarget().getDepthTextureView(), OptionalDouble.empty())) {

            renderPass.setPipeline(pipeline);

            ScissorState scissorState = RenderSystem.getScissorStateForRenderTypeDraws();
            if (scissorState.enabled()) {
                renderPass.enableScissor(scissorState.x(), scissorState.y(), scissorState.width(), scissorState.height());
            }

            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicTransforms);


            renderPass.setVertexBuffer(0, vertices);

            renderPass.setIndexBuffer(indices, indexType);


            renderPass.drawIndexed(0, 0, buffer.drawState().indexCount(), 1);
        }

        buffer.close();
    }

    public static void close() {
        allocator.close();
    }
}
