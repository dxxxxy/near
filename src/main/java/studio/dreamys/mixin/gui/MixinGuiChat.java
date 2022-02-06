package studio.dreamys.mixin.gui;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import studio.dreamys.near;

@Mixin(GuiChat.class)
public class MixinGuiChat extends GuiScreen {

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiChat;drawRect(IIIII)V"))
    public void transparent(int a, int b, int c, int d, int e) {
        if (!near.moduleManager.getModule("Transparent").isToggled()) drawRect(2, height - 14, width - 2, height - 2, Integer.MIN_VALUE);
    }
}
