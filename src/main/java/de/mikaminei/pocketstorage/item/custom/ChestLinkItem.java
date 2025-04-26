package de.mikaminei.pocketstorage.item.custom;

import de.mikaminei.pocketstorage.nbt.LinkData;
import de.mikaminei.pocketstorage.nbt.LinkDataHelper;
import de.mikaminei.pocketstorage.util.RemoteChestHelper;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ChestLinkItem extends Item {

    public ChestLinkItem(Settings settings) {
        super(settings.maxCount(1));
    }


    // Shift + Right Click linking
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();

        if (player == null) return ActionResult.PASS;
        if (!player.isSneaking()) return ActionResult.PASS;
        if (world.isClient) return ActionResult.SUCCESS;

        if (!isValidChestBlockEntity(world.getBlockEntity(pos))) {
            player.sendMessage(Text.translatable("item.pocket-storage.chest_link.invalid_block")
                    .formatted(Formatting.RED));
            return ActionResult.SUCCESS;
        }

        LinkDataHelper.writeLinkData(context.getStack(), new LinkData(pos, world.getRegistryKey()));

        player.sendMessage(Text.translatable("item.pocket-storage.chest_link.link_created",
                pos.getX(), pos.getY(), pos.getZ()).formatted(Formatting.GREEN));
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (user.isSneaking()) return TypedActionResult.pass(stack);
        if (world.isClient) return TypedActionResult.success(stack);

        LinkData linkData = LinkDataHelper.readLinkData(stack);
        if (linkData == null) {
            user.sendMessage(Text.translatable("item.pocket-storage.chest_link.not_linked")
                    .formatted(Formatting.RED));
            return TypedActionResult.success(stack);
        }

        BlockEntity blockEntity = world.getBlockEntity(linkData.pos());
        if (!isValidChestBlockEntity(blockEntity)) {
            user.sendMessage(Text.translatable("item.pocket-storage.chest_link.invalid_link",
                    linkData.pos().getX(), linkData.pos().getY(), linkData.pos().getZ()).formatted(Formatting.RED));
            return TypedActionResult.success(stack);
        }

        RemoteChestHelper.open((ServerPlayerEntity) user, (ChestBlockEntity) blockEntity);
        return TypedActionResult.success(stack);
    }

    private static boolean isValidChestBlockEntity(BlockEntity blockEntity) {
        return blockEntity instanceof ChestBlockEntity chestBlockEntity && !chestBlockEntity.isRemoved() && chestBlockEntity.hasWorld();
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        LinkData data = LinkDataHelper.readLinkData(stack);

        if (data == null) {
            tooltip.add(Text.translatable("item.pocket-storage.chest_link.tooltip.not_linked.line1")
                    .formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("item.pocket-storage.chest_link.tooltip.not_linked.line2")
                    .formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("item.pocket-storage.chest_link.tooltip.linked",
                    data.pos().getX(), data.pos().getY(), data.pos().getZ()).formatted(Formatting.GRAY));
        }
    }
}