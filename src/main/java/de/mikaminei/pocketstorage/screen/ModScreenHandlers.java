package de.mikaminei.pocketstorage.screen;

import de.mikaminei.pocketstorage.PocketStorage;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;

public class ModScreenHandlers {

    private static final Logger LOGGER = PocketStorage.LOGGER;

    public static ScreenHandlerType<NoDistanceCheckScreenHandler> NO_DISTANCE_CHECK_CHEST_SCREEN_HANDLER_TYPE;

    public static void registerScreenHandlers() {
        LOGGER.debug("Registering Mod Screen Handlers for " + PocketStorage.MOD_ID);

        NO_DISTANCE_CHECK_CHEST_SCREEN_HANDLER_TYPE = Registry.register(
                Registries.SCREEN_HANDLER,
                new Identifier(PocketStorage.MOD_ID, "no_distance_check_chest"),
                new ExtendedScreenHandlerType<>(NoDistanceCheckScreenHandler::createClientSide)
        );
    }
}
