package yellowbirb.birbaddons.gui.mainmenu;

import yellowbirb.birbaddons.feature.Feature;

import java.util.ArrayList;
import java.util.List;

public class Category {
    public final String name;
    private final List<Feature> features;

    public Category(String name) {
        this.name = name;
        features = new ArrayList<>();
    }

    public void add(Feature f) {
        features.add(f);
    }

    public void remove(Feature f) {
        features.remove(f);
    }

    public void clear() {
        features.clear();
    }

    public List<Feature> getFeatures() {
        return features;
    }
}
