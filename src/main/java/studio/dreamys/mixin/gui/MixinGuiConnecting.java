package studio.dreamys.mixin.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.NetworkManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import studio.dreamys.util.RenderUtils;

import java.util.concurrent.atomic.AtomicInteger;

@Mixin(GuiConnecting.class)
public abstract class MixinGuiConnecting extends GuiScreen {

    @Shadow
    private NetworkManager networkManager;

    @Shadow
    @Final
    private static Logger logger;

    @Shadow
    private boolean cancel;

    @Shadow
    @Final
    private GuiScreen previousGuiScreen;

    @Shadow
    @Final
    private static AtomicInteger CONNECTION_ID;

    @Overwrite
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        FontRenderer fr = Minecraft.getMinecraft().fontRendererObj;
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());

        drawDefaultBackground();

        RenderUtils.drawLoadingCircle(scaledResolution.getScaledWidth() / 2, scaledResolution.getScaledHeight() / 4 + 70);

        String ip = "Unknown";

        final ServerData serverData = mc.getCurrentServerData();
        if(serverData != null)
            ip = serverData.serverIP;

        fr.drawString("Connecting to", (scaledResolution.getScaledWidth() / 2) - fr.getStringWidth("Connecting to") / 2F, scaledResolution.getScaledHeight() / 4 + 110, 0xFFFFFF, true);
        fr.drawString(ip, scaledResolution.getScaledWidth() / 2 - fr.getStringWidth(ip) / 2F, scaledResolution.getScaledHeight() / 4 + 120, 0x5281FB, true);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
