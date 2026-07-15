package yellowbirb.birbaddons.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import yellowbirb.birbaddons.BirbAddonsClient;

import java.util.Optional;

public class CustomRenderPipelines {

    private static final RenderPipeline.Snippet RENDERTYPE_LINES_SNIPPET_NO_FOG = RenderPipeline.builder(RenderPipelines.MATRICES_PROJECTION_SNIPPET, RenderPipelines.GLOBALS_SNIPPET)
            .withVertexShader("core/rendertype_lines")
            .withFragmentShader("core/rendertype_lines")
            .withColorTargetState(new ColorTargetState(BlendFunction.TRANSLUCENT))
            .withCull(false)
            .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.LINES)
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

    public static final RenderPipeline LINE_STRIP = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.LINES_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/line_strip"))
                    .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.DEBUG_LINE_STRIP)
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    .build()
    );

    public static final RenderPipeline LINE_STRIP_THROUGH_WALLS = RenderPipelines.register(
            RenderPipeline.builder(RENDERTYPE_LINES_SNIPPET_NO_FOG)
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/line_strip_through_walls"))
                    .withVertexFormat(DefaultVertexFormat.POSITION_COLOR_NORMAL, VertexFormat.Mode.DEBUG_LINE_STRIP)
                    .withDepthStencilState(Optional.empty())
                    .build()
    );

    public static final RenderPipeline TRIANGLE_STRIP = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/triangle_strip"))
                    .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_STRIP)
                    .withDepthStencilState(DepthStencilState.DEFAULT)
                    .build()
    );

    public static final RenderPipeline TRIANGLE_STRIP_THROUGH_WALLS = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.DEBUG_FILLED_SNIPPET)
                    .withLocation(Identifier.fromNamespaceAndPath(BirbAddonsClient.MOD_ID, "pipeline/triangle_strip_through_walls"))
                    .withVertexFormat(DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLE_STRIP)
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
