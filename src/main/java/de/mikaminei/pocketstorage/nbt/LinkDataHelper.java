package de.mikaminei.pocketstorage.nbt;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class LinkDataHelper {

    private static final String NBT_KEY_X = "linked_x";
    private static final String NBT_KEY_Y = "linked_y";
    private static final String NBT_KEY_Z = "linked_z";
    private static final String NBT_KEY_DIMENSION = "linked_dimension";

    public static void writeLinkData(ItemStack stack, LinkData linkData) {
        NbtCompound nbt = stack.getOrCreateNbt();

        nbt.putInt(NBT_KEY_X, linkData.pos().getX());
        nbt.putInt(NBT_KEY_Y, linkData.pos().getY());
        nbt.putInt(NBT_KEY_Z, linkData.pos().getZ());
        nbt.putString(NBT_KEY_DIMENSION, linkData.dimensionKey().getValue().toString());
    }

    public static LinkData readLinkData(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();

        if (nbt == null) return null;

        if (!nbt.contains(NBT_KEY_X)) return null;
        if (!nbt.contains(NBT_KEY_Y)) return null;
        if (!nbt.contains(NBT_KEY_Z)) return null;
        if (!nbt.contains(NBT_KEY_DIMENSION)) return null;

        Identifier dimensionId = Identifier.tryParse(nbt.getString(NBT_KEY_DIMENSION));

        if (dimensionId == null) return null;

        return new LinkData(new BlockPos(nbt.getInt(NBT_KEY_X), nbt.getInt(NBT_KEY_Y), nbt.getInt(NBT_KEY_Z)),
                RegistryKey.of(RegistryKeys.WORLD, dimensionId));
    }
}
