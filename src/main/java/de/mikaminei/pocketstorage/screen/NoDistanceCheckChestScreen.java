package de.mikaminei.pocketstorage.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class NoDistanceCheckChestScreen extends GenericContainerScreen {

    public NoDistanceCheckChestScreen(GenericContainerScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }
}
