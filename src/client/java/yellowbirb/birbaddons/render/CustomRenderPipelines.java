package yellowbirb.birbaddons.render;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.*;
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

    // ----------------------------------------------------------------------------------------------------------------- // TODO: fix (squiggly lines, weird seam)

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

    public static final RenderPipeline TRIANGLE_STRIP = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/triangle_strip"))
                    .withVertexBinding(0, DefaultVertexFormat.POSITION_COLOR)
                    .withPrimitiveTopology(PrimitiveTopology.TRIANGLE_STRIP)
                    .withDepthStencilState(DepthStencilState.DEFAULT)
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
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/triangle_strip_through_walls"))
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    .build()
    );

    public static final RenderPipeline QUADS_THROUGH_WALLS = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/triangle_strip_through_walls"))
                    .withDepthStencilState(Optional.empty())
                    .build()
    );
}
