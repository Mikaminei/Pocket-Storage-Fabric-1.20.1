package de.mikaminei.pocketstorage.world;

import de.mikaminei.pocketstorage.ticket.ModTicketTypes;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;

public class ChunkLoadingHelper {

    public static void requestChunkLoad(ServerWorld world, BlockPos blockPos) {
        requestChunkLoad(world, new ChunkPos(blockPos));
    }

    public static void requestChunkLoad(ServerWorld world, ChunkPos chunkPos) {
        if (world.isClient) return;

        ServerChunkManager chunkManager = world.getChunkManager();
        chunkManager.addTicket(ModTicketTypes.CHUNK_LOADER_TICKET, chunkPos, 2, chunkPos);
    }

    public static void unrequestChunkLoad(ServerWorld world, BlockPos blockPos) {
        unrequestChunkLoad(world, new ChunkPos(blockPos));
    }

    public static void unrequestChunkLoad(ServerWorld world, ChunkPos chunkPos) {
        if (world.isClient) return;

        ServerChunkManager chunkManager = world.getChunkManager();
        chunkManager.removeTicket(ModTicketTypes.CHUNK_LOADER_TICKET, chunkPos, 2, chunkPos);
    }
}
