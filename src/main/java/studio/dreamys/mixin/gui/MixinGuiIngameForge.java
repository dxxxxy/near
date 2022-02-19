package studio.dreamys.mixin.gui;

import net.minecraft.client.gui.FontRenderer;
import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.lib.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import studio.dreamys.font.Fonts;

@Mixin(GuiIngameForge.class)
public abstract class MixinGuiIngameForge {

    @Shadow
    private FontRenderer fontrenderer;

    @Redirect(method = "renderGameOverlay", at = @At(value = "FIELD", target = "Lnet/minecraftforge/client/GuiIngameForge;fontrenderer:Lnet/minecraft/client/gui/FontRenderer;", opcode = Opcodes.PUTFIELD))
    public void renderGameOverlay(GuiIngameForge instance, FontRenderer value) {
        fontrenderer = Fonts.font35MontserratMedium;
    }

    @Overwrite(remap = false)
    protected void renderArmor(int width, int height) {

    }

    @Overwrite(remap = false)
    protected void renderAir(int width, int height) {

    }

    @Overwrite(remap = false)
    public void renderFood(int width, int height) {

    }

    @Overwrite(remap = false)
    protected void renderJumpBar(int width, int height) {

    }

    @Overwrite(remap = false)
    protected void renderHealthMount(int width, int height) {

    }
}
