package net.cozystudios.tokimistoolshed.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.item.enchantment.Repairable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class ChiselItem extends Item {

    private final ChiselTier tier;

    public ChiselItem(ChiselTier tier, Properties settings) {
        super(applyTierSettings(tier, settings));
        this.tier = tier;
    }

    private static Properties applyTierSettings(ChiselTier tier, Properties settings) {
        settings.durability(tier.durability);
        settings.component(DataComponents.REPAIRABLE,
                new Repairable(HolderSet.direct(tier.repairItem.builtInRegistryHolder())));
        return settings;
    }

    public ChiselTier getTier() {
        return tier;
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay displayComponent, Consumer<Component> textConsumer, TooltipFlag type) {
        textConsumer.accept(Component.literal("§7Replaces blocks with items from your inventory"));
        textConsumer.accept(Component.literal("§7Hold §eLeft Alt §7and scroll to switch modes"));
        if (isWornOut(stack)) {
            textConsumer.accept(Component.literal("§cWorn Out"));
        }
        super.appendHoverText(stack, context, displayComponent, textConsumer, type);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        Level world = context.getLevel();
        BlockPos targetPos = context.getClickedPos();
        BlockState targetState = world.getBlockState(targetPos);
        if (targetState.isAir()) return InteractionResult.PASS;

        ItemStack chiselStack = context.getItemInHand();

        if (isWornOut(chiselStack)) {
            playFailSound(world, player);
            return InteractionResult.FAIL;
        }

        ItemStack simulatedTool = new ItemStack(tier.simulatedPickaxe);
        if (targetState.requiresCorrectToolForDrops() && !simulatedTool.isCorrectToolForDrops(targetState)) {
            playFailSound(world, player);
            return InteractionResult.FAIL;
        }

        ChiselMode mode = ChiselMode.read(chiselStack);
        List<ItemStack> candidateStacks = collectCandidates(player, mode);
        if (candidateStacks.isEmpty()) return InteractionResult.PASS;

        if (world.isClientSide()) return InteractionResult.SUCCESS;

        for (ItemStack candidate : new ArrayList<>(candidateStacks)) {
            if (candidate.getItem() instanceof BlockItem bi && bi.getBlock() == targetState.getBlock()) {
                candidateStacks.remove(candidate);
            }
        }
        if (candidateStacks.isEmpty()) return InteractionResult.PASS;

        ServerLevel serverWorld = (ServerLevel) world;
        BlockHitResult hitResult = new BlockHitResult(
                context.getClickLocation(), context.getClickedFace(), targetPos, false
        );

        for (ItemStack candidate : candidateStacks) {
            if (!(candidate.getItem() instanceof BlockItem blockItem)) continue;

            BlockEntity targetBlockEntity = world.getBlockEntity(targetPos);
            List<ItemStack> targetDrops = computeDrops(targetState, serverWorld, targetPos, targetBlockEntity, player, chiselStack, simulatedTool);

            world.removeBlock(targetPos, false);

            ItemStack placedStack = candidate.copy();
            placedStack.setCount(1);
            BlockPlaceContext placementContext = new BlockPlaceContext(
                    world, player, context.getHand(), placedStack, hitResult
            );

            InteractionResult result = blockItem.place(placementContext);
            if (!result.consumesAction()) {
                world.setBlockAndUpdate(targetPos, targetState);
                continue;
            }

            BlockPos placedPos = placementContext.getClickedPos();
            SoundType soundGroup = world.getBlockState(placedPos).getSoundType();
            world.playSound(null, placedPos,
                    soundGroup.getPlaceSound(), SoundSource.BLOCKS,
                    (soundGroup.getVolume() + 1.0F) / 2.0F,
                    soundGroup.getPitch() * 0.8F);

            if (!player.getAbilities().instabuild) {
                candidate.shrink(1);
                damageChisel(chiselStack);
            }

            if (!player.getAbilities().instabuild) {
                for (ItemStack drop : targetDrops) {
                    if (drop.isEmpty()) continue;
                    if (!player.getInventory().add(drop)) {
                        player.drop(drop, false);
                    }
                }
            }

            return InteractionResult.SUCCESS;
        }

        return InteractionResult.PASS;
    }

    private static void damageChisel(ItemStack stack) {
        int max = stack.getMaxDamage();
        int next = Math.min(stack.getDamageValue() + 1, max - 1);
        stack.setDamageValue(next);
    }

    private static boolean isWornOut(ItemStack stack) {
        return stack.getDamageValue() >= stack.getMaxDamage() - 1;
    }

    private static void playFailSound(Level world, Player player) {
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 0.8F, 1.0F);
    }

    private static List<ItemStack> computeDrops(BlockState state, ServerLevel world, BlockPos pos,
                                                BlockEntity blockEntity, Player player,
                                                ItemStack chisel, ItemStack simulatedTool) {
        copyEnchantments(chisel, simulatedTool);
        return Block.getDrops(state, world, pos, blockEntity, player, simulatedTool);
    }

    private static void copyEnchantments(ItemStack from, ItemStack to) {
        ItemEnchantments enchants = from.get(DataComponents.ENCHANTMENTS);
        if (enchants != null && !enchants.isEmpty()) {
            to.set(DataComponents.ENCHANTMENTS, enchants);
        }
    }

    private static List<ItemStack> collectCandidates(Player player, ChiselMode mode) {
        List<ItemStack> out = new ArrayList<>();
        if (mode == ChiselMode.COPY) {
            ItemStack offhand = player.getOffhandItem();
            if (!offhand.isEmpty() && offhand.getItem() instanceof BlockItem) {
                out.add(offhand);
            }
            return out;
        }
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (stack.isEmpty()) continue;
            if (!(stack.getItem() instanceof BlockItem)) continue;
            if (stack.getItem() instanceof ChiselItem) continue;
            out.add(stack);
        }
        Collections.shuffle(out);
        return out;
    }
}
