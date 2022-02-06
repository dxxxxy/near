package studio.dreamys.mixin.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ChatLine;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import studio.dreamys.near;

import java.util.List;

import static net.minecraft.client.gui.Gui.drawRect;

@Mixin(GuiNewChat.class)
public class MixinGuiNewChat {

    @Final
    @Shadow
    private Minecraft mc = Minecraft.getMinecraft();

    @Final
    @Shadow
    private List<String> sentMessages = Lists.newArrayList();

    @Final
    @Shadow
    private List<ChatLine> chatLines = Lists.newArrayList();

    @Final
    @Shadow
    private List<ChatLine> drawnChatLines = Lists.newArrayList();

    @Shadow
    private int scrollPos;

    @Shadow
    private boolean isScrolled;

    @Shadow
    public static int calculateChatboxHeight(float scale) {
        return 0;
    }

    public boolean getChatOpen() {
        return mc.currentScreen instanceof GuiChat;
    }

    @Overwrite
    public int getChatHeight() {
        return calculateChatboxHeight(getChatOpen() ? (near.moduleManager.getModule("Longer").isToggled() ? mc.gameSettings.chatHeightFocused * 2 : mc.gameSettings.chatHeightFocused) : mc.gameSettings.chatHeightUnfocused);
    }

    @Shadow
    public int getChatWidth() {
        return 0;
    }

    public float getChatScale() {
        return mc.gameSettings.chatScale;
    }

    public int getLineCount() {
        return getChatHeight() / 9;
    }

    @Overwrite
    public void drawChat(int updateCounter) {
        if (mc.gameSettings.chatVisibility != EntityPlayer.EnumChatVisibility.HIDDEN) {
            int i = getLineCount();
            boolean flag = false;
            int j = 0;
            int k = drawnChatLines.size();
            float f = mc.gameSettings.chatOpacity * 0.9F + 0.1F;

            if (k > 0) {
                if (getChatOpen()) {
                    flag = true;
                }

                float f1 = getChatScale();
                int l = MathHelper.ceiling_float_int((float) getChatWidth() / f1);
                GlStateManager.pushMatrix();
                GlStateManager.translate(2.0F, 20.0F, 0.0F);
                GlStateManager.scale(f1, f1, 1.0F);

                for (int i1 = 0; i1 + scrollPos < drawnChatLines.size() && i1 < i; ++i1) {
                    ChatLine chatline = drawnChatLines.get(i1 + scrollPos);

                    if (chatline != null) {
                        int j1 = updateCounter - chatline.getUpdatedCounter();

                        if (j1 < 200 || flag) {
                            double d0 = (double) j1 / 200.0D;
                            d0 = 1.0D - d0;
                            d0 = d0 * 10.0D;
                            d0 = MathHelper.clamp_double(d0, 0.0D, 1.0D);
                            d0 = d0 * d0;
                            int l1 = (int) (255.0D * d0);

                            if (flag) {
                                l1 = 255;
                            }

                            l1 = (int) ((float) l1 * f);
                            ++j;

                            if (l1 > 3) {
                                int i2 = 0;
                                int j2 = -i1 * 9;
                                if (!near.moduleManager.getModule("Transparent").isToggled()) {
                                    drawRect(i2, j2 - 9, i2 + l + 4, j2, l1 / 2 << 24);
                                }
                                String s = chatline.getChatComponent().getFormattedText();
                                GlStateManager.enableBlend();
                                mc.fontRendererObj.drawStringWithShadow(s, (float) i2, (float) (j2 - 8), 16777215 + (l1 << 24));
                                GlStateManager.disableAlpha();
                                GlStateManager.disableBlend();
                            }
                        }
                    }
                }

                if (flag) {
                    int k2 = mc.fontRendererObj.FONT_HEIGHT;
                    GlStateManager.translate(-3.0F, 0.0F, 0.0F);
                    int l2 = k * k2 + k;
                    int i3 = j * k2 + j;
                    int j3 = scrollPos * i3 / k;
                    int k1 = i3 * i3 / l2;

                    if (l2 != i3) {
                        int k3 = j3 > 0 ? 170 : 96;
                        int l3 = isScrolled ? 13382451 : 3355562;
                        if (!near.moduleManager.getModule("Transparent").isToggled()) {
                            drawRect(0, -j3, 2, -j3 - k1, l3 + (k3 << 24));
                            drawRect(2, -j3, 1, -j3 - k1, 13421772 + (k3 << 24));
                        }
                    }
                }
                GlStateManager.popMatrix();
            }
        }
    }
}
