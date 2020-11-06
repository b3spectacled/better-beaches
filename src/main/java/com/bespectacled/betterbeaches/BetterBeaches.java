package com.bespectacled.betterbeaches;

import com.bespectacled.betterbeaches.surfacebuilder.SurfaceBuilders;

import net.fabricmc.api.ModInitializer;

public class BetterBeaches implements ModInitializer {
    @Override
    public void onInitialize() {
        SurfaceBuilders.register();
        
        VanillaBiomeModifier.removeDisks();
        VanillaBiomeModifier.modifySurfaces();
        VanillaBiomeModifier.addClayOre();
    }

}
