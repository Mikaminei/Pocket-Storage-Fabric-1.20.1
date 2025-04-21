package de.mikaminei.pocketstorage.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LinkDataHelper {

    private static final String NBT_KEY_POS_X = "linked_x";
    private static final String NBT_KEY_POS_Y = "linked_y";
    private static final String NBT_KEY_POS_Z = "linked_z";
    private static final String NBT_KEY_DIMENSION = "linked_dimension";

    public static void writeLinkData(ItemStack stack, BlockPos pos, World world) {
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putInt(NBT_KEY_POS_X, pos.getX());
        nbt.putInt(NBT_KEY_POS_Y, pos.getY());
        nbt.putInt(NBT_KEY_POS_Z, pos.getZ());
        nbt.putString(NBT_KEY_DIMENSION, world.getRegistryKey().getValue().toString());
    }

    public static LinkData readLinkData(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();

        if (nbt == null) return null;

        if (!nbt.contains(NBT_KEY_POS_X)) return null;
        if (!nbt.contains(NBT_KEY_POS_Y)) return null;
        if (!nbt.contains(NBT_KEY_POS_Z)) return null;
        if (!nbt.contains(NBT_KEY_DIMENSION)) return null;

        int x = nbt.getInt(NBT_KEY_POS_X);
        int y = nbt.getInt(NBT_KEY_POS_Y);
        int z = nbt.getInt(NBT_KEY_POS_Z);
        String dimensionIdString = nbt.getString(NBT_KEY_DIMENSION);

        Identifier dimensionId = Identifier.tryParse(dimensionIdString);

        if (dimensionId == null) return null;

        RegistryKey<World> dimensionKey = RegistryKey.of(RegistryKeys.WORLD, dimensionId);

        return new LinkData(new BlockPos(x, y, z), dimensionKey);
    }
}
