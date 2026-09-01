package net.cozystudios.tokimistoolshed.client;

import net.cozystudios.tokimistoolshed.item.ManualContent;
import net.cozystudios.tokimistoolshed.mixin.client.BookScreenPageAccessor;
import net.minecraft.client.gui.screen.ingame.BookScreen;

public final class ManualBookScreen extends BookScreen {

    private boolean restoredInitialPage = false;

    public ManualBookScreen(BookScreen.Contents contents) {
        super(contents);
        ManualBookTexture.enter();
    }

    @Override
    protected void init() {
        super.init();
        if (!restoredInitialPage) {
            restoredInitialPage = true;
            setPage(ManualClientHooks.lastPage);
        }
    }

    @Override
    public void removed() {
        int page = ((BookScreenPageAccessor) (Object) this).tokimistoolshed$getPageIndex();
        int max = ManualContent.pageCount() - 1;
        if (page >= 0 && page <= max) {
            ManualClientHooks.lastPage = page;
        }
        ManualBookTexture.leave();
        super.removed();
    }
}
