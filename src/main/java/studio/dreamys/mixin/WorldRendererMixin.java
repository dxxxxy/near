package studio.dreamys.mixin;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import studio.dreamys.entityculling.Config;
import studio.dreamys.entityculling.access.Cullable;
import studio.dreamys.entityculling.access.EntityRendererInter;
import studio.dreamys.near;

@Mixin(RenderManager.class)
public abstract class WorldRendererMixin {

    @Shadow
    public abstract <T extends Entity> Render<T> getEntityRenderObject(Entity p_getEntityRenderObject_1_);

    @Inject(at = @At("HEAD"), method = "doRenderEntity", cancellable = true)
    public void doRenderEntity(Entity entity, double p_doRenderEntity_2_, double d1, double d2,
            float tickDelta, float p_doRenderEntity_9_, boolean p_doRenderEntity_10_, CallbackInfoReturnable<Boolean> info) {
        Cullable cullable = (Cullable) entity;
        if (cullable.isForcedVisible() && cullable.isCulled()) {
            //noinspection unchecked
            EntityRendererInter<Entity> entityRenderer = (EntityRendererInter<Entity>) getEntityRenderObject(entity);
            if (Config.renderNametagsThroughWalls && entityRenderer.shadowShouldShowName(entity)) {
                entityRenderer.shadowRenderNameTag(entity, p_doRenderEntity_2_, d1, d2);
                //entityRenderer.doRender(entity, entity.posX, entity.posY, entity.posZ, tickDelta, tickDelta);
            }
            near.skippedEntities++;
            info.cancel();
            return;
        }
        near.renderedEntities++;
    }
}
