package net.cozystudios.tokimistoolshed.mixin.client;

import net.cozystudios.tokimistoolshed.client.ChiselClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseScrollMixin {

    @Inject(method = "onMouseScroll", at = @At("HEAD"), cancellable = true)
    private void tokimistoolshed$onScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
        if (ChiselClient.handleScroll(vertical)) {
            ci.cancel();
        }
    }
}
