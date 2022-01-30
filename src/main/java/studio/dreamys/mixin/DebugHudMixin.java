package studio.dreamys.mixin;

import net.minecraft.client.gui.GuiOverlayDebug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import studio.dreamys.near;

import java.util.List;

@Mixin(GuiOverlayDebug.class)
public class DebugHudMixin {

    public DebugHudMixin() {
        near.cullTask.requestCull = true;
    }
    
    @Inject(method = "call", at = @At("RETURN"))
    public void getLeftText(CallbackInfoReturnable<List<String>> callback) {
        List<String> list = callback.getReturnValue();
        list.add("[Culling] Last pass: " + near.cullTask.lastTime + "ms");
        list.add("[Culling] Rendered Block Entities: " + near.renderedBlockEntities + " Skipped: " + near.skippedBlockEntities);
        list.add("[Culling] Rendered Entities: " + near.renderedEntities + " Skipped: " + near.skippedEntities);
        //list.add("[Culling] Ticked Entities: " + lastTickedEntities + " Skipped: " + lastSkippedEntityTicks);
        
        near.renderedBlockEntities = 0;
        near.skippedBlockEntities = 0;
        near.renderedEntities = 0;
        near.skippedEntities = 0;
    }
    
}
