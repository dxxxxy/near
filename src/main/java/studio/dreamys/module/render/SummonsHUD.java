package studio.dreamys.module.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import studio.dreamys.module.Category;
import studio.dreamys.module.Module;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SummonsHUD extends Module {
    public static HashMap<Entity, String> summons = new HashMap<>();

    public SummonsHUD() {
        super("Summons HUD", Category.HUD);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
        if (e.entity instanceof EntityArmorStand) {
            Matcher m = Pattern.compile(Minecraft.getMinecraft().thePlayer.getName() + "'s (.*)").matcher(ChatFormatting.stripFormatting(e.entity.getName()));
            if (m.find()) summons.put(e.entity, m.group(1));
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent e) {
        if (e.type.equals(RenderGameOverlayEvent.ElementType.TEXT)) {
            //render each summon
            summons.forEach((key, value) -> Minecraft.getMinecraft().fontRendererObj.drawString(value, 2, 2 + (new ArrayList<>(summons.keySet()).indexOf(key) * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT), Color.GREEN.getRGB()));
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent e) {
        //remove when dead
        summons.entrySet().removeIf(summon -> summon.getKey().isDead);
    }
}
