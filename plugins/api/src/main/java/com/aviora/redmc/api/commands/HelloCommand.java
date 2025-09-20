package com.aviora.redmc.api.commands;

import com.aviora.redmc.api.ApiPlugin;
import com.aviora.redmc.api.utils.ApiUtils;
import com.aviora.redmc.api.utils.ConfigManager;
import com.aviora.redmc.api.utils.LocaleManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public record HelloCommand(ConfigManager configManager, LocaleManager localeManager) implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            ApiUtils.sendCommandSenderMessage(sender, "Only for players.");
            return true;
        }

        ApiUtils.sendPlayerMessageArgs(player, "👋 Hello, %player%! Checking Folia...", "%player%", player.getName());

        Location target = player.getWorld().getSpawnLocation();

        Bukkit.getRegionScheduler().run(JavaPlugin.getPlugin(ApiPlugin.class), target, task -> {
            player.teleportAsync(target);
            ApiUtils.sendPlayerMessage(player, "✅ Complete!");
        });

        return true;
    }
}
