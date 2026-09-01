package net.cozystudios.tokimistoolshed.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.cozystudios.tokimistoolshed.client.ManualBookTexture;
import net.minecraft.client.gui.screens.inventory.PageButton;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PageButton.class)
public class PageButtonTextureMixin {

    @ModifyExpressionValue(
        method = "extractContents(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
        at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/gui/screens/inventory/PageButton;PAGE_FORWARD_HIGHLIGHTED_SPRITE:Lnet/minecraft/resources/Identifier;")
    )
    private static Identifier tokimistoolshed$swapForwardHighlighted(Identifier original) {
        return ManualBookTexture.isActive() ? ManualBookTexture.PAGE_FORWARD_HIGHLIGHTED : original;
    }

    @ModifyExpressionValue(
        method = "extractContents(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
        at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/gui/screens/inventory/PageButton;PAGE_FORWARD_SPRITE:Lnet/minecraft/resources/Identifier;")
    )
    private static Identifier tokimistoolshed$swapForward(Identifier original) {
        return ManualBookTexture.isActive() ? ManualBookTexture.PAGE_FORWARD : original;
    }

    @ModifyExpressionValue(
        method = "extractContents(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
        at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/gui/screens/inventory/PageButton;PAGE_BACKWARD_HIGHLIGHTED_SPRITE:Lnet/minecraft/resources/Identifier;")
    )
    private static Identifier tokimistoolshed$swapBackwardHighlighted(Identifier original) {
        return ManualBookTexture.isActive() ? ManualBookTexture.PAGE_BACKWARD_HIGHLIGHTED : original;
    }

    @ModifyExpressionValue(
        method = "extractContents(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
        at = @At(value = "FIELD",
            target = "Lnet/minecraft/client/gui/screens/inventory/PageButton;PAGE_BACKWARD_SPRITE:Lnet/minecraft/resources/Identifier;")
    )
    private static Identifier tokimistoolshed$swapBackward(Identifier original) {
        return ManualBookTexture.isActive() ? ManualBookTexture.PAGE_BACKWARD : original;
    }
}
