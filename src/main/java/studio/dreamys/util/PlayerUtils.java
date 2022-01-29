package studio.dreamys.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerUtils {
    public static final String prefix = "§b§l[§f§lnear§b§l]§r ";

    public static String formatCodes(String toFormat) {
        Matcher m = Pattern.compile("(?i)§[0-9A-FK-OR]").matcher(toFormat);
        String formatted = "asd";
        while (m.find()) {
            System.out.println(toFormat+ "" +formatted + "" + m.group(0) +"" + ChatFormatting.getByChar(m.group(0).charAt(2)).toString());
            formatted = toFormat.replaceAll(m.group(0), ChatFormatting.getByChar(m.group(0).charAt(2)).toString());
        }
        return formatted;
    }

    public static void addMessage(String msg, String result) {
        if (Minecraft.getMinecraft().thePlayer == null) {
            System.out.println(msg);
            return;
        }

        ChatFormatting color = ChatFormatting.RESET;
            switch (result) {
                case "success":
                    color = ChatFormatting.GREEN;
                    break;
                case "error":
                    color = ChatFormatting.RED;
                    break;
            }

        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(PlayerUtils.prefix + color + msg));
    }


    public static void addMessage(String msg) {
        if (Minecraft.getMinecraft().thePlayer == null) {
            System.out.println(msg);
            return;
        }
        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(PlayerUtils.prefix + msg));

    }
}
