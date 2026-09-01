package net.cozystudios.tokimistoolshed.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import java.util.function.Consumer;

public class TrowelItem extends Item {

    public TrowelItem(Properties settings) {
        super(settings);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.literal("§7Places a random block from your hotbar"));
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        Level world = context.getLevel();

        boolean hasBlocks = false;
        for (int i = 0; i < 9; i++) {
            ItemStack hotbarStack = player.getInventory().getItem(i);
            if (!hotbarStack.isEmpty() && hotbarStack.getItem() instanceof BlockItem) {
                hasBlocks = true;
                break;
            }
        }
        if (!hasBlocks) return InteractionResult.PASS;

        if (world.isClientSide()) return InteractionResult.SUCCESS;

        List<Integer> blockSlots = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ItemStack hotbarStack = player.getInventory().getItem(i);
            if (!hotbarStack.isEmpty() && hotbarStack.getItem() instanceof BlockItem) {
                blockSlots.add(i);
            }
        }

        Collections.shuffle(blockSlots);

        BlockHitResult hitResult = new BlockHitResult(
                context.getClickLocation(), context.getClickedFace(), context.getClickedPos(), false
        );

        for (int slot : blockSlots) {
            ItemStack blockStack = player.getInventory().getItem(slot);
            BlockItem blockItem = (BlockItem) blockStack.getItem();

            BlockPlaceContext placementContext = new BlockPlaceContext(
                    world, player, context.getHand(), blockStack, hitResult
            );

            InteractionResult result = blockItem.place(placementContext);
            if (result.consumesAction()) {
                BlockPos placedPos = placementContext.getClickedPos();
                SoundType soundGroup = world.getBlockState(placedPos).getSoundType();
                world.playSound(null, placedPos,
                        soundGroup.getPlaceSound(), SoundSource.BLOCKS,
                        (soundGroup.getVolume() + 1.0F) / 2.0F,
                        soundGroup.getPitch() * 0.8F);
                return result;
            }
        }

        return InteractionResult.PASS;
    }
}
