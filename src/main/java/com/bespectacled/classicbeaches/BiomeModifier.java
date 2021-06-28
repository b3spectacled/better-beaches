package com.bespectacled.classicbeaches;

import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.bespectacled.classicbeaches.compat.ExcludedBiomes;
import com.bespectacled.classicbeaches.surfacebuilder.SurfaceBuilders;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.surfacebuilder.SurfaceBuilder;

@SuppressWarnings("deprecation")
public class BiomeModifier {
    private static final Predicate<BiomeSelectionContext> ALL_BIOMES = BiomeSelectors.all();
    private static final Predicate<BiomeSelectionContext> ALL_BIOMES_BUT_DESERT = BiomeSelectors.categories(Category.DESERT).negate();
    private static final Predicate<BiomeSelectionContext> ALL_BIOMES_WITH_DEFAULT_SURFACE =
        ALL_BIOMES.and(BiomeSelectors.includeByKey(
            BuiltinRegistries.BIOME.getEntries()
                .stream()
                .filter(e -> 
                    e.getValue().getGenerationSettings().getSurfaceConfig().equals(SurfaceBuilder.GRASS_CONFIG) && 
                    e.getValue().getGenerationSettings().getSurfaceBuilder().get().surfaceBuilder.equals(SurfaceBuilder.DEFAULT) &&
                    !ExcludedBiomes.getExcludedBiomes().contains(e.getKey()))
                .map(e -> e.getKey())
                .collect(Collectors.toList())
        ));
    private static final Predicate<BiomeSelectionContext> BEACH_BIOMES = BiomeSelectors.includeByKey(
        RegistryKey.of(Registry.BIOME_KEY, new Identifier("beach")),
        RegistryKey.of(Registry.BIOME_KEY, new Identifier("snowy_beach"))
    );
    
    private static final Identifier REPLACE_BEACH_SURFACE = new Identifier(ClassicBeaches.MOD_ID, "replace_beach_surface");
    private static final Identifier REPLACE_DEFAULT_SURFACE = new Identifier(ClassicBeaches.MOD_ID, "replace_default_surface");
    
    private static final Identifier REMOVE_DISK_SAND = new Identifier(ClassicBeaches.MOD_ID, "remove_disk_sand");
    private static final Identifier REMOVE_DISK_GRAV = new Identifier(ClassicBeaches.MOD_ID, "remove_disk_gravel");
    private static final Identifier REMOVE_DISK_CLAY = new Identifier(ClassicBeaches.MOD_ID, "remove_disk_clay");
    
    private static final Identifier ORE_CLAY = new Identifier(ClassicBeaches.MOD_ID, "ore_clay");
    
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
        BiomeModifications.create(REPLACE_BEACH_SURFACE).add(ModificationPhase.POST_PROCESSING, BEACH_BIOMES, context -> {
            context.getGenerationSettings().setBuiltInSurfaceBuilder(SurfaceBuilders.CONF_BEACH_SURFACE);
        });
        
        BiomeModifications.create(REPLACE_DEFAULT_SURFACE).add(ModificationPhase.POST_PROCESSING, ALL_BIOMES_WITH_DEFAULT_SURFACE, context -> {
            context.getGenerationSettings().setBuiltInSurfaceBuilder(SurfaceBuilders.CONF_BEACH_SURFACE); 
        });
    }
    
    public static void addClayOre() {
        BiomeModifications.addFeature(
            ALL_BIOMES_BUT_DESERT, 
            GenerationStep.Feature.UNDERGROUND_ORES, 
            RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN, ORE_CLAY)
        );
    }
}
