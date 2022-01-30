package studio.dreamys.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class PlayerUtils {
    public static final String prefix = "§b§l[§f§lnear§b§l]§r ";

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


// --Commented out by Inspection START (1/30/2022 6:17 PM):
//    public static void addMessage(String msg) {
//        if (Minecraft.getMinecraft().thePlayer == null) {
//            System.out.println(msg);
//            return;
//        }
//        Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText(PlayerUtils.prefix + msg));
//
//    }
// --Commented out by Inspection STOP (1/30/2022 6:17 PM)
}
