package yellowbirb.birbaddons.feature.impl;

import yellowbirb.birbaddons.config.ConfigBoolean;

public class DoomDrill {

    public static final String ID = "DoomDrill";
    public static ConfigBoolean enabled = new ConfigBoolean(ID, "enabled", true);

    public static void init() {}

}
