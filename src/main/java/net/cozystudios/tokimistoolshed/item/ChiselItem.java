package net.cozystudios.tokimistoolshed.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
//? if <1.21 {
import net.minecraft.nbt.NbtElement;
import net.minecraft.client.item.TooltipContext;
//?} else {
/*import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.tooltip.TooltipType;
*///?}
//? if >=1.21.2 {
/*import net.minecraft.component.type.RepairableComponent;
import net.minecraft.registry.entry.RegistryEntryList;
*///?}
//? if >=1.21.5 {
/*import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.text.Text;
import java.util.function.Consumer;
*///?}

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChiselItem extends Item {

    private final ChiselTier tier;

    public ChiselItem(ChiselTier tier, Settings settings) {
        super(applyTierSettings(tier, settings));
        this.tier = tier;
    }

    private static Settings applyTierSettings(ChiselTier tier, Settings settings) {
        settings.maxDamage(tier.durability);
        //? if >=1.21.2 {
        /*settings.component(DataComponentTypes.REPAIRABLE,
                new RepairableComponent(RegistryEntryList.of(tier.repairItem.getRegistryEntry())));
        *///?}
        return settings;
    }

    public ChiselTier getTier() {
        return tier;
    }

    //? if <1.21.2 {
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canRepair(ItemStack stack, ItemStack ingredient) {
        return ingredient.isOf(tier.repairItem);
    }
    //?}

    //? if <1.21 {
    @Override
    public void appendTooltip(ItemStack stack, World world, java.util.List<net.minecraft.text.Text> tooltip, TooltipContext context) {
        tooltip.add(net.minecraft.text.Text.literal("§7Replaces blocks with items from your inventory"));
        tooltip.add(net.minecraft.text.Text.literal("§7Hold §eLeft Alt §7and scroll to switch modes"));
        if (isWornOut(stack)) {
            tooltip.add(net.minecraft.text.Text.literal("§cWorn Out"));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
    //?} elif <1.21.5 {
    /*@Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, java.util.List<net.minecraft.text.Text> tooltip, TooltipType type) {
        tooltip.add(net.minecraft.text.Text.literal("§7Replaces blocks with items from your inventory"));
        tooltip.add(net.minecraft.text.Text.literal("§7Hold §eLeft Alt §7and scroll to switch modes"));
        if (isWornOut(stack)) {
            tooltip.add(net.minecraft.text.Text.literal("§cWorn Out"));
        }
        super.appendTooltip(stack, context, tooltip, type);
    }
    *///?} else {
    /*@Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.literal("§7Replaces blocks with items from your inventory"));
        textConsumer.accept(Text.literal("§7Hold §eLeft Alt §7and scroll to switch modes"));
        if (isWornOut(stack)) {
            textConsumer.accept(Text.literal("§cWorn Out"));
        }
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
    *///?}

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        PlayerEntity player = context.getPlayer();
        if (player == null) return ActionResult.PASS;

        World world = context.getWorld();
        BlockPos targetPos = context.getBlockPos();
        BlockState targetState = world.getBlockState(targetPos);
        if (targetState.isAir()) return ActionResult.PASS;

        ItemStack chiselStack = context.getStack();

        if (isWornOut(chiselStack)) {
            playFailSound(world, player);
            return ActionResult.FAIL;
        }

        ItemStack simulatedTool = new ItemStack(tier.simulatedPickaxe);
        if (targetState.isToolRequired() && !simulatedTool.isSuitableFor(targetState)) {
            playFailSound(world, player);
            return ActionResult.FAIL;
        }

        ChiselMode mode = ChiselMode.read(chiselStack);
        List<ItemStack> candidateStacks = collectCandidates(player, mode);
        if (candidateStacks.isEmpty()) return ActionResult.PASS;

        //? if <1.21.11 {
        if (world.isClient) return ActionResult.SUCCESS;
        //?} else {
        /*if (world.isClient()) return ActionResult.SUCCESS;
        *///?}

        for (ItemStack candidate : new ArrayList<>(candidateStacks)) {
            if (candidate.getItem() instanceof BlockItem bi && bi.getBlock() == targetState.getBlock()) {
                candidateStacks.remove(candidate);
            }
        }
        if (candidateStacks.isEmpty()) return ActionResult.PASS;

        ServerWorld serverWorld = (ServerWorld) world;
        BlockHitResult hitResult = new BlockHitResult(
                context.getHitPos(), context.getSide(), targetPos, false
        );

        for (ItemStack candidate : candidateStacks) {
            if (!(candidate.getItem() instanceof BlockItem blockItem)) continue;

            BlockEntity targetBlockEntity = world.getBlockEntity(targetPos);
            List<ItemStack> targetDrops = computeDrops(targetState, serverWorld, targetPos, targetBlockEntity, player, chiselStack, simulatedTool);

            world.removeBlock(targetPos, false);

            ItemStack placedStack = candidate.copy();
            placedStack.setCount(1);
            ItemPlacementContext placementContext = new ItemPlacementContext(
                    world, player, context.getHand(), placedStack, hitResult
            );

            ActionResult result = blockItem.place(placementContext);
            if (!result.isAccepted()) {
                world.setBlockState(targetPos, targetState);
                continue;
            }

            BlockPos placedPos = placementContext.getBlockPos();
            BlockSoundGroup soundGroup = world.getBlockState(placedPos).getSoundGroup();
            world.playSound(null, placedPos,
                    soundGroup.getPlaceSound(), SoundCategory.BLOCKS,
                    (soundGroup.getVolume() + 1.0F) / 2.0F,
                    soundGroup.getPitch() * 0.8F);

            if (!player.getAbilities().creativeMode) {
                candidate.decrement(1);
                damageChisel(chiselStack);
            }

            if (!player.getAbilities().creativeMode) {
                for (ItemStack drop : targetDrops) {
                    if (drop.isEmpty()) continue;
                    if (!player.getInventory().insertStack(drop)) {
                        player.dropItem(drop, false);
                    }
                }
            }

            return ActionResult.SUCCESS;
        }

        return ActionResult.PASS;
    }

    private static void damageChisel(ItemStack stack) {
        int max = stack.getMaxDamage();
        int next = Math.min(stack.getDamage() + 1, max - 1);
        stack.setDamage(next);
    }

    private static boolean isWornOut(ItemStack stack) {
        return stack.getDamage() >= stack.getMaxDamage() - 1;
    }

    private static void playFailSound(World world, PlayerEntity player) {
        world.playSound(null, player.getX(), player.getY(), player.getZ(),
                SoundEvents.BLOCK_DISPENSER_FAIL, SoundCategory.PLAYERS, 0.8F, 1.0F);
    }

    private static List<ItemStack> computeDrops(BlockState state, ServerWorld world, BlockPos pos,
                                                BlockEntity blockEntity, PlayerEntity player,
                                                ItemStack chisel, ItemStack simulatedTool) {
        copyEnchantments(chisel, simulatedTool);
        return Block.getDroppedStacks(state, world, pos, blockEntity, player, simulatedTool);
    }

    private static void copyEnchantments(ItemStack from, ItemStack to) {
        //? if <1.21 {
        NbtCompound nbt = from.getNbt();
        if (nbt == null) return;
        if (nbt.contains("Enchantments", NbtElement.LIST_TYPE)) {
            to.getOrCreateNbt().put("Enchantments", nbt.getList("Enchantments", NbtElement.COMPOUND_TYPE).copy());
        }
        //?} else {
        /*ItemEnchantmentsComponent enchants = from.get(DataComponentTypes.ENCHANTMENTS);
        if (enchants != null && !enchants.isEmpty()) {
            to.set(DataComponentTypes.ENCHANTMENTS, enchants);
        }
        *///?}
    }

    private static List<ItemStack> collectCandidates(PlayerEntity player, ChiselMode mode) {
        List<ItemStack> out = new ArrayList<>();
        if (mode == ChiselMode.COPY) {
            ItemStack offhand = player.getOffHandStack();
            if (!offhand.isEmpty() && offhand.getItem() instanceof BlockItem) {
                out.add(offhand);
            }
            return out;
        }
        for (int i = 0; i < 9; i++) {
            ItemStack stack = player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;
            if (!(stack.getItem() instanceof BlockItem)) continue;
            if (stack.getItem() instanceof ChiselItem) continue;
            out.add(stack);
        }
        Collections.shuffle(out);
        return out;
    }
}
