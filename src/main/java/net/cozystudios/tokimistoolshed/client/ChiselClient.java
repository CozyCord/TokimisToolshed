package net.cozystudios.tokimistoolshed.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.cozystudios.tokimistoolshed.item.ChiselItem;
import net.cozystudios.tokimistoolshed.item.ChiselMode;
import net.cozystudios.tokimistoolshed.network.ChiselNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.lwjgl.glfw.GLFW;

public final class ChiselClient {

    private static long lastChangeMillis = 0L;
    private static ChiselMode lastPrevMode = ChiselMode.COPY;
    private static int lastChangeDirection = -1;

    private ChiselClient() {
    }

    public static long lastChangeMillis() {
        return lastChangeMillis;
    }

    public static ChiselMode lastPrevMode() {
        return lastPrevMode;
    }

    public static int lastChangeDirection() {
        return lastChangeDirection;
    }

    public static boolean isAltHeld() {
        Minecraft client = Minecraft.getInstance();
        return InputConstants.isKeyDown(client.getWindow(), GLFW.GLFW_KEY_LEFT_ALT)
                || InputConstants.isKeyDown(client.getWindow(), GLFW.GLFW_KEY_RIGHT_ALT);
    }

    public static boolean handleScroll(double vertical) {
        if (vertical == 0.0) return false;
        Minecraft client = Minecraft.getInstance();
        if (client.screen != null) return false;
        LocalPlayer player = client.player;
        if (player == null || client.level == null) return false;

        if (!isAltHeld()) return false;

        ItemStack chisel = findChisel(player);
        if (chisel == null) return false;

        ChiselMode current = ChiselMode.read(chisel);
        ChiselMode next = vertical > 0 ? current.next() : current.prev();
        if (next == current) return true;

        ChiselMode.write(chisel, next);
        lastPrevMode = current;
        lastChangeMillis = System.currentTimeMillis();
        lastChangeDirection = vertical > 0 ? 1 : -1;

        ClientPlayNetworking.send(new ChiselNetworking.SetChiselModePayload(next.ordinal()));
        return true;
    }

    private static ItemStack findChisel(Player player) {
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof ChiselItem) return stack;
        }
        return null;
    }
}
