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
import java.util.UUID;

public class PlayerPermsListCommand implements CommandExecutor {

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

        String playerName = args[0];
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        UUID uuid = offlinePlayer.getUniqueId();
        PlayerData data = permissionManager.getPlayers().get(uuid);

        if (data == null) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "player-not-found"),
                    "%player%", playerName,
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        List<PermissionEntry> perms = data.getPermissions();

        if (perms.isEmpty()) {
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "player-perms-is-empty"),
                    "%player%", playerName,
                    "%prefix%", localeManager.getMessage(sender, "prefix"));
            return true;
        }

        ApiUtils.sendCommandSenderMessageArgs(sender,
                localeManager.getMessage(sender, "player-perms-title"),
                "%player%", playerName,
                "%prefix%", localeManager.getMessage(sender, "prefix"));

        for (int i = 0; i < perms.size(); i++) {
            PermissionEntry entry = perms.get(i);
            ApiUtils.sendCommandSenderMessageArgs(sender,
                    localeManager.getMessage(sender, "player-perms-item"),
                    "%index%", String.valueOf(i),
                    "%perm%", entry.getName(),
                    "%allowed%", String.valueOf(entry.isAllowed()),
                    "%weight%", String.valueOf(entry.getWeight())
            );
        }

        return true;
    }
}
