package net.cozystudios.tokimistoolshed.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
//? if <1.21.2 {
import net.minecraft.util.TypedActionResult;
//?} else {
/*import net.minecraft.util.ActionResult;
*///?}
//? if <1.21 {
import net.minecraft.client.item.TooltipContext;
//?} else {
/*import net.minecraft.item.tooltip.TooltipType;
*///?}
//? if >=1.21.5 {
/*import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.text.Text;
import java.util.function.Consumer;
*///?}

public class ManualItem extends Item {

    public static Runnable clientOpener = () -> {};

    public ManualItem(Settings settings) {
        super(settings);
    }

    //? if <1.21 {
    @Override
    public void appendTooltip(ItemStack stack, World world, java.util.List<net.minecraft.text.Text> tooltip, TooltipContext context) {
        tooltip.add(net.minecraft.text.Text.literal("§7Right-click to open"));
        super.appendTooltip(stack, world, tooltip, context);
    }
    //?} elif <1.21.5 {
    /*@Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, java.util.List<net.minecraft.text.Text> tooltip, TooltipType type) {
        tooltip.add(net.minecraft.text.Text.literal("§7Right-click to open"));
        super.appendTooltip(stack, context, tooltip, type);
    }
    *///?} else {
    /*@Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, TooltipDisplayComponent displayComponent, Consumer<Text> textConsumer, TooltipType type) {
        textConsumer.accept(Text.literal("§7Right-click to open"));
        super.appendTooltip(stack, context, displayComponent, textConsumer, type);
    }
    *///?}

    //? if <1.21.2 {
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (isClientSide(world)) {
            clientOpener.run();
        }
        return TypedActionResult.success(stack, isClientSide(world));
    }
    //?} else {
    /*@Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (isClientSide(world)) {
            clientOpener.run();
        }
        return ActionResult.SUCCESS;
    }
    *///?}

    private static boolean isClientSide(World world) {
        //? if <1.21.11 {
        return world.isClient;
        //?} else {
        /*return world.isClient();
        *///?}
    }
}
