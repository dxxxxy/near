package studio.dreamys;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
import studio.dreamys.entityculling.ConfigUpgrader;
import studio.dreamys.entityculling.CullTask;
import studio.dreamys.entityculling.Provider;
import studio.dreamys.module.Module;
import studio.dreamys.module.ModuleManager;
import studio.dreamys.settings.SettingsManager;
import studio.dreamys.util.APIUtils;
import studio.dreamys.util.SaveLoad;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        APIUtils.postLogin();
        MinecraftForge.EVENT_BUS.register(this);
        settingsManager = new SettingsManager();
        moduleManager = new ModuleManager();
        clickGUI = new ClickGUI();
        saveLoad = new SaveLoad();
        if (settingsFile.exists()) {
            try {
                config = gson.fromJson(new String(Files.readAllBytes(settingsFile.toPath()), StandardCharsets.UTF_8),
                        Config.class);
            } catch (Exception ex) {
                System.out.println("Error while loading config! Creating a new one!");
                ex.printStackTrace();
            }
        }
        if (config == null) {
            config = new Config();
            writeConfig();
        } else {
            if(ConfigUpgrader.upgradeConfig(config)) {
                writeConfig(); // Config got modified
            }
        }
        culling = new OcclusionCullingInstance(config.tracingDistance, new Provider());
        cullTask = new CullTask(culling, config.blockEntityWhitelist);

        Thread cullThread = new Thread(cullTask, "CullThread");
        cullThread.setUncaughtExceptionHandler((thread, ex) -> {
            System.out.println("The CullingThread has crashed! Please report the following stacktrace!");
            ex.printStackTrace();
        });
        cullThread.start();
    }

    //if(!near.moduleManager.getModule("Optimization").isToggled() || !near.settingsManager.getSettingByName(near.moduleManager.getModule("Optimization"), "Culling").getValBoolean())return false;
    //

    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent e) {
        if (Minecraft.getMinecraft().theWorld == null || Minecraft.getMinecraft().thePlayer == null) return;
        try {
            if (Keyboard.isCreated())
                if (Keyboard.getEventKeyState()) {
                    int keyCode = Keyboard.getEventKey();
                    if (keyCode <= 0)
                        return;
                    for (Module m : moduleManager.modules)
                        if (m.getKey() == keyCode) m.toggle();
                }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public OcclusionCullingInstance culling;
    public boolean debugHitboxes;
    public static boolean enabled = true; // public static to make it faster for the jvm
    public static CullTask cullTask;

    public static Config config;
    private final File settingsFile = new File("config", "entityculling.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //stats
    public static int renderedBlockEntities;
    public static int skippedBlockEntities;
    public static int renderedEntities;
    public static int skippedEntities;

    public void writeConfig() {
        if (settingsFile.exists())
            settingsFile.delete();
        try {
            Files.write(settingsFile.toPath(), gson.toJson(config).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    @SubscribeEvent
    public void doClientTick(TickEvent.ClientTickEvent event) {
        cullTask.requestCull = true;
    }

    @SubscribeEvent
    public void doWorldTick(TickEvent.WorldTickEvent event) {
        cullTask.requestCull = true;
    }
}
