package net.cozystudios.tokimistoolshed.client;

import net.cozystudios.tokimistoolshed.item.ChiselItem;
import net.cozystudios.tokimistoolshed.item.ChiselMode;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;

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
        HudRenderCallback.EVENT.register((drawContext, tickDelta) -> renderHud(drawContext));
    }

    private static void renderHud(DrawContext drawContext) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.world == null) return;
        if (client.options.hudHidden) return;

        ItemStack chiselStack = findChisel(client.player);
        if (chiselStack == null) return;
        if (!ChiselClient.isAltHeld()) return;

        ChiselMode current = ChiselMode.read(chiselStack);
        ChiselMode prev = ChiselClient.lastPrevMode();

        long now = System.currentTimeMillis();
        long elapsed = now - ChiselClient.lastChangeMillis();
        float t = elapsed >= ANIM_DURATION_MS ? 1.0f : Math.max(0f, (float) elapsed / ANIM_DURATION_MS);
        float ease = 1f - (1f - t) * (1f - t);

        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        TextRenderer font = client.textRenderer;

        String currentLabel = current.displayName() + " Mode";
        String prevLabel = prev.displayName() + " Mode";
        int maxTextWidth = Math.max(font.getWidth(currentLabel), font.getWidth(prevLabel));
        int boxWidth = maxTextWidth + PADDING_X * 2;
        int boxHeight = LINE_HEIGHT + PADDING_Y * 2;

        int centerX = screenWidth / 2;
        int boxX = centerX - boxWidth / 2;
        int boxY = screenHeight - 45 - boxHeight;

        String hint = "§8Alt + Scroll";
        int hintWidth = font.getWidth(hint);
        drawContext.drawTextWithShadow(font, hint, centerX - hintWidth / 2, boxY - font.fontHeight - 2, OTHER_COLOR);

        drawContext.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, BG_COLOR);

        int rowCenterY = boxY + PADDING_Y + (LINE_HEIGHT - font.fontHeight) / 2;

        if (t < 1.0f && prev != current) {
            int outAlpha = (int) ((1f - ease) * 0xFF) & 0xFF;
            int inAlpha = (int) (ease * 0xFF) & 0xFF;
            int outColorMasked = (outAlpha << 24) | (CURRENT_COLOR & 0x00FFFFFF);
            int inColorMasked = (inAlpha << 24) | (CURRENT_COLOR & 0x00FFFFFF);

            int direction = ChiselClient.lastChangeDirection();
            int outYOffset = direction * (int) (ease * LINE_HEIGHT);
            int inYOffset = -direction * (int) ((1f - ease) * LINE_HEIGHT);

            int prevWidth = font.getWidth(prevLabel);
            int curWidth = font.getWidth(currentLabel);

            enableScissor(drawContext, boxX + 1, boxY + PADDING_Y, boxX + boxWidth - 1, boxY + boxHeight - PADDING_Y);
            drawContext.drawTextWithShadow(font, prevLabel, centerX - prevWidth / 2, rowCenterY + outYOffset, outColorMasked);
            drawContext.drawTextWithShadow(font, currentLabel, centerX - curWidth / 2, rowCenterY + inYOffset, inColorMasked);
            disableScissor(drawContext);
        } else {
            int curWidth = font.getWidth(currentLabel);
            drawContext.drawTextWithShadow(font, currentLabel, centerX - curWidth / 2, rowCenterY, CURRENT_COLOR);
        }

    }

    private static void enableScissor(DrawContext drawContext, int x1, int y1, int x2, int y2) {
        drawContext.enableScissor(x1, y1, x2, y2);
    }

    private static void disableScissor(DrawContext drawContext) {
        drawContext.disableScissor();
    }

    private static ItemStack findChisel(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() instanceof ChiselItem) return stack;
        }
        return null;
    }
}
