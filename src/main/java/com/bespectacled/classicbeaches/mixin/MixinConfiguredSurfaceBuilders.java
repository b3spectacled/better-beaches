package com.bespectacled.classicbeaches.mixin;

import org.spongepowered.asm.mixin.Mixin;

import com.bespectacled.classicbeaches.surfacebuilder.SurfaceBuilders;

import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilder.ConfiguredSurfaceBuilders;
import net.minecraft.world.gen.surfacebuilder.TernarySurfaceConfig;

@Mixin(ConfiguredSurfaceBuilders.class)
public class MixinConfiguredSurfaceBuilders {
    
    private static final ConfiguredSurfaceBuilder<TernarySurfaceConfig> GRASS = SurfaceBuilders.CONF_BEACH_SURFACE;
}
