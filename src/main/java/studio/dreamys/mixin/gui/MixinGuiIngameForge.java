package studio.dreamys.mixin.gui;

import net.minecraftforge.client.GuiIngameForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = GuiIngameForge.class, remap = false)
public abstract class MixinGuiIngameForge {

//    @Shadow
//    private FontRenderer fontrenderer;

//    @Redirect(method = "renderGameOverlay", at = @At(value = "FIELD", target = "Lnet/minecraftforge/client/GuiIngameForge;fontrenderer:Lnet/minecraft/client/gui/FontRenderer;", opcode = Opcodes.PUTFIELD, remap = true))
//    public void renderGameOverlay(GuiIngameForge instance, FontRenderer value) {
//        fontrenderer = Fonts.font35MontserratMedium;
//    }

    @Overwrite
    protected void renderArmor(int width, int height) {

    }

    @Overwrite
    protected void renderAir(int width, int height) {

    }

    @Overwrite
    public void renderFood(int width, int height) {

    }

    @Overwrite
    protected void renderJumpBar(int width, int height) {

    }

    @Overwrite
    protected void renderHealthMount(int width, int height) {

    }
}
