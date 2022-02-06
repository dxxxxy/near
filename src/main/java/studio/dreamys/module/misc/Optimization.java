package studio.dreamys.module.misc;

import studio.dreamys.module.Category;
import studio.dreamys.module.Module;
import studio.dreamys.settings.Setting;

public class Optimization extends Module {
    public Optimization() {
        super("Optimization", Category.MISC);

        set(new Setting("Culling", this, true));
    }
}
