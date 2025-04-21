package de.mikaminei.pocketstorage;

import de.mikaminei.pocketstorage.item.ModItems;
import de.mikaminei.pocketstorage.screen.ModScreenHandlers;
import de.mikaminei.pocketstorage.util.ChunkLoadingHelper;
import de.mikaminei.pocketstorage.util.RemoteAccessData;
import de.mikaminei.pocketstorage.util.RemoteAccessManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class PocketStorage implements ModInitializer {

	public static final String MOD_ID = "pocket-storage";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Pocket Storage Mod");

		ModItems.registerModItems();
		ModScreenHandlers.registerScreenHandlers();
		RemoteAccessManager.register();

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			ServerPlayerEntity player = handler.getPlayer();
			UUID playerUuid = player.getUuid();
			RemoteAccessData accessData = RemoteAccessManager.removeAccess(playerUuid);

			if (accessData != null) {
				ChunkLoadingHelper.earlyRemoveTicket(player.getServerWorld(), accessData.pos());
			}
		});
	}
}