package com.bespectacled.classicbeaches;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bespectacled.classicbeaches.surfacebuilder.SurfaceBuilders;

import net.fabricmc.api.ModInitializer;

public class ClassicBeaches implements ModInitializer {
    public static final String ID = "classic_beaches";
    public static final Logger LOGGER = LogManager.getLogger("ClassicBeaches");
    
    @Override
    public void onInitialize() {
        LOGGER.log(Level.INFO, "Initializing Classic Beaches...");
        
        SurfaceBuilders.register();
        
        VanillaBiomeModifier.removeDisks();
        VanillaBiomeModifier.modifySurfaces();
        VanillaBiomeModifier.addClayOre();
        
        LOGGER.log(Level.INFO, "Initialized Classic Beaches!");
    }

}
