package net.cozystudios.tokimistoolshed.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum ChiselTier {
    IRON(325, Items.IRON_INGOT, Items.IRON_PICKAXE),
    DIAMOND(810, Items.DIAMOND, Items.DIAMOND_PICKAXE);

    public final int durability;
    public final Item repairItem;
    public final Item simulatedPickaxe;

    ChiselTier(int durability, Item repairItem, Item simulatedPickaxe) {
        this.durability = durability;
        this.repairItem = repairItem;
        this.simulatedPickaxe = simulatedPickaxe;
    }
}
