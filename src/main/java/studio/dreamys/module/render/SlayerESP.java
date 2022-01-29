package studio.dreamys.module.render;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import studio.dreamys.module.Category;
import studio.dreamys.module.Module;
import studio.dreamys.near;
import studio.dreamys.settings.Setting;
import studio.dreamys.util.RenderUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.regex.Pattern;

public class SlayerESP extends Module {
    public static final HashMap<Entity, Vec3> slayers = new HashMap<>();

    public SlayerESP() {
        super("SlayerESP", Category.RENDER);
        near.settingsManager.rSetting(new Setting("Ring", this, true));
        near.settingsManager.rSetting(new Setting("Tracers", this, true));
    }

    //"revenant sycophant" "revenant champion" "deformed revenant" "atoned champion" "atoned revenant" "revenant horror" "atoned horror"
    //"tarantula vermin" "tarantula beast" "mutant tarantula" "tarantula broodfather"
    //"pack enforcer" "sven follower" "sven alpha" "sven packmaster"
    //"voidling devotee" "voidling radical" "voidcrazed maniac" "voidgloom seraph"
    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
        //register slayer entity
        if (e.entity instanceof EntityArmorStand)
            if (Pattern.compile("tarantula (?:broodfather|vermin)|revenant sycophant|(?:revenant|atoned) champion|void(?:crazed maniac|gloom seraph|ling (?:devotee|radical))|deformed revenant|mutant tarantula|(?:(?:revenant|atoned) horro|(?:sven (?:packmast|follow)|pack enforc)e)r|tarantula beast|atoned revenant|sven alpha").matcher(e.entity.getName().toLowerCase()).find())
                slayers.put(e.entity, e.entity.getPositionVector());
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent e) {
        if (slayers.size() > 0) {
            slayers.forEach((key, value) -> {
                //draw ring
                if (near.settingsManager.getSettingByName(this, "Ring").getValBoolean())
                    RenderUtils.drawBox(key, Pattern.compile("tarantula broodfather|voidgloom seraph|(?:revenant|atoned) horror|sven packmaster").matcher(key.getName().toLowerCase()).find() ? Color.getHSBColor((System.currentTimeMillis() % 1000) / 1000F, 0.8F, 1F).getRGB() : Color.WHITE.getRGB());

                //draw tracer
                if (near.settingsManager.getSettingByName(this, "Tracers").getValBoolean())
                    RenderUtils.drawTrace(key, Pattern.compile("tarantula broodfather|voidgloom seraph|(?:revenant|atoned) horror|sven packmaster").matcher(key.getName().toLowerCase()).find() ? Color.getHSBColor((System.currentTimeMillis() % 1000) / 1000F, 0.8F, 1F).getRGB() : Color.WHITE.getRGB(), e.partialTicks);
            });
            //unregister entity
            slayers.entrySet().removeIf(slayer -> slayer.getKey().isDead);
        }
    }
}
