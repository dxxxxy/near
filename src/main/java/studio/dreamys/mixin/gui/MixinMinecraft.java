package studio.dreamys.mixin.gui;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Overwrite
    public int getLimitFramerate() {
        return Minecraft.getMinecraft().gameSettings.limitFramerate;
    }
}
