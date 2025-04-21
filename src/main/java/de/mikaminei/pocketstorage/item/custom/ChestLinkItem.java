package de.mikaminei.pocketstorage.item.custom;

import de.mikaminei.pocketstorage.util.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
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

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        ItemStack stack = context.getStack();
        BlockPos pos = context.getBlockPos();

        if (player == null) return ActionResult.PASS;
        if (!player.isSneaking()) return ActionResult.PASS;
        if (world.isClient) return ActionResult.SUCCESS;

        BlockEntity blockEntity = world.getBlockEntity(pos);

        if (!ChestHelper.isValidChestBlockEntity(blockEntity)) {
            player.sendMessage(Text.translatable("item.pocket-storage.chest_link.not_linkable")
                    .formatted(Formatting.RED));

            return ActionResult.SUCCESS;
        }

        LinkDataHelper.writeLinkData(stack, pos, world);

        player.sendMessage(Text.translatable("item.pocket-storage.chest_link.link_created",
                pos.getX(), pos.getY(), pos.getZ()).formatted(Formatting.GREEN));

        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (player.isSneaking()) return TypedActionResult.pass(stack);

        if (!world.isClient) {
            LinkData data = LinkDataHelper.readLinkData(stack);

            if (data == null) {
                player.sendMessage(Text.translatable("item.pocket-storage.chest_link.not_linked")
                        .formatted(Formatting.RED));

                return TypedActionResult.success(stack);
            }

            BlockEntity blockEntity = world.getBlockEntity(data.pos());

            if (!ChestHelper.isValidChestBlockEntity(blockEntity)) {
                player.sendMessage(Text.translatable("item.pocket-storage.chest_link.chest_not_existing")
                        .formatted(Formatting.RED));

                return TypedActionResult.success(stack);
            }

            ChestBlockEntity chestBlockEntity = (ChestBlockEntity) blockEntity;

            ChestHelper.openChestGui(chestBlockEntity, player);
        }

        return TypedActionResult.success(stack);
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
            tooltip.add(Text.translatable("item.pocket-storage.chest_link.tooltip.linked_to",
                    data.pos().getX(), data.pos().getY(), data.pos().getZ()).formatted(Formatting.GRAY));
        }
    }
}