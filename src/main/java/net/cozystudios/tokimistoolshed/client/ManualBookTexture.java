package net.cozystudios.tokimistoolshed.client;

import net.minecraft.resources.Identifier;

public final class ManualBookTexture {

    public static final Identifier BOOK = Identifier.fromNamespaceAndPath("tokimistoolshed", "textures/gui/manual/book.png");
    public static final Identifier PAGE_FORWARD = Identifier.fromNamespaceAndPath("tokimistoolshed", "manual/page_forward");
    public static final Identifier PAGE_FORWARD_HIGHLIGHTED = Identifier.fromNamespaceAndPath("tokimistoolshed", "manual/page_forward_highlighted");
    public static final Identifier PAGE_BACKWARD = Identifier.fromNamespaceAndPath("tokimistoolshed", "manual/page_backward");
    public static final Identifier PAGE_BACKWARD_HIGHLIGHTED = Identifier.fromNamespaceAndPath("tokimistoolshed", "manual/page_backward_highlighted");

    private static boolean active = false;

    public static void enter() { active = true; }
    public static void leave() { active = false; }
    public static boolean isActive() { return active; }

    private ManualBookTexture() {}
}
