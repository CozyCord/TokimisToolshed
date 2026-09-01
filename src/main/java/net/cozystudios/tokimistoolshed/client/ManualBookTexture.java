package net.cozystudios.tokimistoolshed.client;

import net.minecraft.util.Identifier;

public final class ManualBookTexture {

    //? if <1.21 {
    public static final Identifier BOOK = new Identifier("tokimistoolshed", "textures/gui/manual/book.png");
    public static final Identifier PAGE_FORWARD = new Identifier("tokimistoolshed", "manual/page_forward");
    public static final Identifier PAGE_FORWARD_HIGHLIGHTED = new Identifier("tokimistoolshed", "manual/page_forward_highlighted");
    public static final Identifier PAGE_BACKWARD = new Identifier("tokimistoolshed", "manual/page_backward");
    public static final Identifier PAGE_BACKWARD_HIGHLIGHTED = new Identifier("tokimistoolshed", "manual/page_backward_highlighted");
    //?} else {
    /*public static final Identifier BOOK = Identifier.of("tokimistoolshed", "textures/gui/manual/book.png");
    public static final Identifier PAGE_FORWARD = Identifier.of("tokimistoolshed", "manual/page_forward");
    public static final Identifier PAGE_FORWARD_HIGHLIGHTED = Identifier.of("tokimistoolshed", "manual/page_forward_highlighted");
    public static final Identifier PAGE_BACKWARD = Identifier.of("tokimistoolshed", "manual/page_backward");
    public static final Identifier PAGE_BACKWARD_HIGHLIGHTED = Identifier.of("tokimistoolshed", "manual/page_backward_highlighted");
    *///?}

    private static boolean active = false;

    public static void enter() { active = true; }
    public static void leave() { active = false; }
    public static boolean isActive() { return active; }

    private ManualBookTexture() {}
}
