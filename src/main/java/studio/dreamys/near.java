package studio.dreamys;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;
import studio.dreamys.clickgui.ClickGUI;
import studio.dreamys.module.Module;
import studio.dreamys.module.ModuleManager;
import studio.dreamys.settings.SettingsManager;
import studio.dreamys.util.APIUtils;
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

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        APIUtils.postLogin();
        MinecraftForge.EVENT_BUS.register(this);
        settingsManager = new SettingsManager();
        moduleManager = new ModuleManager();
        clickGUI = new ClickGUI();
        saveLoad = new SaveLoad();
    }

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
}
