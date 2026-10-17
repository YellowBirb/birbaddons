package yellowbirb.birbaddons.render.shapes;

import java.util.List;

public record CompoundShape(String featureID, String ID, List<RenderShape> shapes) {

    public CompoundShape {
        shapes = List.copyOf(shapes);
    }

    public CompoundShape(String featureID, String ID, RenderShape... shapes) {
        this(featureID, ID, List.of(shapes));
    }
}