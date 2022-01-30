package studio.dreamys.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import studio.dreamys.entityculling.access.Cullable;
import studio.dreamys.near;

@Mixin({Entity.class, TileEntity.class})
public class CullableMixin implements Cullable {

	private long lasttime;
	private boolean culled;
	private boolean outOfCamera;
	
	@Override
	public void setTimeout() {
		lasttime = System.currentTimeMillis() + 1000;
	}

	@Override
	public boolean isForcedVisible() {
		return lasttime <= System.currentTimeMillis();
	}

	@Override
	public void setCulled(boolean value) {
		culled = value;
		if(!value) {
			setTimeout();
		}
	}

	@Override
	public boolean isCulled() {
		if(!near.moduleManager.getModule("Optimization").isToggled() || !near.settingsManager.getSettingByName(near.moduleManager.getModule("Optimization"), "Culling").getValBoolean())return false;
		return culled;
	}

    @Override
    public void setOutOfCamera(boolean value) {
        outOfCamera = value;
    }

    @Override
    public boolean isOutOfCamera() {
		if(!near.moduleManager.getModule("Optimization").isToggled() || !near.settingsManager.getSettingByName(near.moduleManager.getModule("Optimization"), "Culling").getValBoolean())return false;
		return outOfCamera;
    }

}
