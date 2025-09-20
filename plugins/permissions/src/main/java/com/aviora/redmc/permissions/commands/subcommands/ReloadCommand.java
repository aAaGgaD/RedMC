package com.aviora.redmc.permissions.commands.subcommands;

import com.aviora.redmc.api.utils.ApiUtils;
import com.aviora.redmc.api.utils.ConfigManager;
import com.aviora.redmc.api.utils.LocaleManager;
import com.aviora.redmc.permissions.PermissionsPlugin;
import com.aviora.redmc.permissions.utils.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        ConfigManager configManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getConfigManager();
        LocaleManager localeManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getLocaleManager();
        PermissionManager permissionManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

        if (args.length < 1) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "missing-arguments"),
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        String target = args[0].toLowerCase();

        switch (target) {
            case "config" -> {
                configManager.reload();
                ApiUtils.sendCommandSenderMessageArgs(sender,
                        localeManager.getMessage(sender, "reload-config"),
                        "%prefix%", localeManager.getMessage(sender, "prefix"));
            }
            case "db" -> {
                permissionManager.reloadAll();
                ApiUtils.sendCommandSenderMessageArgs(sender,
                        localeManager.getMessage(sender, "reload-db"),
                        "%prefix%", localeManager.getMessage(sender, "prefix"));
            }
            case "all" -> {
                configManager.reload();
                permissionManager.reloadAll();
                ApiUtils.sendCommandSenderMessageArgs(sender,
                        localeManager.getMessage(sender, "reload-all"),
                        "%prefix%", localeManager.getMessage(sender, "prefix"));
            }
            default -> ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "invalid-args"),
                    "%usage%", "reload <config|db|all>",
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
        }

        return true;
    }
}
