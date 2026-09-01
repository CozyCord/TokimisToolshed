package net.cozystudios.tokimistoolshed.item;

import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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

    public static List<Text> pages() {
        Style iconStyle = manualFontStyle();

        List<Text> out = new ArrayList<>(12);
        out.add(Text.translatable("item.tokimistoolshed.manual.page.1"));
        out.add(Text.translatable("item.tokimistoolshed.manual.page.2",  icon(ICON_EXCAVATOR,       iconStyle)));
        out.add(Text.translatable("item.tokimistoolshed.manual.page.3",  icon(ICON_HAMMER,          iconStyle)));
        out.add(Text.translatable("item.tokimistoolshed.manual.page.4",  icon(ICON_SCYTHE,          iconStyle)));
        out.add(Text.translatable("item.tokimistoolshed.manual.page.5",  icon(ICON_LUMBER_AXE,      iconStyle)));
        out.add(Text.translatable("item.tokimistoolshed.manual.page.6",  icon(ICON_CLIPPERS,        iconStyle)));
        out.add(Text.translatable("item.tokimistoolshed.manual.page.7",
                icon(ICON_IRON_CHISEL, iconStyle),
                icon(ICON_DIAMOND_CHISEL, iconStyle)));
        out.add(Text.translatable("item.tokimistoolshed.manual.page.8",  icon(ICON_IRON_CHISEL,     iconStyle)));
        out.add(Text.translatable("item.tokimistoolshed.manual.page.9",  icon(ICON_IRON_CHISEL,     iconStyle)));
        out.add(Text.translatable("item.tokimistoolshed.manual.page.10", icon(ICON_TROWEL,          iconStyle)));
        out.add(Text.translatable("item.tokimistoolshed.manual.page.11", icon(ICON_ABACUS,          iconStyle)));
        out.add(Text.translatable("item.tokimistoolshed.manual.page.12", icon(ICON_COPPER_BUCK,     iconStyle)));
        return out;
    }

    private static MutableText icon(String glyph, Style style) {
        return Text.literal(glyph).setStyle(style);
    }

    private static Identifier manualFontId() {
        //? if <1.21 {
        return new Identifier("tokimistoolshed", "manual");
        //?} else {
        /*return Identifier.of("tokimistoolshed", "manual");
        *///?}
    }

    private static Style manualFontStyle() {
        //? if <1.21.11 {
        return Style.EMPTY.withColor(0xFFFFFF).withFont(manualFontId());
        //?} else {
        /*return Style.EMPTY.withColor(0xFFFFFF).withFont(new net.minecraft.text.StyleSpriteSource.Font(manualFontId()));
        *///?}
    }
}
