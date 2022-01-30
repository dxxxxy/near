package studio.dreamys.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import org.spongepowered.asm.mixin.Mixin;
import studio.dreamys.entityculling.access.Cullable;
import studio.dreamys.near;

@Mixin(value = { Entity.class, TileEntity.class })
public class CullableMixin implements Cullable {

	private long lasttime = 0;
	private boolean culled = false;
	private boolean outOfCamera = false;
	
	@Override
	public void setTimeout() {
		lasttime = System.currentTimeMillis() + 1000;
	}

	@Override
	public boolean isForcedVisible() {
		return lasttime > System.currentTimeMillis();
	}

	@Override
	public void setCulled(boolean value) {
		this.culled = value;
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
        this.outOfCamera = value;
    }

    @Override
    public boolean isOutOfCamera() {
        if(!near.moduleManager.getModule("Optimization").isToggled() || !near.settingsManager.getSettingByName(near.moduleManager.getModule("Optimization"), "Culling").getValBoolean())return false;
        return outOfCamera;
    }

}
