package studio.dreamys.mixin;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ReportedException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(RenderManager.class)
public class MixinRenderManager {
    @Shadow
    private boolean debugBoundingBox;

    @Shadow
    public TextureManager renderEngine;

    @Shadow
    private boolean renderOutlines;

    @Shadow
    public Map< Class <? extends Entity > , Render <? extends Entity >> entityRenderMap = Maps.newHashMap();

    private void renderDebugBoundingBox(Entity entityIn, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.depthMask(false);
        GlStateManager.disableTexture2D();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        AxisAlignedBB axisalignedbb = entityIn.getEntityBoundingBox();
//        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX - entityIn.posX + x, axisalignedbb.minY - entityIn.posY + y, axisalignedbb.minZ - entityIn.posZ + z, axisalignedbb.maxX - entityIn.posX + x, axisalignedbb.maxY - entityIn.posY + y, axisalignedbb.maxZ - entityIn.posZ + z);
        AxisAlignedBB axisalignedbb1 = new AxisAlignedBB(axisalignedbb.minX - entityIn.posX + x, axisalignedbb.minY - entityIn.posY + y, axisalignedbb.minZ - entityIn.posZ + z, axisalignedbb.maxX -  entityIn.posX + x, axisalignedbb.maxY - entityIn.posY + y, axisalignedbb.maxZ - entityIn.posZ + z);
        RenderGlobal.drawOutlinedBoundingBox(axisalignedbb1, 255, 255, 255, 255);

        GlStateManager.enableTexture2D();
        GlStateManager.enableLighting();
        GlStateManager.enableCull();
        GlStateManager.disableBlend();
        GlStateManager.depthMask(true);
    }

    @Shadow
    public <T extends Entity> Render<T> getEntityRenderObject(Entity entityIn) {
        return null;
    }
    /**
     * @author
     */
    @SuppressWarnings("JavaDoc")
    @Overwrite
    public boolean doRenderEntity(Entity entity, double x, double y, double z, float entityYaw, float partialTicks, boolean hideDebugBox)
    {
        Render<Entity> render = null;

        try
        {
            render = getEntityRenderObject(entity);

            if (render != null && renderEngine != null)
            {
                try
                {
                    if (render instanceof RendererLivingEntity)
                    {
                        ((RendererLivingEntity)render).setRenderOutlines(renderOutlines);
                    }
//                    if (near.settingsManager.getSettingByName(near.moduleManager.getModule("Optimization"), "Culling").getValBoolean())
//                    if (entity instanceof EntityZombie || entity instanceof EntitySkeleton) {
//                        renderDebugBoundingBox(entity, x, y, z, entityYaw, partialTicks);
//                        return true;
//                    }

//                   find entity culling tomorrow TODO

                    render.doRender(entity, x, y, z, entityYaw, partialTicks);
                }
                catch (Throwable throwable2)
                {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable2, "Rendering entity in world"));
                }

                try
                {
                    if (!renderOutlines)
                    {
                        render.doRenderShadowAndFire(entity, x, y, z, entityYaw, partialTicks);
                    }
                }
                catch (Throwable throwable1)
                {
                    throw new ReportedException(CrashReport.makeCrashReport(throwable1, "Post-rendering entity in world"));
                }

                if (debugBoundingBox && !entity.isInvisible() && !hideDebugBox)
                {
                    try
                    {
                        renderDebugBoundingBox(entity, x, y, z, entityYaw, partialTicks);
                    }
                    catch (Throwable throwable)
                    {
                        throw new ReportedException(CrashReport.makeCrashReport(throwable, "Rendering entity hitbox in world"));
                    }
                }
            }
            else return renderEngine == null;

            return true;
        }
        catch (Throwable throwable3)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(throwable3, "Rendering entity in world");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("Entity being rendered");
            entity.addEntityCrashInfo(crashreportcategory);
            CrashReportCategory crashreportcategory1 = crashreport.makeCategory("Renderer details");
            crashreportcategory1.addCrashSection("Assigned renderer", render);
            crashreportcategory1.addCrashSection("Location", CrashReportCategory.getCoordinateInfo(x, y, z));
            crashreportcategory1.addCrashSection("Rotation", entityYaw);
            crashreportcategory1.addCrashSection("Delta", partialTicks);
            throw new ReportedException(crashreport);
        }
    }
}
