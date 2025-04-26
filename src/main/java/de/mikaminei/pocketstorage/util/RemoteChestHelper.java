package de.mikaminei.pocketstorage.util;

import de.mikaminei.pocketstorage.screen.ModScreenHandlers;
import de.mikaminei.pocketstorage.screen.RemoteChestScreenHandler;
import de.mikaminei.pocketstorage.world.ChunkLoadingHelper;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class RemoteChestHelper {

    public static void open(ServerPlayerEntity player, ChestBlockEntity chestBlockEntity) {
        BlockPos pos = chestBlockEntity.getPos();
        World world = chestBlockEntity.getWorld();

        ChunkLoadingHelper.requestChunkLoad((ServerWorld) world, pos);

        BlockState state = chestBlockEntity.getWorld().getBlockState(pos);

        RemoteAccessData accessData = new RemoteAccessData(pos, world.getRegistryKey());
        RemoteAccessManager.addAccess(player.getUuid(), accessData);

        player.openHandledScreen(new ExtendedScreenHandlerFactory() {

            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                Inventory inventory = ChestBlock.getInventory((ChestBlock) state.getBlock(), state, world, pos, true);
                buf.writeByte((inventory != null) ? inventory.size() / 9 : 0);
            }

            @Override
            public Text getDisplayName() {
                return chestBlockEntity.getDisplayName();
            }

            @Override
            public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                Inventory inventory = ChestBlock.getInventory((ChestBlock) state.getBlock(), state, world, pos, true);
                if (inventory == null) return null;
                return new RemoteChestScreenHandler(ModScreenHandlers.REMOTE_CHEST_SCREEN_HANDLER_TYPE, syncId,
                        playerInventory, inventory, inventory.size() / 9);
            }
        });
    }
}
