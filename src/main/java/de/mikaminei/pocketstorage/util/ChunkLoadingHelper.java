package de.mikaminei.pocketstorage.util;

import de.mikaminei.pocketstorage.PocketStorage;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import org.slf4j.Logger;

public class ChunkLoadingHelper {

    private static Logger LOGGER = PocketStorage.LOGGER;

    public static void addTicket(ServerWorld world, BlockPos pos) {
        if (world.isClient) {
            LOGGER.warn("Client tried to run ChunkLoadingHelper.addTicket; Function is Server only!", new Throwable());
            return;
        }

        ChunkPos chunkPos = new ChunkPos(pos);
        ServerChunkManager chunkManager = world.getChunkManager();

        chunkManager.addTicket(ModTicketTypes.CHUNK_LOADER_TICKET, chunkPos, 2, chunkPos);
    }

    public static void earlyRemoveTicket(ServerWorld world, BlockPos pos) {
        if (world.isClient) {
            LOGGER.warn("Client tried to run ChunkLoadingHelper.earlyRemoveTicket: Function is Server only!", new Throwable());
            return;
        }

        ChunkPos chunkPos = new ChunkPos(pos);
        ServerChunkManager chunkManager = world.getChunkManager();

        chunkManager.removeTicket(ModTicketTypes.CHUNK_LOADER_TICKET, chunkPos, 2, chunkPos);
    }
}
