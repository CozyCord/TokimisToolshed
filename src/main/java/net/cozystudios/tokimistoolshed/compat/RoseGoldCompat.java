package net.cozystudios.tokimistoolshed.compat;

import net.cozystudios.tokimistoolshed.TokimisToolshed;
import net.cozystudios.tokimistoolshed.item.ExcavatorItem;
import net.cozystudios.tokimistoolshed.item.HammerItem;
import net.cozystudios.tokimistoolshed.item.LumberAxeItem;
import net.cozystudios.tokimistoolshed.item.ScytheItem;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.cozystudios.tokimistoolshed.item.ModToolMaterials;

public class RoseGoldCompat {

    public static Item ROSE_GOLD_EXCAVATOR;
    public static Item ROSE_GOLD_HAMMER;
    public static Item ROSE_GOLD_SCYTHE;
    public static Item ROSE_GOLD_LUMBER_AXE;

    private static Item.Properties settings(String name) {
        return new Item.Properties()
                .setId(ResourceKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(TokimisToolshed.MOD_ID, name)));
    }

    public static void register() {
        ROSE_GOLD_EXCAVATOR = Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(TokimisToolshed.MOD_ID, "rose_gold_excavator"),
                new ExcavatorItem(ModToolMaterials.ROSE_GOLD, 2.0F, -3.0F, settings("rose_gold_excavator"))
        );

        ROSE_GOLD_HAMMER = Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(TokimisToolshed.MOD_ID, "rose_gold_hammer"),
                new HammerItem(ModToolMaterials.ROSE_GOLD, 2.0F, -3.2F, settings("rose_gold_hammer"))
        );

        ROSE_GOLD_SCYTHE = Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(TokimisToolshed.MOD_ID, "rose_gold_scythe"),
                new ScytheItem(ModToolMaterials.ROSE_GOLD, 7.0F, -2.6F, settings("rose_gold_scythe"))
        );

        ROSE_GOLD_LUMBER_AXE = Registry.register(
                BuiltInRegistries.ITEM,
                Identifier.fromNamespaceAndPath(TokimisToolshed.MOD_ID, "rose_gold_lumber_axe"),
                new LumberAxeItem(ModToolMaterials.ROSE_GOLD, 10.0F, -3.3F, settings("rose_gold_lumber_axe"))
        );
    }
}
