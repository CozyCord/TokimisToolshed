package net.cozystudios.tokimistoolshed.mixin.client;

import net.cozystudios.tokimistoolshed.client.ManualBookTexture;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
//? if <1.21 {
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.injection.Redirect;
//?} else {
/*import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
*///?}

@Mixin(BookScreen.class)
public class BookScreenTextureMixin {

    //? if <1.21 {
    @Redirect(
        method = "render",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/screen/ingame/BookScreen;BOOK_TEXTURE:Lnet/minecraft/util/Identifier;",
            opcode = Opcodes.GETSTATIC
        )
    )
    private static Identifier tokimistoolshed$swapBookTexture() {
        return ManualBookTexture.isActive() ? ManualBookTexture.BOOK : BookScreen.BOOK_TEXTURE;
    }
    //?} else {
    /*@ModifyExpressionValue(
        method = "renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/screen/ingame/BookScreen;BOOK_TEXTURE:Lnet/minecraft/util/Identifier;"
        )
    )
    private static Identifier tokimistoolshed$swapBookTexture(Identifier original) {
        return ManualBookTexture.isActive() ? ManualBookTexture.BOOK : original;
    }
    *///?}
}
