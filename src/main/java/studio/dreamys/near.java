package studio.dreamys;

import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;
import studio.dreamys.clickgui.ClickGUI;
import studio.dreamys.entityculling.Config;
import studio.dreamys.entityculling.CullTask;
import studio.dreamys.entityculling.Provider;
import studio.dreamys.module.Module;
import studio.dreamys.module.ModuleManager;
import studio.dreamys.settings.SettingsManager;
import studio.dreamys.util.SaveLoad;

import java.io.IOException;

@Mod(modid = near.MODID, name = near.NAME, version = near.VERSION)
public class near {
    public static final String MODID = "near";
    public static final String NAME = "near";
    public static final String VERSION = "1.0";
    public static ModuleManager moduleManager;
    public static SettingsManager settingsManager;
    public static ClickGUI clickGUI;
    public static SaveLoad saveLoad;
    @SuppressWarnings("unused")
    public static String token;

    public OcclusionCullingInstance culling;
    public static CullTask cullTask;

    public static int renderedBlockEntities;
    public static int skippedBlockEntities;
    public static int renderedEntities;
    public static int skippedEntities;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
//        APIUtils.postLogin();
        MinecraftForge.EVENT_BUS.register(this);
        settingsManager = new SettingsManager();
        moduleManager = new ModuleManager();
        clickGUI = new ClickGUI();
        saveLoad = new SaveLoad();

        culling = new OcclusionCullingInstance(Config.tracingDistance, new Provider());
        cullTask = new CullTask(culling, Config.blockEntityWhitelist);

        Thread cullThread = new Thread(cullTask, "CullThread");
        cullThread.setUncaughtExceptionHandler((thread, ex) -> {
            System.out.println("The CullingThread has crashed! Please report the following stacktrace!");
            ex.printStackTrace();
        });
        cullThread.start();
    }

    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent e) throws IOException {
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) return;
        if (Keyboard.getEventKeyState()) {
            int keyCode = Keyboard.getEventKey();
            if (keyCode <= 0) return;
            for (Module m : moduleManager.modules) {
                if (m.getKey() == keyCode) m.toggle();
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        cullTask.requestCull = true;
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        cullTask.requestCull = true;
    }
}
