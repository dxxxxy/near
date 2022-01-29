package studio.dreamys.module.hud;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
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
    public static final HashMap<Entity, String> summons = new HashMap<>();

    public SummonsHUD() {
        super("Summons HUD", Category.HUD);
    }

    private static void draw(Entity key, String value) {
        Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow(value, 12, 2 + (new ArrayList<>(summons.keySet()).indexOf(key) * Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT) + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, Color.GREEN.getRGB());
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
        if (e.entity instanceof EntityArmorStand) {
            //find only summons belonging to the player
            Matcher m = Pattern.compile(Minecraft.getMinecraft().thePlayer.getName() + "'s (.*)").matcher(ChatFormatting.stripFormatting(e.entity.getName()));
            if (m.find()) {
                summons.put(e.entity, m.group(1));
            }
        }
    }

    @SubscribeEvent
    public void onRenderGameOverlay(RenderGameOverlayEvent e) {
        if (e.type.equals(RenderGameOverlayEvent.ElementType.TEXT)) {
            if (summons.size() > 0) {
                Minecraft.getMinecraft().fontRendererObj.drawStringWithShadow("Summons: ", 2, 2, Color.GREEN.getRGB());
                summons.forEach(SummonsHUD::draw);
            }
        }
    }

    @SubscribeEvent
    public void onRenderWorldLast(RenderWorldLastEvent e) {
        summons.keySet().removeIf(summon -> summon.isDead);
    }

    @SubscribeEvent
    public void onChangeWorld(EntityJoinWorldEvent e) {
        if (e.entity instanceof EntityPlayerSP) {
            summons.clear();
        }
    }
}
