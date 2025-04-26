package de.mikaminei.pocketstorage.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

public class RemoteChestScreenHandler extends GenericContainerScreenHandler {

    public RemoteChestScreenHandler(ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, Inventory inventory, int rows) {
        super(type, syncId, playerInventory, inventory, rows);
    }

    public static RemoteChestScreenHandler createClientSide(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        int rows = buf.readUnsignedByte();

        return new RemoteChestScreenHandler(ModScreenHandlers.REMOTE_CHEST_SCREEN_HANDLER_TYPE, syncId, playerInventory,
                new SimpleInventory(rows * 9), rows);
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return ModScreenHandlers.REMOTE_CHEST_SCREEN_HANDLER_TYPE;
    }
}
