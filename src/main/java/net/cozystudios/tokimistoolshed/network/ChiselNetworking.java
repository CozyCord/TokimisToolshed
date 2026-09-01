package net.cozystudios.tokimistoolshed.network;

import net.cozystudios.tokimistoolshed.TokimisToolshed;
import net.cozystudios.tokimistoolshed.item.ChiselItem;
import net.cozystudios.tokimistoolshed.item.ChiselMode;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
//? if <1.21 {
import net.minecraft.util.Identifier;
//?} else {
/*import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
*///?}

public final class ChiselNetworking {

    //? if <1.21 {
    public static final Identifier SET_MODE_ID = new Identifier(TokimisToolshed.MOD_ID, "chisel_set_mode");
    //?} else {
    /*public static final CustomPayload.Id<SetChiselModePayload> SET_MODE_ID =
            new CustomPayload.Id<>(Identifier.of(TokimisToolshed.MOD_ID, "chisel_set_mode"));
    public static final PacketCodec<RegistryByteBuf, SetChiselModePayload> SET_MODE_CODEC =
            PacketCodec.tuple(PacketCodecs.VAR_INT, SetChiselModePayload::mode, SetChiselModePayload::new);

    public record SetChiselModePayload(int mode) implements CustomPayload {
        @Override
        public Id<? extends CustomPayload> getId() { return SET_MODE_ID; }
    }
    *///?}

    private ChiselNetworking() {
    }

    public static void registerServer() {
        //? if <1.21 {
        ServerPlayNetworking.registerGlobalReceiver(SET_MODE_ID, (server, player, handler, buf, responseSender) -> {
            int modeIndex = buf.readVarInt();
            server.execute(() -> applyMode(player, modeIndex));
        });
        //?} else {
        /*PayloadTypeRegistry.playC2S().register(SET_MODE_ID, SET_MODE_CODEC);
        ServerPlayNetworking.registerGlobalReceiver(SET_MODE_ID, (payload, context) -> {
            int modeIndex = payload.mode();
            context.server().execute(() -> applyMode(context.player(), modeIndex));
        });
        *///?}
    }

    private static void applyMode(ServerPlayerEntity player, int modeIndex) {
        ChiselMode mode = ChiselMode.fromOrdinal(modeIndex);
        for (Hand hand : Hand.values()) {
            ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() instanceof ChiselItem) {
                ChiselMode.write(stack, mode);
            }
        }
    }
}
