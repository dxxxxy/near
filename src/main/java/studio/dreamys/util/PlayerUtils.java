package studio.dreamys.util;

import com.mojang.realmsclient.gui.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ChatComponentText;

public class PlayerUtils {
    public static final String prefix = "§b§l[§f§lnear§b§l]§r ";
    public static final EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;

    public static void addMessage(String msg, String result) {
        if (player == null) {
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

        player.addChatComponentMessage(new ChatComponentText(prefix + color + msg));
    }

    public static void addMessage(ChatComponentText comp) {
        player.addChatComponentMessage(new ChatComponentText(prefix + comp));
    }


    public static void addMessage(String msg) {
        if (player == null) {
            System.out.println(msg);
            return;
        }

        player.addChatComponentMessage(new ChatComponentText(prefix + msg));
    }
}
