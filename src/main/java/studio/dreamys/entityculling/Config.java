package studio.dreamys.entityculling;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Config {
    public int configVersion = 4;
    public final boolean renderNametagsThroughWalls = true;
    public final Set<String> blockEntityWhitelist = new HashSet<>(Collections.singletonList("tile.beacon"));
    public final int tracingDistance = 128;
    public boolean debugMode;
    public final int sleepDelay = 10;
    public final int hitboxLimit = 50;
    public final boolean skipMarkerArmorStands = true;
}
