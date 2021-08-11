package com.bespectacled.classicbeaches;

import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.bespectacled.classicbeaches.surfacebuilder.SurfaceBuilders;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

@SuppressWarnings("deprecation")
public class BiomeModifier {
    private static final Set<RegistryKey<Biome>> EXCLUDED_BIOMES = ClassicBeaches.CONFIG.excludedBiomes
        .stream()
        .map(b -> RegistryKey.of(Registry.BIOME_KEY, new Identifier(b)))
        .collect(Collectors.toSet());
    
    private static final Set<RegistryKey<Biome>> OVERRIDDEN_BIOMES = ClassicBeaches.CONFIG.overriddenBiomes
        .stream()
        .map(b -> RegistryKey.of(Registry.BIOME_KEY, new Identifier(b)))
        .collect(Collectors.toSet());
    
    private static final Predicate<BiomeSelectionContext> ALL_BIOMES = BiomeSelectors.all();
    private static final Predicate<BiomeSelectionContext> ALL_BIOMES_BUT_DESERT_CATEGORY = BiomeSelectors.categories(Category.DESERT).negate();
    private static final Predicate<BiomeSelectionContext> BIOMES_WITH_NONDEFAULT_SURFACE = BiomeSelectors.includeByKey(OVERRIDDEN_BIOMES);
    
    private static final Identifier REPLACE_NONDEFAULT_SURFACE = ClassicBeaches.createId("replace_nondefault_surface");
    private static final Identifier REPLACE_DEFAULT_SURFACE_GRASS = ClassicBeaches.createId("replace_default_surface_grass");
    
    private static final Identifier REMOVE_DISK_SAND = ClassicBeaches.createId("remove_disk_sand");
    private static final Identifier REMOVE_DISK_GRAV = ClassicBeaches.createId("remove_disk_gravel");
    private static final Identifier REMOVE_DISK_CLAY = ClassicBeaches.createId("remove_disk_clay");
    
    private static final Identifier ORE_CLAY = ClassicBeaches.createId("ore_clay");
    
    public static void removeDisks(boolean generateDiskSand, boolean generateDiskGravel, boolean generateDiskClay) {
        if (!generateDiskSand)
            BiomeModifications.create(REMOVE_DISK_SAND).add(ModificationPhase.REMOVALS, ALL_BIOMES, context -> {
                context.getGenerationSettings().removeBuiltInFeature(ConfiguredFeatures.DISK_SAND);
            });
        
        if (!generateDiskGravel)
            BiomeModifications.create(REMOVE_DISK_GRAV).add(ModificationPhase.REMOVALS, ALL_BIOMES, context -> {
                context.getGenerationSettings().removeBuiltInFeature(ConfiguredFeatures.DISK_GRAVEL);
            });
        
        if (!generateDiskClay)
            BiomeModifications.create(REMOVE_DISK_CLAY).add(ModificationPhase.REMOVALS, ALL_BIOMES, context -> {
                context.getGenerationSettings().removeBuiltInFeature(ConfiguredFeatures.DISK_CLAY);
            });
    }
    
    public static void modifySurfaces() {
        BiomeModifications.create(REPLACE_NONDEFAULT_SURFACE).add(
            ModificationPhase.POST_PROCESSING, 
            BIOMES_WITH_NONDEFAULT_SURFACE, 
            context -> {
                context.getGenerationSettings().setBuiltInSurfaceBuilder(SurfaceBuilders.CONF_BEACH_SURFACE_GRASS);
            }
        );
        
        BiomeModifications.create(REPLACE_DEFAULT_SURFACE_GRASS).add(
            ModificationPhase.POST_PROCESSING, 
            getDefaultSurfaceBiomeSelector(ConfiguredSurfaceBuilders.GRASS), 
            context -> {
                context.getGenerationSettings().setBuiltInSurfaceBuilder(SurfaceBuilders.CONF_BEACH_SURFACE_GRASS); 
            }
        );
    }
    
    public static void addClayOre() {
        BiomeModifications.addFeature(
            ALL_BIOMES_BUT_DESERT_CATEGORY, 
            GenerationStep.Feature.UNDERGROUND_ORES, 
            RegistryKey.of(Registry.CONFIGURED_FEATURE_KEY, ORE_CLAY)
        );
    }
    
    private static Predicate<BiomeSelectionContext> getDefaultSurfaceBiomeSelector(ConfiguredSurfaceBuilder<?> configuredSurfaceBuilder) {
        return ALL_BIOMES.and(BiomeSelectors.includeByKey(
            BuiltinRegistries.BIOME.getEntries()
                .stream()
                .filter(e -> 
                    e.getValue().getGenerationSettings().getSurfaceBuilder().get().equals(configuredSurfaceBuilder) &&
                    !EXCLUDED_BIOMES.contains(e.getKey()))
                .map(e -> e.getKey())
                .collect(Collectors.toList())
        ));
    }
}
