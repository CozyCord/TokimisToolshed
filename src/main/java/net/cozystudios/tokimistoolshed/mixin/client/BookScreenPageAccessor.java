package net.cozystudios.tokimistoolshed.mixin.client;

import net.minecraft.client.gui.screen.ingame.BookScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BookScreen.class)
public interface BookScreenPageAccessor {

    @Accessor("pageIndex")
    int tokimistoolshed$getPageIndex();
}
