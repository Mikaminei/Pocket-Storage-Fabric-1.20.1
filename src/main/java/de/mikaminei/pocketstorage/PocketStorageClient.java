package de.mikaminei.pocketstorage;

import de.mikaminei.pocketstorage.screen.ModScreenHandlers;
import de.mikaminei.pocketstorage.screen.RemoteChestScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.slf4j.Logger;

@Environment(EnvType.CLIENT)
public class PocketStorageClient implements ClientModInitializer {

    private static final Logger LOGGER = PocketStorage.LOGGER;

    @Override
    public void onInitializeClient() {
        PocketStorage.LOGGER.debug("Initializing Pocket Storage Client");

        LOGGER.debug("Registering Mod Screen Associations for " + PocketStorage.MOD_ID);

        HandledScreens.register(ModScreenHandlers.REMOTE_CHEST_SCREEN_HANDLER_TYPE, RemoteChestScreen::new);
    }
}