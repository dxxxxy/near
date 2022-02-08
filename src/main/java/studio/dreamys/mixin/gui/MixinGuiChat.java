package studio.dreamys.mixin.gui;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import studio.dreamys.font.Fonts;
import studio.dreamys.near;

@Mixin(GuiChat.class)
public class MixinGuiChat extends GuiScreen {

    @Shadow
    private int sentHistoryCursor = -1;

    @Shadow
    private String defaultInputFieldText;

    @Shadow
    protected GuiTextField inputField;

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiChat;drawRect(IIIII)V"))
    public void transparent(int a, int b, int c, int d, int e) {
        if (!near.moduleManager.getModule("Transparent").isToggled()) drawRect(2, height - 14, width - 2, height - 2, Integer.MIN_VALUE);
    }

    @Overwrite
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        sentHistoryCursor = mc.ingameGUI.getChatGUI().getSentMessages().size();
        inputField = new GuiTextField(0, Fonts.font35, 4, height - 12, width - 4, 12);
        inputField.setMaxStringLength(100);
        inputField.setEnableBackgroundDrawing(false);
        inputField.setFocused(true);
        inputField.setText(defaultInputFieldText);
        inputField.setCanLoseFocus(false);
    }
}
