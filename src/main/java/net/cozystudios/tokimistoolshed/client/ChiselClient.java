package net.cozystudios.tokimistoolshed.client;

import net.cozystudios.tokimistoolshed.item.ChiselItem;
import net.cozystudios.tokimistoolshed.item.ChiselMode;
import net.cozystudios.tokimistoolshed.network.ChiselNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;
//? if <1.21 {
import net.minecraft.network.PacketByteBuf;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
//?}

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
        MinecraftClient client = MinecraftClient.getInstance();
        //? if <1.21.11 {
        long handle = client.getWindow().getHandle();
        return InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_LEFT_ALT)
                || InputUtil.isKeyPressed(handle, GLFW.GLFW_KEY_RIGHT_ALT);
        //?} else {
        /*return InputUtil.isKeyPressed(client.getWindow(), GLFW.GLFW_KEY_LEFT_ALT)
                || InputUtil.isKeyPressed(client.getWindow(), GLFW.GLFW_KEY_RIGHT_ALT);
        *///?}
    }

    public static boolean handleScroll(double vertical) {
        if (vertical == 0.0) return false;
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.currentScreen != null) return false;
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) return false;

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

        sendModePacket(next);
        return true;
    }

    private static void sendModePacket(ChiselMode mode) {
        //? if <1.21 {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeVarInt(mode.ordinal());
        ClientPlayNetworking.send(ChiselNetworking.SET_MODE_ID, buf);
        //?} else {
        /*ClientPlayNetworking.send(new ChiselNetworking.SetChiselModePayload(mode.ordinal()));
        *///?}
    }

    private static ItemStack findChisel(PlayerEntity player) {
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() instanceof ChiselItem) return stack;
        }
        return null;
    }
}
