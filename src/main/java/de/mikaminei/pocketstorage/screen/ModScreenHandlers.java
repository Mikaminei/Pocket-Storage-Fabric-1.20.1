package de.mikaminei.pocketstorage.screen;

import de.mikaminei.pocketstorage.PocketStorage;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;

public class ModScreenHandlers {

    public static ScreenHandlerType<RemoteChestScreenHandler> REMOTE_CHEST_SCREEN_HANDLER_TYPE;
    public static ScreenHandlerType<RemoteCraftingScreenHandler> REMOTE_CRAFTING_SCREEN_HANDLER_TYPE;

    public static void registerScreenHandlers() {
        PocketStorage.LOGGER.debug("Registering Mod Screen Handlers for " + PocketStorage.MOD_ID);

        REMOTE_CHEST_SCREEN_HANDLER_TYPE = Registry.register(
                Registries.SCREEN_HANDLER, new Identifier(PocketStorage.MOD_ID, "remote_chest"),
                new ExtendedScreenHandlerType<>(RemoteChestScreenHandler::createClientSide));

        REMOTE_CRAFTING_SCREEN_HANDLER_TYPE = Registry.register(
                Registries.SCREEN_HANDLER, new Identifier(PocketStorage.MOD_ID, "remote_crafting"),
                new ScreenHandlerType<>(RemoteCraftingScreenHandler::new, FeatureFlags.VANILLA_FEATURES));
    }
}
