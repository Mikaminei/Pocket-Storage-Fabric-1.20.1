package de.mikaminei.pocketstorage.item.custom;

import de.mikaminei.pocketstorage.nbt.LinkData;
import de.mikaminei.pocketstorage.nbt.LinkDataHelper;
import de.mikaminei.pocketstorage.screen.RemoteCraftingScreenHandler;
import net.minecraft.block.Block;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
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

public class CraftingLinkItem extends Item {

    public CraftingLinkItem(Settings settings) {
        super(settings.maxCount(1));
    }

    // linking
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos();

        if (world.isClient) return ActionResult.SUCCESS;
        if (player == null) return ActionResult.PASS;
        if (!player.isSneaking()) return ActionResult.PASS;

        Block block = world.getBlockState(pos).getBlock();

        if (!(block instanceof CraftingTableBlock)) {
            player.sendMessage(Text.translatable("item.pocket-storage.crafting_link.invalid_block")
                    .formatted(Formatting.RED));
            return ActionResult.SUCCESS;
        }

        LinkDataHelper.writeLinkData(context.getStack(), new LinkData(pos, world.getRegistryKey()));
        player.sendMessage(Text.translatable("item.pocket-storage.crafting_link.link_created",
                        pos.getX(), pos.getY(), pos.getZ())
                .formatted(Formatting.GREEN));
        return ActionResult.SUCCESS;
    }

    // opening
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (world.isClient) return TypedActionResult.success(stack);

        ServerPlayerEntity player = (ServerPlayerEntity) user;
        if (player.isSneaking()) return TypedActionResult.pass(stack);

        LinkData linkData = LinkDataHelper.readLinkData(stack);
        if (linkData == null) {
            player.sendMessage(Text.translatable("item.pocket-storage.crafting_link.not_linked")
                    .formatted(Formatting.RED));
            return TypedActionResult.success(stack);
        }

        Block block = world.getBlockState(linkData.pos()).getBlock();
        if (!(block instanceof CraftingTableBlock)) return TypedActionResult.success(stack);

        player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, player1) ->
                new RemoteCraftingScreenHandler(syncId, playerInventory, ScreenHandlerContext.create(world, linkData.pos())),
                Text.translatable("container.crafting")));
        return TypedActionResult.success(stack);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        LinkData data = LinkDataHelper.readLinkData(stack);

        if (data == null) {
            tooltip.add(Text.translatable("item.pocket-storage.crafting_link.tooltip.not_linked.line1")
                    .formatted(Formatting.GRAY));
            tooltip.add(Text.translatable("item.pocket-storage.crafting_link.tooltip.not_linked.line2")
                    .formatted(Formatting.GRAY));
        } else {
            tooltip.add(Text.translatable("item.pocket-storage.crafting_link.tooltip.linked",
                            data.pos().getX(), data.pos().getY(), data.pos().getZ())
                    .formatted(Formatting.GRAY));
        }
    }
}
