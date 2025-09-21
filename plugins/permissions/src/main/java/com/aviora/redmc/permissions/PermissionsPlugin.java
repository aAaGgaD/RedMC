package com.aviora.redmc.permissions;

import com.aviora.redmc.api.utils.ConfigManager;
import com.aviora.redmc.api.utils.LocaleManager;
import com.aviora.redmc.permissions.listeners.PlayerJoinListener;
import com.aviora.redmc.permissions.utils.PermissionManager;
import org.bukkit.plugin.java.JavaPlugin;

public class PermissionsPlugin extends JavaPlugin {

	private static PermissionsPlugin instance;

    private ConfigManager configManager;
    private LocaleManager localeManager;
    private PermissionManager permissionManager;

    @Override
    public void onEnable() {
	    instance = this;

        configManager = new ConfigManager(getInstance(), "config.yml", "groups.yml", "players.yml");
        localeManager = new LocaleManager(configManager, "en_US", "en_US", "ru_RU");

        permissionManager = new PermissionManager();
        permissionManager.loadAll();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), getInstance());
    }

	public static PermissionsPlugin getInstance() {
		return instance;
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
