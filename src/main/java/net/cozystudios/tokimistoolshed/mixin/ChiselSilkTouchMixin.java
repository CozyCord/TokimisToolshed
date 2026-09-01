package net.cozystudios.tokimistoolshed.mixin;

import net.minecraft.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
//? if <1.21 {
import net.cozystudios.tokimistoolshed.item.ChiselItem;
import net.minecraft.enchantment.SilkTouchEnchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//?}

@Mixin(Enchantment.class)
public class ChiselSilkTouchMixin {

    //? if <1.21 {
    @Inject(method = "isAcceptableItem", at = @At("HEAD"), cancellable = true)
    private void tokimistoolshed$acceptChiselForSilkTouch(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!(stack.getItem() instanceof ChiselItem)) return;
        Enchantment self = (Enchantment) (Object) this;
        if (self instanceof SilkTouchEnchantment) {
            cir.setReturnValue(true);
        }
    }
    //?}
}
