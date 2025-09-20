package com.aviora.redmc.permissions.commands.subcommands.group;

import com.aviora.redmc.api.utils.ApiUtils;
import com.aviora.redmc.api.utils.LocaleManager;
import com.aviora.redmc.permissions.PermissionsPlugin;
import com.aviora.redmc.permissions.models.Group;
import com.aviora.redmc.permissions.utils.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class GroupListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        LocaleManager localeManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getLocaleManager();
        PermissionManager permissionManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

        if (permissionManager.getGroups().isEmpty()) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "group-list-empty"),
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        ApiUtils.sendCommandSenderMessageArgs(sender,
                localeManager.getMessage(sender, "group-list-title"),
                "%prefix%", localeManager.getMessage(sender, "prefix"));

        for (Group group : permissionManager.getGroups().values()) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "group-list-item"),
                    "%id%", group.getId(),
                    "%name%", group.getDisplayName(),
                    "%prefix%", localeManager.getMessage(sender, "prefix")
            );
        }

        return true;
    }
}