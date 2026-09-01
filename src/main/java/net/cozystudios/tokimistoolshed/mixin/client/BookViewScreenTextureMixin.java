package net.cozystudios.tokimistoolshed.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.cozystudios.tokimistoolshed.client.ManualBookTexture;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BookViewScreen.class)
public class BookViewScreenTextureMixin {

    @ModifyExpressionValue(
        method = "extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/gui/screens/inventory/BookViewScreen;BOOK_LOCATION:Lnet/minecraft/resources/Identifier;"
        )
    )
    private static Identifier tokimistoolshed$swapBookTexture(Identifier original) {
        return ManualBookTexture.isActive() ? ManualBookTexture.BOOK : original;
    }
}
