package com.bespectacled.classicbeaches.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "classicbeaches")
public class ClassicBeachesConfig implements ConfigData {
    @ConfigEntry.Category(value = "beach")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean generateBeaches = true;
    
    @ConfigEntry.Category(value = "beach")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean generateBeachClay = true;
    
    @ConfigEntry.Category(value = "beach")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean generateHighGravelBeaches = false;
    
    @ConfigEntry.Category(value = "beach")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public double sandBeachThreshold = 0D;
    
    @ConfigEntry.Category(value = "beach")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public double gravelBeachThreshold = 3D;
    
    @ConfigEntry.Category(value = "disk")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean generateSandDisks = false;
    
    @ConfigEntry.Category(value = "disk")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean generateGravelDisks = false;
    
    @ConfigEntry.Category(value = "disk")
    @ConfigEntry.Gui.Tooltip(count = 1)
    public boolean generateClayDisks = false;
    
}
