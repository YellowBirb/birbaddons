package yellowbirb.birbaddons.render;

import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.systems.ScissorState;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import yellowbirb.birbaddons.BirbAddonsClient;
import yellowbirb.birbaddons.render.shapes.RenderShape;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;

// Only works for list topologies (LINES, QUADS, ...): the meshes of a draw are concatenated, which would stitch strips together.
public class Renderer {

    private static final StagedVertexBuffer stagedBuffer = new StagedVertexBuffer(() -> "BirbAddons Renderer Buffer", RenderType.SMALL_BUFFER_SIZE);

    public static void drawShapes(LevelRenderContext ctx, List<RenderShape> shapes) {

        Map<RenderPipeline, List<RenderShape>> shapesByPipeline = new LinkedHashMap<>();
        for (RenderShape shape : shapes) {
            shapesByPipeline.computeIfAbsent(shape.getRenderPipeline(), _ -> new ArrayList<>()).add(shape);
        }

        PoseStack matrices = ctx.poseStack();
        Vec3 cam = ctx.levelState().cameraRenderState.pos;

        matrices.pushPose();
        matrices.translate(-cam.x, -cam.y, -cam.z);

        Map<RenderPipeline, StagedVertexBuffer.Draw> draws = new LinkedHashMap<>();
        for (Map.Entry<RenderPipeline, List<RenderShape>> entry : shapesByPipeline.entrySet()) {
            RenderPipeline pipeline = entry.getKey();
            VertexFormat format = pipeline.getVertexFormatBinding(0);
            assert format != null;

            StagedVertexBuffer.Draw draw = stagedBuffer.appendDraw(format, pipeline.getPrimitiveTopology());
            VertexConsumer builder = stagedBuffer.getVertexBuilder(draw);
            for (RenderShape shape : entry.getValue()) {
                shape.render(matrices, builder);
            }
            draws.put(pipeline, draw);
        }

        matrices.popPose();

        stagedBuffer.upload();

        draw(Minecraft.getInstance(), draws);

        stagedBuffer.endFrame();
    }

    private static void draw(Minecraft client, Map<RenderPipeline, StagedVertexBuffer.Draw> draws) {

        GpuBufferSlice dynamicTransforms = RenderSystem.getDynamicUniforms()
                .writeTransform(RenderSystem.getModelViewMatrixCopy(), new Vector4f(1f, 1f, 1f, 1f), new Vector3f(), new Matrix4f());

        try (RenderPass renderPass = RenderSystem.getDevice()
                .createCommandEncoder()
                .createRenderPass(() -> BirbAddonsClient.MOD_ID + " rendering", client.gameRenderer.mainRenderTarget().getColorTextureView(), Optional.empty(),client.gameRenderer.mainRenderTarget().getDepthTextureView(), OptionalDouble.empty())) {

            ScissorState scissorState = RenderSystem.getScissorStateForRenderTypeDraws();
            if (scissorState.enabled()) {
                renderPass.enableScissor(scissorState.x(), scissorState.y(), scissorState.width(), scissorState.height());
            }

            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.setUniform("DynamicTransforms", dynamicTransforms);

            for (Map.Entry<RenderPipeline, StagedVertexBuffer.Draw> entry : draws.entrySet()) {
                StagedVertexBuffer.ExecuteInfo info = stagedBuffer.getExecuteInfo(entry.getValue());
                if (info == null) {
                    continue;
                }

                renderPass.setPipeline(entry.getKey());

                renderPass.setVertexBuffer(0, info.vertexBuffer().slice());
                renderPass.setIndexBuffer(info.indexBuffer(), info.indexType());

                renderPass.drawIndexed(info.indexCount(), 1, info.firstIndex(), info.baseVertex(), 0);
            }
        }
    }

    public static void close() {
        stagedBuffer.close();
    }
}