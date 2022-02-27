package studio.dreamys.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.Sys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import studio.dreamys.util.RenderUtils;

@Mixin(Minecraft.class)
public class MixinMinecraft {

    @Shadow
    private int rightClickDelayTimer;

    @Shadow
    public FontRenderer fontRendererObj;

    private long lastFrame = getTime();
    Minecraft mc = Minecraft.getMinecraft();

    @Inject(method = "runGameLoop", at = @At("HEAD"))
    private void runGameLoop(CallbackInfo callbackInfo) {
        long currentTime = getTime();
        int deltaTime = (int) (currentTime - lastFrame);
        lastFrame = currentTime;

        RenderUtils.deltaTime = deltaTime;
    }
//
//    @Shadow @Final private static Logger logger;
//    @Shadow public PlayerControllerMP playerController;
//    @Shadow public WorldClient theWorld;
//    @Shadow public EntityPlayerSP thePlayer;
//    @Shadow public MovingObjectPosition objectMouseOver;
//    @Shadow public EntityRenderer entityRenderer;
//
//    @Overwrite
//    private void rightClickMouse()
//    {
//        if (!playerController.getIsHittingBlock())
//        {
//            rightClickDelayTimer = 4;
//            boolean flag = true;
//            ItemStack itemstack = thePlayer.inventory.getCurrentItem();
//
//            if (objectMouseOver == null)
//            {
//                logger.warn("Null returned as 'hitResult', this shouldn't happen!");
//            }
//            else
//            {
//                switch (objectMouseOver.typeOfHit)
//                {
//                    case ENTITY:
//                        if (objectMouseOver.entityHit instanceof EntityArmorStand) break;
//                        if (playerController.isPlayerRightClickingOnEntity(thePlayer, objectMouseOver.entityHit, objectMouseOver))
//                        {
//                            flag = false;
//                        }
//                        else if (playerController.interactWithEntitySendPacket(thePlayer, objectMouseOver.entityHit))
//                        {
//                            flag = false;
//                        }
//
//                        break;
//                    case BLOCK:
//                        BlockPos blockpos = objectMouseOver.getBlockPos();
//
//                        if (!theWorld.isAirBlock(blockpos))
//                        {
//                            int i = itemstack != null ? itemstack.stackSize : 0;
//
//
//                            boolean result = !net.minecraftforge.event.ForgeEventFactory.onPlayerInteract(thePlayer, net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, theWorld, blockpos, objectMouseOver.sideHit, objectMouseOver.hitVec).isCanceled();
//                            if (result) { //Forge: Kept separate to simplify patch
//                                if (playerController.onPlayerRightClick(thePlayer, theWorld, itemstack, blockpos, objectMouseOver.sideHit, objectMouseOver.hitVec))
//                                {
//                                    flag = false;
//                                    thePlayer.swingItem();
//                                }
//                            }
//
//                            if (itemstack == null)
//                            {
//                                return;
//                            }
//
//                            if (itemstack.stackSize == 0)
//                            {
//                                thePlayer.inventory.mainInventory[thePlayer.inventory.currentItem] = null;
//                            }
//                            else if (itemstack.stackSize != i || playerController.isInCreativeMode())
//                            {
//                                this.entityRenderer.itemRenderer.resetEquippedProgress();
//                            }
//                        }
//                }
//            }
//
//            if (flag)
//            {
//                ItemStack itemstack1 = thePlayer.inventory.getCurrentItem();
//
//                boolean result = !net.minecraftforge.event.ForgeEventFactory.onPlayerInteract(thePlayer, net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_AIR, theWorld, null, null, null).isCanceled();
//                if (result && itemstack1 != null && playerController.sendUseItem(thePlayer, theWorld, itemstack1))
//                {
//                    this.entityRenderer.itemRenderer.resetEquippedProgress2();
//                }
//            }
//        }
//    }

    @Overwrite
    public int getLimitFramerate() {
        return Minecraft.getMinecraft().gameSettings.limitFramerate;
    }

    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
}
