package yellowbirb.birbaddons.feature;

import yellowbirb.birbaddons.config.ConfigBoolean;

public abstract class Feature {

    public String ID;
    protected ConfigBoolean enabled;

    public Feature(String id) {
        this.ID = id;
        this.enabled = new ConfigBoolean(id, "enabled", false);
    }

    public void enable() {
        enabled.set(true);
    }

    public void disable() {
        enabled.set(false);
    }

    public boolean enabled() {
        return enabled.get();
    }

    public void toggle(){
        if (enabled()) {
            disable();
        } else {
            enable();
        }
    }


}
