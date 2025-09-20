package com.aviora.redmc.permissions.listeners;

import com.aviora.redmc.permissions.PermissionsPlugin;
import com.aviora.redmc.permissions.utils.PermissionManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public record PlayerJoinListener() implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        PermissionManager manager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

        var player = event.getPlayer();
        manager.getOrCreatePlayer(player.getUniqueId(), player.getName());
        manager.applyPermissions(player);
        manager.saveAll();
    }
}
