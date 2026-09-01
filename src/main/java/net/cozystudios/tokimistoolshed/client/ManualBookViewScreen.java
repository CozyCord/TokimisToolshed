package net.cozystudios.tokimistoolshed.client;

import net.cozystudios.tokimistoolshed.item.ManualContent;
import net.cozystudios.tokimistoolshed.mixin.client.BookViewScreenPageAccessor;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;

public final class ManualBookViewScreen extends BookViewScreen {

    private boolean restoredInitialPage = false;

    public ManualBookViewScreen(BookViewScreen.BookAccess access) {
        super(access);
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
        int page = ((BookViewScreenPageAccessor) (Object) this).tokimistoolshed$getCurrentPage();
        int max = ManualContent.pageCount() - 1;
        if (page >= 0 && page <= max) {
            ManualClientHooks.lastPage = page;
        }
        ManualBookTexture.leave();
        super.removed();
    }
}
