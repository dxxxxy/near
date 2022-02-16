package studio.dreamys.mixin.gui;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.boss.BossStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import studio.dreamys.font.Fonts;

import java.awt.*;
import java.util.Objects;
import java.util.Random;

@Mixin(GuiIngame.class)
public class MixinGuiIngame extends Gui {

    @Final
    @Shadow
    protected Minecraft mc;

    @Shadow
    protected int updateCounter;

    @Shadow
    protected int playerHealth;

    @Shadow
    protected int lastPlayerHealth;

    @Shadow
    protected long lastSystemTime;

    @Shadow
    protected long healthUpdateCounter;

    @Final
    @Shadow
    protected final Random rand = new Random();

    @Overwrite //custom font
    public FontRenderer getFontRenderer() {
        return Fonts.font35MontserratMedium;
    }

    @Redirect(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiIngame;drawRect(IIIII)V"))
    public void transparentBackground(int a, int b, int c, int d, int e) {

    }

    @ModifyArg(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I"), index = 3)
    public int fixColor(int x) {
        return Color.WHITE.getRGB();
    }

    @Redirect(method = "renderScoreboard", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/FontRenderer;drawString(Ljava/lang/String;III)I", ordinal = 1))
    public int removeRedNumbers(FontRenderer instance, String text, int x, int y, int color) {
        return x;
    }

    @Inject(method = "renderBossHealth", at = @At("HEAD"), cancellable = true)
    public void removeWitherborn(CallbackInfo ci) {
        if (Objects.equals(BossStatus.bossName, "Wither")) {
            ci.cancel();
        }
    }

    @Overwrite //do not render horse jump bar
    public void renderHorseJumpBar(ScaledResolution scaledRes, int x) {

    }

    @Overwrite //anti pumpkin overlay
    protected void renderPumpkinOverlay(ScaledResolution scaledRes) {

    }

    @Overwrite //only keep health and air
    protected void renderPlayerStats(ScaledResolution scaledRes)
    {
        if (mc.getRenderViewEntity() instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer) mc.getRenderViewEntity();
            int i = MathHelper.ceiling_float_int(entityplayer.getHealth());
            boolean flag = healthUpdateCounter > (long) updateCounter && (healthUpdateCounter - (long) updateCounter) / 3L % 2L == 1L;

            if (i < playerHealth && entityplayer.hurtResistantTime > 0)
            {
                lastSystemTime = Minecraft.getSystemTime();
                healthUpdateCounter = updateCounter + 20;
            }
            else if (i > playerHealth && entityplayer.hurtResistantTime > 0)
            {
                lastSystemTime = Minecraft.getSystemTime();
                healthUpdateCounter = updateCounter + 10;
            }

            if (Minecraft.getSystemTime() - lastSystemTime > 1000L)
            {
                playerHealth = i;
                lastPlayerHealth = i;
                lastSystemTime = Minecraft.getSystemTime();
            }

            playerHealth = i;
            int j = lastPlayerHealth;
            rand.setSeed(updateCounter * 312871L);
            IAttributeInstance iattributeinstance = entityplayer.getEntityAttribute(SharedMonsterAttributes.maxHealth);
            int i1 = scaledRes.getScaledWidth() / 2 - 91;
            int j1 = scaledRes.getScaledWidth() / 2 + 91;
            int k1 = scaledRes.getScaledHeight() - 39;
            float f = (float)iattributeinstance.getAttributeValue();
            float f1 = entityplayer.getAbsorptionAmount();
            int l1 = MathHelper.ceiling_float_int((f + f1) / 2.0F / 10.0F);
            int i2 = Math.max(10 - (l1 - 2), 3);
            int j2 = k1 - (l1 - 1) * i2 - 10;
            float f2 = f1;
            int l2 = -1;

            if (entityplayer.isPotionActive(Potion.regeneration))
            {
                l2 = updateCounter % MathHelper.ceiling_float_int(f + 5.0F);
            }

            //health

            for (int i6 = MathHelper.ceiling_float_int((f + f1) / 2.0F) - 1; i6 >= 0; --i6)
            {
                int j6 = 16;

                if (entityplayer.isPotionActive(Potion.poison))
                {
                    j6 += 36;
                }
                else if (entityplayer.isPotionActive(Potion.wither))
                {
                    j6 += 72;
                }

                int k3 = 0;

                if (flag)
                {
                    k3 = 1;
                }

                int l3 = MathHelper.ceiling_float_int((float)(i6 + 1) / 10.0F) - 1;
                int i4 = i1 + i6 % 10 * 8;
                int j4 = k1 - l3 * i2;

                if (i <= 4)
                {
                    j4 += rand.nextInt(2);
                }

                if (i6 == l2)
                {
                    j4 -= 2;
                }

                int k4 = 0;

                if (entityplayer.worldObj.getWorldInfo().isHardcoreModeEnabled())
                {
                    k4 = 5;
                }

                drawTexturedModalRect(i4, j4, 16 + k3 * 9, 9 * k4, 9, 9);

                if (flag)
                {
                    if (i6 * 2 + 1 < j)
                    {
                        drawTexturedModalRect(i4, j4, j6 + 54, 9 * k4, 9, 9);
                    }

                    if (i6 * 2 + 1 == j)
                    {
                        drawTexturedModalRect(i4, j4, j6 + 63, 9 * k4, 9, 9);
                    }
                }

                if (f2 > 0.0F)
                {
                    if (f2 == f1 && f1 % 2.0F == 1.0F)
                    {
                        drawTexturedModalRect(i4, j4, j6 + 153, 9 * k4, 9, 9);
                    }
                    else
                    {
                        drawTexturedModalRect(i4, j4, j6 + 144, 9 * k4, 9, 9);
                    }

                    f2 -= 2.0F;
                }
                else
                {
                    if (i6 * 2 + 1 < i)
                    {
                        drawTexturedModalRect(i4, j4, j6 + 36, 9 * k4, 9, 9);
                    }

                    if (i6 * 2 + 1 == i)
                    {
                        drawTexturedModalRect(i4, j4, j6 + 45, 9 * k4, 9, 9);
                    }
                }
            }


            //air

            if (entityplayer.isInsideOfMaterial(Material.water))
            {
                int l6 = mc.thePlayer.getAir();
                int k7 = MathHelper.ceiling_double_int((double)(l6 - 2) * 10.0D / 300.0D);
                int i8 = MathHelper.ceiling_double_int((double)l6 * 10.0D / 300.0D) - k7;

                for (int l8 = 0; l8 < k7 + i8; ++l8)
                {
                    if (l8 < k7)
                    {
                        drawTexturedModalRect(j1 - l8 * 8 - 9, j2, 16, 18, 9, 9);
                    }
                    else
                    {
                        drawTexturedModalRect(j1 - l8 * 8 - 9, j2, 25, 18, 9, 9);
                    }
                }
            }
        }
    }
}
