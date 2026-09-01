package net.cozystudios.tokimistoolshed.client;

import net.cozystudios.tokimistoolshed.item.ManualContent;
import net.cozystudios.tokimistoolshed.item.ManualItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.BookScreen;
import net.minecraft.text.Text;
//? if <1.21 {
import net.minecraft.text.StringVisitable;
//?}

import java.util.List;

public final class ManualClientHooks {

    public static int lastPage = 0;

    private ManualClientHooks() {
    }

    public static void register() {
        ManualItem.clientOpener = ManualClientHooks::open;
    }

    private static void open() {
        List<Text> pages = ManualContent.pages();
        //? if <1.21 {
        BookScreen.Contents contents = new BookScreen.Contents() {
            @Override
            public int getPageCount() {
                return pages.size();
            }

            @Override
            public StringVisitable getPageUnchecked(int i) {
                return pages.get(i);
            }
        };
        //?} else {
        /*BookScreen.Contents contents = new BookScreen.Contents(pages);
        *///?}
        MinecraftClient.getInstance().setScreen(new ManualBookScreen(contents));
    }
}
