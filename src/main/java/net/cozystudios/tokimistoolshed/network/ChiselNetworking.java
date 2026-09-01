package net.cozystudios.tokimistoolshed.network;

import net.cozystudios.tokimistoolshed.TokimisToolshed;
import net.cozystudios.tokimistoolshed.item.ChiselItem;
import net.cozystudios.tokimistoolshed.item.ChiselMode;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;

public final class ChiselNetworking {

    public static final CustomPacketPayload.Type<SetChiselModePayload> SET_MODE_ID =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(TokimisToolshed.MOD_ID, "chisel_set_mode"));

    public static final StreamCodec<RegistryFriendlyByteBuf, SetChiselModePayload> SET_MODE_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT, SetChiselModePayload::mode,
                    SetChiselModePayload::new
            );

    public record SetChiselModePayload(int mode) implements CustomPacketPayload {
        @Override
        public Type<? extends CustomPacketPayload> type() { return SET_MODE_ID; }
    }

    private ChiselNetworking() {
    }

    public static void registerCommon() {
        PayloadTypeRegistry.serverboundPlay().register(SET_MODE_ID, SET_MODE_CODEC);
    }

    public static void registerServer() {
        ServerPlayNetworking.registerGlobalReceiver(SET_MODE_ID, (payload, context) -> {
            int modeIndex = payload.mode();
            context.server().execute(() -> applyMode(context.player(), modeIndex));
        });
    }

    private static void applyMode(ServerPlayer player, int modeIndex) {
        ChiselMode mode = ChiselMode.fromOrdinal(modeIndex);
        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack stack = player.getItemInHand(hand);
            if (stack.getItem() instanceof ChiselItem) {
                ChiselMode.write(stack, mode);
            }
        }
    }
}
