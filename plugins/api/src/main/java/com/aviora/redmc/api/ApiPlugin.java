package com.aviora.redmc.api;

import com.aviora.redmc.api.commands.HelloCommand;
import com.aviora.redmc.api.commands.ReloadCommand;
import com.aviora.redmc.api.utils.ConfigManager;
import com.aviora.redmc.api.utils.LocaleManager;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ApiPlugin extends JavaPlugin implements Listener {
    
    private ConfigManager configManager;
    private LocaleManager localeManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this, "config.yml");
        localeManager = new LocaleManager(configManager, "en_US", "en_US", "ru_RU");
        
        getServer().getPluginManager().registerEvents(this, this);

        getCommand("hello").setExecutor(new HelloCommand(configManager, localeManager));
        getCommand("reloadapi").setExecutor(new ReloadCommand(configManager, localeManager));
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LocaleManager getLocaleManager() {
        return localeManager;
    }
}
