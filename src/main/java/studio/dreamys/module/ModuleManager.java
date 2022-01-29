package studio.dreamys.module;

import studio.dreamys.module.chat.Compact;
import studio.dreamys.module.chat.Hide;
import studio.dreamys.module.cosmetics.DragonWings;
import studio.dreamys.module.dungeons.Map;
import studio.dreamys.module.misc.KneeSurgery;
import studio.dreamys.module.movement.KeepSprint;
import studio.dreamys.module.render.*;

import java.util.ArrayList;

public class ModuleManager {
    public final ArrayList<Module> modules = new ArrayList<>();

    public ModuleManager() {
        modules.add(new ClickGUI());
        modules.add(new DragonWings());
        modules.add(new SlayerESP());
        modules.add(new KneeSurgery());
        modules.add(new KeepSprint());
        modules.add(new Fullbright());
        modules.add(new Map());
        modules.add(new ShortDamage());
        modules.add(new SummonsHUD());
        modules.add(new Hide());

        //this has to be the last
        modules.add(new Compact());
    }

    public Module getModule(String name) {
        for (Module m : modules) {
            if (m.getName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }

    public ArrayList<Module> getModulesInCategory(Category c) {
        ArrayList<Module> mods = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory() == c) {
                mods.add(m);
            }
        }
        return mods;
    }
}
