package com.aviora.redmc.permissions;

import com.aviora.redmc.api.utils.ConfigManager;
import com.aviora.redmc.api.utils.LocaleManager;
import com.aviora.redmc.permissions.commands.PermCommand;
import com.aviora.redmc.permissions.listeners.PlayerJoinListener;
import com.aviora.redmc.permissions.tabs.PermTabCompleter;
import com.aviora.redmc.permissions.utils.PermissionManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class PermissionsPlugin extends JavaPlugin {
    
    private ConfigManager configManager;
    private LocaleManager localeManager;
    private PermissionManager permissionManager;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this, "config.yml", "groups.yml", "players.yml");
        localeManager = new LocaleManager(configManager, "en_US", "en_US", "ru_RU");

        permissionManager = new PermissionManager();
        permissionManager.loadAll();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        Objects.requireNonNull(getCommand("perm")).setExecutor(new PermCommand());
        Objects.requireNonNull(getCommand("perm")).setTabCompleter(new PermTabCompleter());
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public LocaleManager getLocaleManager() {
        return localeManager;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }
}
