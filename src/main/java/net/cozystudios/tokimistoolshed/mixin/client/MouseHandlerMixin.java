package net.cozystudios.tokimistoolshed.mixin.client;

import net.cozystudios.tokimistoolshed.client.ChiselClient;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Inject(method = "onScroll", at = @At("HEAD"), cancellable = true)
    private void tokimistoolshed$onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (ChiselClient.handleScroll(vertical)) {
            ci.cancel();
        }
    }
}
