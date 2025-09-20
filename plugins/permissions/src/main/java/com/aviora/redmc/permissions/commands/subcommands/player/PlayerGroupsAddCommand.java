package com.aviora.redmc.permissions.commands.subcommands.player;

import com.aviora.redmc.api.utils.ApiUtils;
import com.aviora.redmc.api.utils.LocaleManager;
import com.aviora.redmc.permissions.PermissionsPlugin;
import com.aviora.redmc.permissions.models.Group;
import com.aviora.redmc.permissions.models.PlayerData;
import com.aviora.redmc.permissions.utils.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PlayerGroupsAddCommand implements CommandExecutor {

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

        String playerName = args[0];
        String groupId = args[1].toLowerCase();

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        PlayerData data = permissionManager.getPlayers().get(offlinePlayer.getUniqueId());

        if (data == null) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "player-not-found"),
                    "%player%", playerName,
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        Group group = permissionManager.getGroups().get(groupId);
        if (group == null) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "group-not-found"),
                    "%group%", groupId,
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        if (data.getGroupIds().contains(groupId)) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "already-exists"),
                    "%name%", groupId,
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        data.addGroup(groupId);

        permissionManager.reloadAll();

        ApiUtils.sendCommandSenderMessageArgs(sender,
                localeManager.getMessage(sender, "player-group-added"),
                "%player%", playerName,
                "%group%", groupId,
                "%prefix%", localeManager.getMessage(sender, "prefix")
        );

        return true;
    }
}
