package de.mikaminei.pocketstorage.util;

import de.mikaminei.pocketstorage.PocketStorage;
import de.mikaminei.pocketstorage.screen.RemoteChestScreenHandler;
import de.mikaminei.pocketstorage.world.ChunkLoadingHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RemoteAccessManager implements ServerTickEvents.EndTick {

    private static final int CHECK_INTERVAL = 10 * 20;

    private static final Map<UUID, RemoteAccessData> remoteAccessMap = new ConcurrentHashMap<>();

    public static void addAccess(UUID playerUuid, RemoteAccessData data) {
        remoteAccessMap.put(playerUuid, data);
    }

    @Nullable
    public static RemoteAccessData removeAccess(UUID playerUuid) {
        return remoteAccessMap.remove(playerUuid);
    }

    @Nullable
    public static RemoteAccessData getAccessData(UUID playerUuid) {
        return remoteAccessMap.get(playerUuid);
    }

    public static void clearAllAccess() {
        remoteAccessMap.clear();
    }

    @Override
    public void onEndTick(MinecraftServer server) {
        if (server.getTicks() % CHECK_INTERVAL != 0) return;

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            UUID playerUuid = player.getUuid();
            RemoteAccessData accessData = getAccessData(playerUuid);

            if (accessData == null) return;

            if (player.currentScreenHandler instanceof RemoteChestScreenHandler) {
                ChunkLoadingHelper.requestChunkLoad(server.getWorld(accessData.dimensionKey()), accessData.pos());
            } else {
                removeAccess(playerUuid);
            }
        }
    }

    public static void register() {
        PocketStorage.LOGGER.info("Registering RemoteAccessTicker");

        ServerTickEvents.END_SERVER_TICK.register(new RemoteAccessManager());
    }
}
