package net.cozystudios.tokimistoolshed.mixin.client;

import net.cozystudios.tokimistoolshed.client.ManualBookTexture;
import net.minecraft.client.gui.widget.PageTurnWidget;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
//? if <1.21 {
import net.minecraft.client.gui.screen.ingame.BookScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
//?} else {
/*import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.injection.At;
*///?}

@Mixin(PageTurnWidget.class)
public class PageTurnWidgetTextureMixin {

    //? if <1.21 {
    @Redirect(
        method = "renderButton",
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
    /*@ModifyExpressionValue(method = {"renderWidget", "drawIcon"},
        at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/gui/widget/PageTurnWidget;PAGE_FORWARD_HIGHLIGHTED_TEXTURE:Lnet/minecraft/util/Identifier;"))
    private static Identifier tokimistoolshed$swapForwardHighlighted(Identifier original) {
        return ManualBookTexture.isActive() ? ManualBookTexture.PAGE_FORWARD_HIGHLIGHTED : original;
    }

    @ModifyExpressionValue(method = {"renderWidget", "drawIcon"},
        at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/gui/widget/PageTurnWidget;PAGE_FORWARD_TEXTURE:Lnet/minecraft/util/Identifier;"))
    private static Identifier tokimistoolshed$swapForward(Identifier original) {
        return ManualBookTexture.isActive() ? ManualBookTexture.PAGE_FORWARD : original;
    }

    @ModifyExpressionValue(method = {"renderWidget", "drawIcon"},
        at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/gui/widget/PageTurnWidget;PAGE_BACKWARD_HIGHLIGHTED_TEXTURE:Lnet/minecraft/util/Identifier;"))
    private static Identifier tokimistoolshed$swapBackwardHighlighted(Identifier original) {
        return ManualBookTexture.isActive() ? ManualBookTexture.PAGE_BACKWARD_HIGHLIGHTED : original;
    }

    @ModifyExpressionValue(method = {"renderWidget", "drawIcon"},
        at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/gui/widget/PageTurnWidget;PAGE_BACKWARD_TEXTURE:Lnet/minecraft/util/Identifier;"))
    private static Identifier tokimistoolshed$swapBackward(Identifier original) {
        return ManualBookTexture.isActive() ? ManualBookTexture.PAGE_BACKWARD : original;
    }
    *///?}
}
