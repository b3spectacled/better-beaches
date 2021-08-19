package com.bespectacled.classicbeaches.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.bespectacled.classicbeaches.surfacebuilder.SurfaceBuilders;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.math.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.DefaultBiomeCreator;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.feature.ConfiguredStructureFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;

@Mixin(DefaultBiomeCreator.class)
public class MixinDefaultBiomeCreator {
	private static int getSkyColor(float temperature) {
        float g = temperature;
        g /= 3.0f;
        g = MathHelper.clamp(g, -1.0f, 1.0f);
        return MathHelper.hsvToRgb(0.62222224f - g * 0.05f, 0.5f + g * 0.1f, 1.0f);
    }
	
	@Overwrite
	public static Biome createBeach(float temp, float rain, int waterColor, boolean cold, boolean mountains) {
        SpawnSettings.Builder lv = new SpawnSettings.Builder();
        if (!mountains && !cold) {
            lv.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.TURTLE, 5, 2, 5));
        }
        DefaultBiomeFeatures.addBatsAndMonsters(lv);
        GenerationSettings.Builder lv2 = new GenerationSettings.Builder().surfaceBuilder(mountains ? ConfiguredSurfaceBuilders.STONE_SHORE : SurfaceBuilders.CONF_BEACH_SURFACE_GRASS);
        if (mountains) {
            DefaultBiomeFeatures.addDefaultUndergroundStructures(lv2);
        } else {
            lv2.structureFeature(ConfiguredStructureFeatures.MINESHAFT);
            lv2.structureFeature(ConfiguredStructureFeatures.BURIED_TREASURE);
            lv2.structureFeature(ConfiguredStructureFeatures.SHIPWRECK_BEACHED);
        }
        lv2.structureFeature(mountains ? ConfiguredStructureFeatures.RUINED_PORTAL_MOUNTAIN : ConfiguredStructureFeatures.RUINED_PORTAL);
        DefaultBiomeFeatures.addLandCarvers(lv2);
        DefaultBiomeFeatures.addAmethystGeodes(lv2);
        DefaultBiomeFeatures.addDungeons(lv2);
        DefaultBiomeFeatures.addMineables(lv2);
        DefaultBiomeFeatures.method_37868(lv2);
        DefaultBiomeFeatures.addDefaultDisks(lv2);
        DefaultBiomeFeatures.addDefaultFlowers(lv2);
        DefaultBiomeFeatures.addDefaultGrass(lv2);
        DefaultBiomeFeatures.addDefaultMushrooms(lv2);
        DefaultBiomeFeatures.addDefaultVegetation(lv2);
        DefaultBiomeFeatures.addSprings(lv2);
        DefaultBiomeFeatures.addFrozenTopLayer(lv2);
        
        Biome.Builder lv3 = new Biome.Builder().precipitation(cold ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN).category(mountains ? Biome.Category.NONE : Biome.Category.BEACH);
        
        return lv3.temperature(temp).downfall(rain).effects(new BiomeEffects.Builder().waterColor(waterColor).waterFogColor(329011).fogColor(12638463).skyColor(getSkyColor(temp)).moodSound(BiomeMoodSound.CAVE).build()).spawnSettings(lv.build()).generationSettings(lv2.build()).build();
    }
}
