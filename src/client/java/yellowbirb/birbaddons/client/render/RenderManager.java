package yellowbirb.birbaddons.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import yellowbirb.birbaddons.client.render.shapes.RenderShape;

import java.util.ArrayList;
import java.util.List;

public class RenderManager {

    private static final List<RenderShape> shapeList = new ArrayList<>();

    public static void add(RenderShape shape) {
        shapeList.add(shape);
    }

    public static RenderShape remove(int index) {
        return shapeList.remove(index);
    }

    public static RenderShape removeFirst() {
        return shapeList.removeFirst();
    }

    public static RenderShape removeLast() {
        return shapeList.removeLast();
    }

    public static void clear() {
        shapeList.clear();
    }

    public static void draw(LevelRenderContext ctx) {
        for (RenderShape shape : shapeList) {
            Renderer.drawShape(ctx, shape);
        }
    }

}
