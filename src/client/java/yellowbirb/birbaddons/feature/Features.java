package yellowbirb.birbaddons.feature;

import yellowbirb.birbaddons.feature.impl.*;

import java.util.List;

public class Features {

    public final AdrenalineBar adrenalineBar;
    public final ChatTabs chatTabs;
    public final DoomDrill doomDrill;
    public final NoSwing noSwing;
    public final Theodolite theodolite;

    public List<Feature> featureList;

    public Features() {
        adrenalineBar = new AdrenalineBar();
        chatTabs = new ChatTabs();
        doomDrill = new DoomDrill();
        noSwing = new NoSwing();
        theodolite = new Theodolite();

        featureList = List.of(adrenalineBar, chatTabs, doomDrill, noSwing, theodolite);
    }

}
