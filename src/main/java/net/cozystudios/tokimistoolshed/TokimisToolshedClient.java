package net.cozystudios.tokimistoolshed;

import net.cozystudios.tokimistoolshed.client.AbacusHudRenderer;
import net.cozystudios.tokimistoolshed.client.AbacusOutlineRenderer;
import net.cozystudios.tokimistoolshed.client.ChiselHudRenderer;
import net.cozystudios.tokimistoolshed.client.ManualClientHooks;
import net.fabricmc.api.ClientModInitializer;

public class TokimisToolshedClient implements ClientModInitializer {


    @Override
    public void onInitializeClient() {
        AbacusOutlineRenderer.register();
        AbacusHudRenderer.register();
        ChiselHudRenderer.register();
        ManualClientHooks.register();
    }
}
