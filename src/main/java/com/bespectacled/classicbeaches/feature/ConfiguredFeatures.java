package com.bespectacled.classicbeaches.feature;

import com.bespectacled.classicbeaches.ClassicBeaches;

import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

public class ConfiguredFeatures {
    public static final Identifier ORE_CLAY_ID = ClassicBeaches.createId("ore_clay");
    
    public static final ConfiguredFeature<?, ?> ORE_CLAY = ((Feature.ORE.configure(new OreFeatureConfig(new BlockMatchRuleTest(Blocks.SAND), Blocks.CLAY.getDefaultState(), 33)).uniformRange(YOffset.fixed(0), YOffset.fixed(127))).spreadHorizontally().repeat(1));
    
    public static void register() {
        register(ORE_CLAY_ID, ORE_CLAY);
    }
    
    private static <F extends FeatureConfig> ConfiguredFeature<F, ?> register(Identifier id, ConfiguredFeature<F, ?> feature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, id, feature);
    }
    
    
}
