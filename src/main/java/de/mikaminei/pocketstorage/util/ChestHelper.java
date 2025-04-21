package de.mikaminei.pocketstorage.util;

import de.mikaminei.pocketstorage.PocketStorage;
import de.mikaminei.pocketstorage.screen.ModScreenHandlers;
import de.mikaminei.pocketstorage.screen.NoDistanceCheckScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.enums.ChestType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.UUID;

public class ChestHelper {

    private static final Logger LOGGER = PocketStorage.LOGGER;

    public static boolean isDoubleChest(ChestBlockEntity chestBlockEntity) {
        BlockState state = chestBlockEntity.getCachedState();

        if (state.contains(ChestBlock.CHEST_TYPE)) {
            ChestType type = state.get(ChestBlock.CHEST_TYPE);
            return type != ChestType.SINGLE;
        } else {
            LOGGER.error("ChestBlockEntity state missing CHEST_TYPE property at {}", chestBlockEntity.getPos());
            return false;
        }
    }

    public static boolean isSingleChest(ChestBlockEntity chestBlockEntity) {
        BlockState state = chestBlockEntity.getCachedState();

        if (state.contains(ChestBlock.CHEST_TYPE)) {
            return state.get(ChestBlock.CHEST_TYPE) == ChestType.SINGLE;
        } else {
            LOGGER.error("ChestBlockEntity state missing CHEST_TYPE property at {}", chestBlockEntity.getPos());
            return false;
        }
    }

    public static boolean isValidChestBlockEntity(BlockEntity blockEntity) {
        return blockEntity instanceof ChestBlockEntity chestBlockEntity && !chestBlockEntity.isRemoved() && chestBlockEntity.hasWorld();
    }

    public static boolean openChestGui(ChestBlockEntity chestBlockEntity, PlayerEntity player) {
        if (!isValidChestBlockEntity(chestBlockEntity)) return false;
        if (!(player instanceof ServerPlayerEntity serverPlayer)) return false;

        World world = chestBlockEntity.getWorld();
        BlockPos pos = chestBlockEntity.getPos();

        ChunkLoadingHelper.addTicket((ServerWorld) world, pos);

        BlockState state = world.getBlockState(pos);

        world.playSound(
                null,
                pos,
                SoundEvents.BLOCK_CHEST_OPEN,
                SoundCategory.BLOCKS,
                1.0f,
                world.random.nextFloat() * 0.1f + 0.9f
        );

        UUID playerUuid = serverPlayer.getUuid();
        RemoteAccessData accessData = new RemoteAccessData(pos, world.getRegistryKey());
        RemoteAccessManager.addAccess(playerUuid, accessData);

        /*
        if (state.getBlock() instanceof ChestBlock && state.contains(Properties.OPEN) && !state.get(Properties.OPEN)) {
            world.setBlockState(pos, state.with(Properties.OPEN, true), Block.NOTIFY_LISTENERS);
        }
        */

        serverPlayer.openHandledScreen(new ExtendedScreenHandlerFactory() {
            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
                BlockState state = world.getBlockState(pos);
                Inventory inventory = ChestBlock.getInventory((ChestBlock) state.getBlock(), state, world, pos, true);
                int rows = (inventory != null) ? inventory.size() / 9 : 0;

                buf.writeByte(rows);
            }

            @Override
            public Text getDisplayName() {
                BlockState state = world.getBlockState(pos);
                Inventory inventory = ChestBlock.getInventory((ChestBlock) state.getBlock(), state, world, pos, true);

                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof NamedScreenHandlerFactory factory) return factory.getDisplayName();

                return Text.translatable(inventory != null && inventory.size() > 27 ? "container.chestDouble" : "container.chest");
            }

            @Override
            public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                BlockState state = world.getBlockState(pos);
                Inventory inventory = ChestBlock.getInventory((ChestBlock) state.getBlock(), state, world, pos, true);

                if (inventory == null) return null;

                int rows = inventory.size() / 9;

                return new NoDistanceCheckScreenHandler(
                        ModScreenHandlers.NO_DISTANCE_CHECK_CHEST_SCREEN_HANDLER_TYPE,
                        syncId, playerInventory, inventory, rows
                );
            }
        });

        return true;
    }
}
