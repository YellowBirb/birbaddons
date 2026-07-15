package yellowbirb.birbaddons.feature;

import yellowbirb.birbaddons.feature.impl.AdrenalineBar;
import yellowbirb.birbaddons.feature.impl.ChatTabs;
import yellowbirb.birbaddons.feature.impl.NoDrillSwinging;
import yellowbirb.birbaddons.feature.impl.Theodolite;

public class Features {

    public static void init() {

        AdrenalineBar.init();
        ChatTabs.init();
        NoDrillSwinging.init();
        Theodolite.init();

    }

}
