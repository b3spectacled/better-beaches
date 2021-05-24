package com.bespectacled.classicbeaches.config;

import com.bespectacled.classicbeaches.ClassicBeaches;

import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.gui.screen.Screen;

public class ClassicBeachesModMenu implements ModMenuApi {
    @Override
    public String getModId() {
        return ClassicBeaches.MOD_ID;
    }

    @Override
    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return screen -> AutoConfig.getConfigScreen(ClassicBeachesConfig.class, screen).get();
    }
}
