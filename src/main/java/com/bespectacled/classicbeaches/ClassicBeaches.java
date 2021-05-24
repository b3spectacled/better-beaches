package com.bespectacled.classicbeaches;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bespectacled.classicbeaches.config.ClassicBeachesConfig;
import com.bespectacled.classicbeaches.surfacebuilder.SurfaceBuilders;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class ClassicBeaches implements ModInitializer {
    public static final String MOD_ID = "classicbeaches";
    public static final Logger LOGGER = LogManager.getLogger("ClassicBeaches");
    public static final ClassicBeachesConfig CONFIG = AutoConfig.register(ClassicBeachesConfig.class, GsonConfigSerializer::new).getConfig();
    
    public static Identifier createId(String name) {
        return new Identifier(MOD_ID, name);
    }
    
    @Override
    public void onInitialize() {
        LOGGER.log(Level.INFO, "Initializing Classic Beaches...");
        
        SurfaceBuilders.register();

        if (CONFIG.generateBeaches)
            VanillaBiomeModifier.modifySurfaces();
        
        if (CONFIG.generateBeachClay)
            VanillaBiomeModifier.addClayOre();
        
        VanillaBiomeModifier.removeDisks(
            CONFIG.generateSandDisks,
            CONFIG.generateGravelDisks,
            CONFIG.generateClayDisks
        );
    }

}
