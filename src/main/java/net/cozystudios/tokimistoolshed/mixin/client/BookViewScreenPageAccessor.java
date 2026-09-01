package net.cozystudios.tokimistoolshed.mixin.client;

import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BookViewScreen.class)
public interface BookViewScreenPageAccessor {

    @Accessor("currentPage")
    int tokimistoolshed$getCurrentPage();
}
