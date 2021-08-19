package com.bespectacled.classicbeaches.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.bespectacled.classicbeaches.ClassicBeaches;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;

@Mixin(DefaultBiomeFeatures.class)
public class MixinDefaultBiomeFeatures {
	@Overwrite
	public static void addDefaultDisks(GenerationSettings.Builder builder) {
		if (ClassicBeaches.CONFIG.generateSandDisks) builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.DISK_SAND);
		if (ClassicBeaches.CONFIG.generateClayDisks) builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.DISK_CLAY);
		if (ClassicBeaches.CONFIG.generateGravelDisks) builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, ConfiguredFeatures.DISK_GRAVEL);
    }
	
	@Inject(
		method = "addMineables",
		at = @At("HEAD")
	)
	private static void injectClayOre(GenerationSettings.Builder builder, CallbackInfo info) {
		if (ClassicBeaches.CONFIG.generateBeachClay) builder.feature(GenerationStep.Feature.UNDERGROUND_ORES, com.bespectacled.classicbeaches.feature.ConfiguredFeatures.ORE_CLAY);
	}
	
}
