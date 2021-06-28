package com.bespectacled.classicbeaches.compat;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.Level;

import com.bespectacled.classicbeaches.ClassicBeaches;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

public class ExcludedBiomes {
    private static final Set<RegistryKey<Biome>> EXCLUDED_BIOMES = new HashSet<>();
    
    public static void addModCompat() {}
    
    public static Set<RegistryKey<Biome>> getExcludedBiomes() {
        return Collections.unmodifiableSet(EXCLUDED_BIOMES);
    }
    
    private static void addExcludedBiomes(String modId, List<String> biomes) {
        try {
            if (FabricLoader.getInstance().isModLoaded(modId)) {
                for (String b : biomes) {
                    ClassicBeaches.log(Level.INFO, "Adding compatibility for mod '" + modId + "', for biome '" + b + "'.");
                    addExcludedBiome(new Identifier(modId, b));
                }
            }
        } catch (Exception e) {
            ClassicBeaches.log(Level.ERROR, "Something blew up when trying to add compatibility for mod '" + modId + "'.");
            e.printStackTrace();
        }
    }
    
    private static void addExcludedBiome(Identifier id) {
        EXCLUDED_BIOMES.add(RegistryKey.of(Registry.BIOME_KEY, id));
    }
}
