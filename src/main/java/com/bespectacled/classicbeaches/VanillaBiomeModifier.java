package com.bespectacled.classicbeaches;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Predicate;

import com.bespectacled.classicbeaches.surfacebuilder.SurfaceBuilders;
import com.google.common.collect.ImmutableList;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.mixin.biome.BuiltinBiomesAccessor;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.SurfaceConfig;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

@SuppressWarnings("deprecation")
public class VanillaBiomeModifier {
    private static Predicate<BiomeSelectionContext> allSelector = BiomeSelectors.all();
    private static Predicate<BiomeSelectionContext> allButDesertSelector = allSelector.and(BiomeSelectors.excludeByKey(BiomeKeys.DESERT));
    
    private static Predicate<BiomeSelectionContext> allOverworldButSwampSelector;
    
    private static Predicate<BiomeSelectionContext> beachSelector = BiomeSelectors.includeByKey(
        ImmutableList.of(
            BiomeKeys.BEACH,
            BiomeKeys.SNOWY_BEACH
    ));
    
    private static final Identifier DISK_SAND = new Identifier(ClassicBeaches.ID, "disk_sand");
    private static final Identifier DISK_GRAVEL = new Identifier(ClassicBeaches.ID, "disk_gravel");
    private static final Identifier DISK_CLAY = new Identifier(ClassicBeaches.ID, "disk_clay");
    private static final Identifier ORE_CLAY = new Identifier(ClassicBeaches.ID, "ore_clay");
    private static final Identifier REPLACE_BEACH_SURFACE = new Identifier(ClassicBeaches.ID, "replace_beach_surface");
    private static final Identifier REPLACE_DEFAULT_SURFACE = new Identifier(ClassicBeaches.ID, "replace_default_surface");
    
    static {
        List<RegistryKey<Biome>> biomeList = new ArrayList<RegistryKey<Biome>>();
        
        // Collect all biomes that do not use the default surface builder
        // to exclude from getting beach surface builder.
        Iterator biomeIter = BuiltinRegistries.BIOME.getEntries().iterator();
        while (biomeIter.hasNext()) {
            Entry<RegistryKey<Biome>, Biome> entry = (Entry<RegistryKey<Biome>, Biome>)biomeIter.next();
            ConfiguredSurfaceBuilder<?> confBuilder = entry.getValue().getGenerationSettings().getSurfaceBuilder().get();
            SurfaceConfig surfaceConfig = entry.getValue().getGenerationSettings().getSurfaceConfig();
            
            if (!(confBuilder.surfaceBuilder.equals(SurfaceBuilder.DEFAULT) && surfaceConfig.equals(SurfaceBuilder.GRASS_CONFIG))) {
                biomeList.add(entry.getKey());
            }
        }
        
        allOverworldButSwampSelector = allSelector.and(BiomeSelectors.foundInOverworld()).and(BiomeSelectors.excludeByKey(biomeList));
    }
    
    public static void removeDisks() {
        BiomeModifications.create(DISK_SAND).add(ModificationPhase.REMOVALS, allSelector, context -> {
            context.getGenerationSettings().removeBuiltInFeature(ConfiguredFeatures.DISK_SAND);
        });
        
        BiomeModifications.create(DISK_GRAVEL).add(ModificationPhase.REMOVALS, allSelector, context -> {
            context.getGenerationSettings().removeBuiltInFeature(ConfiguredFeatures.DISK_GRAVEL);
        });
        
        BiomeModifications.create(DISK_CLAY).add(ModificationPhase.REMOVALS, allSelector, context -> {
            context.getGenerationSettings().removeBuiltInFeature(ConfiguredFeatures.DISK_CLAY);
        });
        
        
    }
    
    public static void modifySurfaces() {
        BiomeModifications.create(REPLACE_BEACH_SURFACE).add(ModificationPhase.POST_PROCESSING, beachSelector, context -> {
            context.getGenerationSettings().setBuiltInSurfaceBuilder(SurfaceBuilders.CONF_BEACH_SURFACE);
        });
        
        BiomeModifications.create(REPLACE_DEFAULT_SURFACE).add(ModificationPhase.POST_PROCESSING, allOverworldButSwampSelector, context -> {
            context.getGenerationSettings().setBuiltInSurfaceBuilder(SurfaceBuilders.CONF_BEACH_SURFACE); 
        });
    }
    
    public static void addClayOre() {
        BiomeModifications.addFeature(
            allButDesertSelector, 
            GenerationStep.Feature.UNDERGROUND_ORES, 
            RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, ORE_CLAY));
    }
}
