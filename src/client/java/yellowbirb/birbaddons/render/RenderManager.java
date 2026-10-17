package yellowbirb.birbaddons.render;

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import yellowbirb.birbaddons.render.shapes.CompoundShape;
import yellowbirb.birbaddons.render.shapes.RenderShape;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class RenderManager {

    private static final Map<String, Map<String, CompoundShape>> shapeMap = new LinkedHashMap<>();

    public static void add(CompoundShape shape) {
        shapeMap.computeIfAbsent(shape.featureID(), _ -> new LinkedHashMap<>()).put(shape.ID(), shape);
    }

    public static CompoundShape get(String featureID, String ID) {
        Map<String, CompoundShape> featureShapes = shapeMap.get(featureID);
        return featureShapes == null ? null : featureShapes.get(ID);
    }

    public static boolean contains(String featureID, String ID) {
        return get(featureID, ID) != null;
    }

    public static CompoundShape remove(String featureID, String ID) {
        Map<String, CompoundShape> featureShapes = shapeMap.get(featureID);
        if (featureShapes == null) {
            return null;
        }

        CompoundShape removed = featureShapes.remove(ID);
        if (featureShapes.isEmpty()) {
            shapeMap.remove(featureID);
        }
        return removed;
    }

    public static CompoundShape remove(CompoundShape shape) {
        return remove(shape.featureID(), shape.ID());
    }

    public static void clear(String featureID) {
        shapeMap.remove(featureID);
    }

    public static void clear() {
        shapeMap.clear();
    }

    public static void draw(LevelRenderContext ctx) {
        List<RenderShape> shapes = new ArrayList<>();
        for (Map<String, CompoundShape> featureShapes : shapeMap.values()) {
            for (CompoundShape compound : featureShapes.values()) {
                shapes.addAll(compound.shapes());
            }
        }

        if (!shapes.isEmpty()) {
            Renderer.drawShapes(ctx, shapes);
        }
    }

}