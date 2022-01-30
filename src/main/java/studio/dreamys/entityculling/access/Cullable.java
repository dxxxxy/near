package studio.dreamys.entityculling.access;

public interface Cullable {

	void setTimeout();
	boolean isForcedVisible();
	
	void setCulled(boolean value);
	boolean isCulled();
	
	void setOutOfCamera(boolean value);
	boolean isOutOfCamera();
	
}
