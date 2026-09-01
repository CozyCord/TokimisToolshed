package net.cozystudios.tokimistoolshed.client;

import net.cozystudios.tokimistoolshed.TokimisToolshed;
import net.cozystudios.tokimistoolshed.item.ChiselItem;
import net.cozystudios.tokimistoolshed.item.ChiselMode;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public final class ChiselHudRenderer {

    private static final long ANIM_DURATION_MS = 260L;
    private static final int LINE_HEIGHT = 12;
    private static final int PADDING_X = 8;
    private static final int PADDING_Y = 4;
    private static final int BG_COLOR = 0x80000000;
    private static final int CURRENT_COLOR = 0xFFFFFFFF;
    private static final int OTHER_COLOR = 0x80AAAAAA;

    private ChiselHudRenderer() {
    }

    public static void register() {
        HudElementRegistry.addLast(
                Identifier.fromNamespaceAndPath(TokimisToolshed.MOD_ID, "chisel_hud"),
                (HudElement) (drawContext, deltaTracker) -> renderHud(drawContext)
        );
    }

    private static void renderHud(GuiGraphicsExtractor drawContext) {
        Minecraft client = Minecraft.getInstance();
        if (client.player == null || client.level == null) return;
        if (client.gui.hud.isHidden()) return;

        ItemStack chiselStack = findChisel(client.player);
        if (chiselStack == null) return;
        if (!ChiselClient.isAltHeld()) return;

        ChiselMode current = ChiselMode.read(chiselStack);
        ChiselMode prev = ChiselClient.lastPrevMode();

        long now = System.currentTimeMillis();
        long elapsed = now - ChiselClient.lastChangeMillis();
        float t = elapsed >= ANIM_DURATION_MS ? 1.0f : Math.max(0f, (float) elapsed / ANIM_DURATION_MS);
        float ease = 1f - (1f - t) * (1f - t);

        int screenWidth = drawContext.guiWidth();
        int screenHeight = drawContext.guiHeight();

        Font font = client.font;

        String currentLabel = current.displayName() + " Mode";
        String prevLabel = prev.displayName() + " Mode";
        int maxTextWidth = Math.max(font.width(currentLabel), font.width(prevLabel));
        int boxWidth = maxTextWidth + PADDING_X * 2;
        int boxHeight = LINE_HEIGHT + PADDING_Y * 2;

        int centerX = screenWidth / 2;
        int boxX = centerX - boxWidth / 2;
        int boxY = screenHeight - 45 - boxHeight;

        String hint = "§8Alt + Scroll";
        int hintWidth = font.width(hint);
        drawContext.text(font, hint, centerX - hintWidth / 2, boxY - font.lineHeight - 2, OTHER_COLOR);

        drawContext.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, BG_COLOR);

        int rowCenterY = boxY + PADDING_Y + (LINE_HEIGHT - font.lineHeight) / 2;

        if (t < 1.0f && prev != current) {
            int outAlpha = (int) ((1f - ease) * 0xFF) & 0xFF;
            int inAlpha = (int) (ease * 0xFF) & 0xFF;
            int outColorMasked = (outAlpha << 24) | (CURRENT_COLOR & 0x00FFFFFF);
            int inColorMasked = (inAlpha << 24) | (CURRENT_COLOR & 0x00FFFFFF);

            int direction = ChiselClient.lastChangeDirection();
            int outYOffset = direction * (int) (ease * LINE_HEIGHT);
            int inYOffset = -direction * (int) ((1f - ease) * LINE_HEIGHT);

            int prevWidth = font.width(prevLabel);
            int curWidth = font.width(currentLabel);

            drawContext.enableScissor(boxX + 1, boxY + PADDING_Y, boxX + boxWidth - 1, boxY + boxHeight - PADDING_Y);
            drawContext.text(font, prevLabel, centerX - prevWidth / 2, rowCenterY + outYOffset, outColorMasked);
            drawContext.text(font, currentLabel, centerX - curWidth / 2, rowCenterY + inYOffset, inColorMasked);
            drawContext.disableScissor();
        } else {
            int curWidth = font.width(currentLabel);
            drawContext.text(font, currentLabel, centerX - curWidth / 2, rowCenterY, CURRENT_COLOR);
        }

    }

    private static ItemStack findChisel(Player player) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof ChiselItem) return stack;
        }
        return null;
    }
}
