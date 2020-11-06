package com.bespectacled.classicbeaches;

import com.bespectacled.classicbeaches.surfacebuilder.SurfaceBuilders;

import net.fabricmc.api.ModInitializer;

public class ClassicBeaches implements ModInitializer {
    @Override
    public void onInitialize() {
        SurfaceBuilders.register();
        
        VanillaBiomeModifier.removeDisks();
        VanillaBiomeModifier.modifySurfaces();
        VanillaBiomeModifier.addClayOre();
    }

}
