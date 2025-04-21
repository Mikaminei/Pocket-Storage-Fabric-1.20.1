package de.mikaminei.pocketstorage.util;

import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public record LinkData(BlockPos pos, RegistryKey<World> dimensionKey) {}
