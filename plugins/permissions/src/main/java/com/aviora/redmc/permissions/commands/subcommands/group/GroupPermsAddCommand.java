package com.aviora.redmc.permissions.commands.subcommands.group;

import com.aviora.redmc.api.utils.ApiUtils;
import com.aviora.redmc.api.utils.LocaleManager;
import com.aviora.redmc.permissions.PermissionsPlugin;
import com.aviora.redmc.permissions.models.Group;
import com.aviora.redmc.permissions.models.PermissionEntry;
import com.aviora.redmc.permissions.utils.PermissionManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class GroupPermsAddCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        LocaleManager localeManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getLocaleManager();
        PermissionManager permissionManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

        if (args.length < 4) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "missing-arguments"),
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        String groupId = args[0].toLowerCase();
        String permName = args[1];
        String weightStr = args[2];
        String allowedStr = args[3];

        Group group = permissionManager.getGroups().get(groupId);
        if (group == null) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "group-not-found"),
                    "%group%", groupId,
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        int weight;
        try {
            weight = Integer.parseInt(weightStr);
        } catch (NumberFormatException e) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "invalid-int"),
                    "%input%", weightStr,
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        boolean allowed;
        if (allowedStr.equalsIgnoreCase("true")) {
            allowed = true;
        } else if (allowedStr.equalsIgnoreCase("false")) {
            allowed = false;
        } else {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "invalid-boolean"),
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        PermissionEntry entry = new PermissionEntry(permName, weight, allowed);
        group.addPermission(entry);

        permissionManager.reloadAll();

        ApiUtils.sendCommandSenderMessageArgs(sender,
                localeManager.getMessage(sender, "permission-added"),
                "%perm%", permName,
                "%prefix%", localeManager.getMessage(sender, "prefix"));

        return true;
    }
}
