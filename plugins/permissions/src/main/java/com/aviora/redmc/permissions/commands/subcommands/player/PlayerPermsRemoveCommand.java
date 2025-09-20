package com.aviora.redmc.permissions.commands.subcommands.player;

import com.aviora.redmc.api.utils.ApiUtils;
import com.aviora.redmc.api.utils.LocaleManager;
import com.aviora.redmc.permissions.PermissionsPlugin;
import com.aviora.redmc.permissions.models.PermissionEntry;
import com.aviora.redmc.permissions.models.PlayerData;
import com.aviora.redmc.permissions.utils.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class PlayerPermsRemoveCommand implements CommandExecutor {

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
        String indexStr = args[1];

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        PlayerData data = permissionManager.getPlayers().get(offlinePlayer.getUniqueId());

        if (data == null) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "player-not-found"),
                    "%player%", playerName,
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

        List<PermissionEntry> perms = data.getPermissions();
        if (index < 0 || index >= perms.size()) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "player-perms-delete-error"),
                    "%index%", indexStr,
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        String removed = perms.get(index).getName();
        data.removePermission(index);

        permissionManager.reloadAll();

        ApiUtils.sendCommandSenderMessageArgs(sender,
                localeManager.getMessage(sender, "player-perm-removed"),
                "%player%", playerName,
                "%perm%", removed,
                "%prefix%", localeManager.getMessage(sender, "prefix"));

        return true;
    }
}
