package yellowbirb.birbaddons.render;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.*;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.BindGroupLayouts;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import yellowbirb.birbaddons.BirbAddonsClient;

import java.util.Optional;

public class CustomRenderPipelines {

    private static final RenderPipeline.Snippet RENDERTYPE_LINES_SNIPPET_NO_FOG = RenderPipeline.builder(RenderPipelines.GLOBALS_SNIPPET)
            .withBindGroupLayout(BindGroupLayouts.MATRICES_PROJECTION)
            .withBindGroupLayout(BindGroupLayouts.FOG) // bleh
            .withVertexShader("core/rendertype_lines")
            .withFragmentShader("core/rendertype_lines")
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withCull(false)
            .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH)
            .withPrimitiveTopology(PrimitiveTopology.LINES)
            .withDepthStencilState(DepthStencilState.DEFAULT)
            .buildSnippet();

    // -----------------------------------------------------------------------------------------------------------------

    public static final RenderPipeline LINES = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/lines"))
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    .build()
    );

    public static final RenderPipeline LINES_THROUGH_WALLS = RenderPipelines.register(
            RenderPipeline.builder(RENDERTYPE_LINES_SNIPPET_NO_FOG)
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/lines_through_walls"))
                    .withDepthStencilState(Optional.empty())
                    .build()
    );

    // Currently unused: rendertype_lines offsets vertices by gl_VertexID % 2, which only works with LINES (BufferBuilder
    // duplicates each vertex into a quad). With a raw line strip this zigzags every other vertex, hence CircleXZ uses LINES.
    // Possible strip alternative: draw lines as a TRIANGLE_STRIP ribbon, emitting each point twice with the tangent as normal,
    // using a copy of rendertype_lines.vsh without the "if (lineOffset.x < 0.0) lineOffset *= -1.0" flip (it swaps the
    // ribbon's sides wherever the screen tangent passes vertical and twists it). Gives real line width and seamless joins.
    public static final RenderPipeline LINE_STRIP = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/line_strip"))
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH)
                    .withPrimitiveTopology(PrimitiveTopology.DEBUG_LINE_STRIP)
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    .build()
    );

    public static final RenderPipeline LINE_STRIP_THROUGH_WALLS = RenderPipelines.register(
            RenderPipeline.builder(RENDERTYPE_LINES_SNIPPET_NO_FOG)
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/line_strip_through_walls"))
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH)
                    .withPrimitiveTopology(PrimitiveTopology.DEBUG_LINE_STRIP)
                    .withDepthStencilState(Optional.empty())
                    .build()
    );

    // Only used by the (currently unused) Box. Strips are not batchable: consecutive strips in one draw get stitched
    // together unless padded with degenerate vertices, so prefer QUADS for new shapes.
    public static final RenderPipeline TRIANGLE_STRIP = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/triangle_strip"))
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
                    .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_STRIP)
                    .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
                    .build()
    );

    public static final RenderPipeline TRIANGLE_STRIP_THROUGH_WALLS = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/triangle_strip_through_walls"))
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
                    .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_STRIP)
                    .withDepthStencilState(Optional.empty())
                    .build()
    );

    public static final RenderPipeline QUADS = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/quads"))
                    .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
                    .build()
    );

    public static final RenderPipeline QUADS_THROUGH_WALLS = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/quads_through_walls"))
                    .withDepthStencilState(Optional.empty())
                    .build()
    );
}
