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

public class GroupPermsListCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        LocaleManager localeManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getLocaleManager();
        PermissionManager permissionManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

        if (args.length < 1) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "missing-arguments"),
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        String groupId = args[0].toLowerCase();
        Group group = permissionManager.getGroups().get(groupId);

        if (group == null) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "group-not-found"),
                    "%group%", groupId,
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        List<PermissionEntry> perms = group.getPermissions();

        if (perms.isEmpty()) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "group-perms-empty"),
                    "%group%", groupId,
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        ApiUtils.sendCommandSenderMessageArgs(sender,
                localeManager.getMessage(sender, "group-perms-title"),
                "%group%", groupId,
                "%prefix%", localeManager.getMessage(sender, "prefix"));

        for (int i = 0; i < perms.size(); i++) {
            PermissionEntry entry = perms.get(i);
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "group-perms-item"),
                    "%index%", String.valueOf(i),
                    "%perm%", entry.getName(),
                    "%allowed%", String.valueOf(entry.isAllowed()),
                    "%weight%", String.valueOf(entry.getWeight()),
                    "%prefix%", localeManager.getMessage(sender, "prefix")
            );
        }

        return true;
    }
}
