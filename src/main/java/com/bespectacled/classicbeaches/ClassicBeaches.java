package com.bespectacled.classicbeaches;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bespectacled.classicbeaches.compat.ExcludedBiomes;
import com.bespectacled.classicbeaches.config.ClassicBeachesConfig;
import com.bespectacled.classicbeaches.feature.ConfiguredFeatures;
import com.bespectacled.classicbeaches.surfacebuilder.SurfaceBuilders;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class ClassicBeaches implements ModInitializer {
    public static final String MOD_ID = "classicbeaches";
    public static final String MOD_NAME = "Classic Beaches";
    public static final Logger LOGGER = LogManager.getLogger("ClassicBeaches");
    public static final ClassicBeachesConfig CONFIG = AutoConfig.register(ClassicBeachesConfig.class, GsonConfigSerializer::new).getConfig();
    
    public static Identifier createId(String name) {
        return new Identifier(MOD_ID, name);
    }
    
    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] {}", message);
    }
    
    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing Classic Beaches...");
        
        // Main SurfaceBuilder and Feature Registration
        SurfaceBuilders.register();
        ConfiguredFeatures.register();

        // Mod Compatibility
        ExcludedBiomes.addModCompat();

        if (CONFIG.generateBeaches)
            BiomeModifier.modifySurfaces();
        
        if (CONFIG.generateBeachClay)
            BiomeModifier.addClayOre();
        
        BiomeModifier.removeDisks(
            CONFIG.generateSandDisks,
            CONFIG.generateGravelDisks,
            CONFIG.generateClayDisks
        );
    }

}
