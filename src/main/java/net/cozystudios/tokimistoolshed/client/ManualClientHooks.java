package net.cozystudios.tokimistoolshed.client;

import net.cozystudios.tokimistoolshed.item.ManualContent;
import net.cozystudios.tokimistoolshed.item.ManualItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;

public final class ManualClientHooks {

    public static int lastPage = 0;

    private ManualClientHooks() {
    }

    public static void register() {
        ManualItem.clientOpener = ManualClientHooks::open;
    }

    private static void open() {
        BookViewScreen.BookAccess access = new BookViewScreen.BookAccess(ManualContent.pages());
        Minecraft.getInstance().setScreen(new ManualBookViewScreen(access));
    }
}
