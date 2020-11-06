package com.bespectacled.classicbeaches;

import java.util.function.Predicate;

import com.bespectacled.classicbeaches.surfacebuilder.SurfaceBuilders;
import com.google.common.collect.ImmutableList;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeatures;

@SuppressWarnings("deprecation")
public class VanillaBiomeModifier {
    private static Predicate<BiomeSelectionContext> allSelector = BiomeSelectors.all();
    private static Predicate<BiomeSelectionContext> allButDesertSelector = allSelector.and(BiomeSelectors.excludeByKey(BiomeKeys.DESERT));
    private static Predicate<BiomeSelectionContext> beachSelector = BiomeSelectors.includeByKey(
        ImmutableList.of(
            BiomeKeys.BEACH,
            BiomeKeys.SNOWY_BEACH
    ));
    
    private static final Identifier DISK_SAND = new Identifier("disk_sand");
    private static final Identifier DISK_GRAVEL = new Identifier("disk_gravel");
    private static final Identifier DISK_CLAY = new Identifier("disk_clay");
    private static final Identifier ORE_CLAY = new Identifier("ore_clay");
    private static final Identifier SURFACE_DESERT = new Identifier("desert");
    
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
        BiomeModifications.create(SURFACE_DESERT).add(ModificationPhase.REMOVALS, beachSelector, context -> {
            context.getGenerationSettings().setBuiltInSurfaceBuilder(SurfaceBuilders.CONF_BEACH_SURFACE);
        });
    }
    
    public static void addClayOre() {
        BiomeModifications.addFeature(
            allButDesertSelector, 
            GenerationStep.Feature.UNDERGROUND_ORES, 
            RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, new Identifier("classicbeaches", "ore_clay")));
    }
}
