package de.mikaminei.pocketstorage.util;

import de.mikaminei.pocketstorage.PocketStorage;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.util.math.ChunkPos;

import java.util.Comparator;

public class ModTicketTypes {

    public static final ChunkTicketType<ChunkPos> CHUNK_LOADER_TICKET = ChunkTicketType.create(
            PocketStorage.MOD_ID + ":chunk_loader", Comparator.comparingLong(ChunkPos::toLong), 600);

    // avoid instancing this class
    private ModTicketTypes() {}
}
