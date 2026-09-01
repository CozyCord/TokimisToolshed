package net.cozystudios.tokimistoolshed.item;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.List;

public final class ManualContent {

    private static final String ICON_EXCAVATOR      = "";
    private static final String ICON_HAMMER         = "";
    private static final String ICON_SCYTHE         = "";
    private static final String ICON_LUMBER_AXE     = "";
    private static final String ICON_CLIPPERS       = "";
    private static final String ICON_IRON_CHISEL    = "";
    private static final String ICON_TROWEL         = "";
    private static final String ICON_ABACUS         = "";
    private static final String ICON_COPPER_BUCK    = "";
    private static final String ICON_DIAMOND_CHISEL = "";

    private ManualContent() {
    }

    public static int pageCount() {
        return 12;
    }

    public static List<Component> pages() {
        Style iconStyle = manualFontStyle();

        List<Component> out = new ArrayList<>(12);
        out.add(Component.translatable("item.tokimistoolshed.manual.page.1"));
        out.add(Component.translatable("item.tokimistoolshed.manual.page.2",  icon(ICON_EXCAVATOR,       iconStyle)));
        out.add(Component.translatable("item.tokimistoolshed.manual.page.3",  icon(ICON_HAMMER,          iconStyle)));
        out.add(Component.translatable("item.tokimistoolshed.manual.page.4",  icon(ICON_SCYTHE,          iconStyle)));
        out.add(Component.translatable("item.tokimistoolshed.manual.page.5",  icon(ICON_LUMBER_AXE,      iconStyle)));
        out.add(Component.translatable("item.tokimistoolshed.manual.page.6",  icon(ICON_CLIPPERS,        iconStyle)));
        out.add(Component.translatable("item.tokimistoolshed.manual.page.7",
                icon(ICON_IRON_CHISEL, iconStyle),
                icon(ICON_DIAMOND_CHISEL, iconStyle)));
        out.add(Component.translatable("item.tokimistoolshed.manual.page.8",  icon(ICON_IRON_CHISEL,     iconStyle)));
        out.add(Component.translatable("item.tokimistoolshed.manual.page.9",  icon(ICON_IRON_CHISEL,     iconStyle)));
        out.add(Component.translatable("item.tokimistoolshed.manual.page.10", icon(ICON_TROWEL,          iconStyle)));
        out.add(Component.translatable("item.tokimistoolshed.manual.page.11", icon(ICON_ABACUS,          iconStyle)));
        out.add(Component.translatable("item.tokimistoolshed.manual.page.12", icon(ICON_COPPER_BUCK,     iconStyle)));
        return out;
    }

    private static MutableComponent icon(String glyph, Style style) {
        return Component.literal(glyph).setStyle(style);
    }

    private static Style manualFontStyle() {
        Identifier id = Identifier.fromNamespaceAndPath("tokimistoolshed", "manual");
        return Style.EMPTY.withColor(0xFFFFFF).withFont(new net.minecraft.network.chat.FontDescription.Resource(id));
    }
}
