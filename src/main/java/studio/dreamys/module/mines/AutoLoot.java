package studio.dreamys.module.mines;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import studio.dreamys.module.Category;
import studio.dreamys.module.Module;
import studio.dreamys.util.PlayerUtils;

public class AutoLoot extends Module {
    public AutoLoot() {
        super("AutoLoot", Category.MINES);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiScreenEvent.InitGuiEvent e) {
        if (e.gui instanceof GuiChest) {
            GuiChest gui = (GuiChest) e.gui;
            Container containerChest = gui.inventorySlots;
            if (containerChest instanceof ContainerChest) {
                String displayName = ((ContainerChest) containerChest).getLowerChestInventory().getDisplayName().getUnformattedText().trim();
                if (displayName.contains("Loot Chest")) {
                    PlayerUtils.player.closeScreen();
                }
            }
        }
    }
}
