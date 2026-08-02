package net.cozystudios.tokimistoolshed.compat;

import net.cozystudios.tokimistoolshed.TokimisToolshed;
import net.cozystudios.tokimistoolshed.item.ExcavatorItem;
import net.cozystudios.tokimistoolshed.item.HammerItem;
import net.cozystudios.tokimistoolshed.item.LumberAxeItem;
import net.cozystudios.tokimistoolshed.item.ScytheItem;
import net.cozystudios.tokimistoolshed.item.ModToolMaterials;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
//? if <1.21 {
import net.minecraft.util.Identifier;
//?} else {
/*import net.minecraft.util.Identifier;
*///?}
//? if >=1.21.2 {
/*import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
*///?}

public class RoseGoldCompat {

    public static Item ROSE_GOLD_EXCAVATOR;
    public static Item ROSE_GOLD_HAMMER;
    public static Item ROSE_GOLD_SCYTHE;
    public static Item ROSE_GOLD_LUMBER_AXE;

    private static Item.Settings settings(String name) {
        //? if >=1.21.2 {
        /*return new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(TokimisToolshed.MOD_ID, name)));
        *///?} else {
        return new Item.Settings();
        //?}
    }

    public static void register() {
        ROSE_GOLD_EXCAVATOR = Registry.register(
                Registries.ITEM,
                //? if <1.21 {
                new Identifier(TokimisToolshed.MOD_ID, "rose_gold_excavator"),
                //?} else {
                /*Identifier.of(TokimisToolshed.MOD_ID, "rose_gold_excavator"),
                *///?}
                new ExcavatorItem(ModToolMaterials.ROSE_GOLD, 2.0F, -3.0F, settings("rose_gold_excavator"))
        );

        ROSE_GOLD_HAMMER = Registry.register(
                Registries.ITEM,
                //? if <1.21 {
                new Identifier(TokimisToolshed.MOD_ID, "rose_gold_hammer"),
                //?} else {
                /*Identifier.of(TokimisToolshed.MOD_ID, "rose_gold_hammer"),
                *///?}
                new HammerItem(ModToolMaterials.ROSE_GOLD, 2.0F, -3.2F, settings("rose_gold_hammer"))
        );

        ROSE_GOLD_SCYTHE = Registry.register(
                Registries.ITEM,
                //? if <1.21 {
                new Identifier(TokimisToolshed.MOD_ID, "rose_gold_scythe"),
                //?} else {
                /*Identifier.of(TokimisToolshed.MOD_ID, "rose_gold_scythe"),
                *///?}
                new ScytheItem(ModToolMaterials.ROSE_GOLD, 7.0F, -2.6F, settings("rose_gold_scythe"))
        );

        ROSE_GOLD_LUMBER_AXE = Registry.register(
                Registries.ITEM,
                //? if <1.21 {
                new Identifier(TokimisToolshed.MOD_ID, "rose_gold_lumber_axe"),
                //?} else {
                /*Identifier.of(TokimisToolshed.MOD_ID, "rose_gold_lumber_axe"),
                *///?}
                new LumberAxeItem(ModToolMaterials.ROSE_GOLD, 10.0F, -3.3F, settings("rose_gold_lumber_axe"))
        );
    }
}
