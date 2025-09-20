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

import java.util.List;

public class GroupPermsRemoveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        LocaleManager localeManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getLocaleManager();
        PermissionManager permissionManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

        if (args.length < 2) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "missing-arguments"),
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        String groupId = args[0].toLowerCase();
        String indexStr = args[1];

        Group group = permissionManager.getGroups().get(groupId);
        if (group == null) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "group-not-found"),
                    "%group%", groupId,
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        int index;
        try {
            index = Integer.parseInt(indexStr);
        } catch (NumberFormatException e) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "invalid-int"),
                    "%input%", indexStr,
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        List<PermissionEntry> perms = group.getPermissions();
        if (index < 0 || index >= perms.size()) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "group-perms-delete-error"),
                    "%index%", indexStr,
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        String removed = perms.get(index).getName();
        group.removePermission(index);

        permissionManager.reloadAll();

        ApiUtils.sendCommandSenderMessageArgs(sender,
                localeManager.getMessage(sender, "permission-removed"),
                "%perm%", removed,
                "%prefix%", localeManager.getMessage(sender, "prefix"));

        return true;
    }
}
