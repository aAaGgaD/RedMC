package com.aviora.redmc.permissions.tabs;

import com.aviora.redmc.permissions.PermissionsPlugin;
import com.aviora.redmc.permissions.models.Group;
import com.aviora.redmc.permissions.models.PlayerData;
import com.aviora.redmc.permissions.utils.PermissionManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class PermTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        PermissionManager manager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

        // /perm <tab>
        if (args.length == 1) {
            return match(args[0], List.of("group", "player", "reload"));
        }

        // /perm group ...
        if (args[0].equalsIgnoreCase("group")) {

            // /perm group <list|add|remove|edit>
            if (args.length == 2) return match(args[1], List.of("list", "add", "remove", "edit"));

            // /perm group remove <groupId>
            if (args[1].equalsIgnoreCase("remove") && args.length == 3) {
                return match(args[2], manager.getGroups().keySet());
            }

            // /perm group add <groupId>
            if (args[1].equalsIgnoreCase("add") && args.length == 3) {
                return matchOrPlaceholder(args[2], "<groupId>", manager.getGroups().keySet());
            }

            // /perm group edit <groupId> ...
            if (args[1].equalsIgnoreCase("edit")) {
                if (args.length == 3) return match(args[2], manager.getGroups().keySet());
                if (args.length == 4) return match(args[3], List.of("name", "perms"));

                // /perm group edit <groupId> name <newName>
                if (args[3].equalsIgnoreCase("name") && args.length == 5) {
                    return matchOrPlaceholder(args[4], "<newName>", List.of("<newName>"));
                }

                // /perm group edit <groupId> perms ...
                if (args[3].equalsIgnoreCase("perms")) {
                    if (args.length == 5) return match(args[4], List.of("list", "add", "remove"));

                    // /perm group edit <groupId> perms add <permission> <weight> <true|false>
                    if (args[4].equalsIgnoreCase("add")) {
                        if (args.length == 6)
                            return matchOrPlaceholder(args[5], "<permission>", getAllRegisteredPermissions());
                        if (args.length == 7) return matchOrPlaceholder(args[6], "<weight>", List.of("<weight>"));
                        if (args.length == 8) return match(args[7], List.of("true", "false"));
                    }

                    // /perm group edit <groupId> perms remove <index>
                    if (args[4].equalsIgnoreCase("remove") && args.length == 6) {
                        String groupId = args[2].toLowerCase();
                        List<String> indexes = getIndexesForGroup(groupId);
                        return matchOrPlaceholder(args[5], "<index>", indexes);
                    }
                }
            }
        }

        // /perm player ...
        if (args[0].equalsIgnoreCase("player")) {

            // /perm player <name>
            if (args.length == 2) {
                return match(args[1], Bukkit.getOnlinePlayers().stream().map(Player::getName).toList());
            }

            // /perm player <name> <perms|groups>
            if (args.length == 3) {
                return match(args[2], List.of("perms", "groups"));
            }

            // /perm player <name> perms ...
            if (args[2].equalsIgnoreCase("perms")) {
                if (args.length == 4) return match(args[3], List.of("list", "add", "remove"));

                // /perm player <name> perms add <permission> <weight> <true|false>
                if (args[3].equalsIgnoreCase("add")) {
                    if (args.length == 5)
                        return matchOrPlaceholder(args[4], "<permission>", getAllRegisteredPermissions());
                    if (args.length == 6) return matchOrPlaceholder(args[5], "<weight>", List.of("<weight>"));
                    if (args.length == 7) return match(args[6], List.of("true", "false"));
                }

                // /perm player <name> perms remove <index>
                if (args[3].equalsIgnoreCase("remove") && args.length == 5) {
                    String playerName = args[1];
                    List<String> indexes = getIndexesForPlayer(playerName);
                    return matchOrPlaceholder(args[4], "<index>", indexes);
                }
            }

            // /perm player <name> groups ...
            if (args[2].equalsIgnoreCase("groups")) {
                if (args.length == 4) return match(args[3], List.of("list", "add", "remove"));

                if ((args[3].equalsIgnoreCase("add") || args[3].equalsIgnoreCase("remove")) && args.length == 5) {
                    return match(args[4], manager.getGroups().keySet());
                }
            }
        }

        // /perm reload <config|db|all>
        if (args[0].equalsIgnoreCase("reload") && args.length == 2) {
            return match(args[1], List.of("config", "db", "all"));
        }

        // fallback: no suggestion
        return List.of();
    }

    private List<String> match(String input, Iterable<String> options) {
        List<String> matches = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase().startsWith(input.toLowerCase())) {
                matches.add(option);
            }
        }
        return matches;
    }

    private List<String> matchOrPlaceholder(String input, String placeholder, Iterable<String> realOptions) {
        List<String> options = new ArrayList<>();
        realOptions.forEach(options::add);

        if (options.isEmpty()) {
            return List.of(placeholder);
        }

        List<String> result = match(input, options);
        if (result.isEmpty() && placeholder.toLowerCase().startsWith(input.toLowerCase())) {
            result.add(placeholder);
        }
        return result;
    }

    private List<String> getIndexesForGroup(String groupId) {
        PermissionManager manager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

        Group group = manager.getGroups().get(groupId);
        if (group == null) return List.of();
        int size = group.getPermissions().size();
        List<String> indexes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            indexes.add(String.valueOf(i));
        }
        return indexes;
    }

    private List<String> getIndexesForPlayer(String playerName) {
        PermissionManager manager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

        Player player = Bukkit.getPlayerExact(playerName);
        if (player == null) return List.of();
        PlayerData data = manager.getPlayers().get(player.getUniqueId());
        if (data == null) return List.of();
        int size = data.getPermissions().size();
        List<String> indexes = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            indexes.add(String.valueOf(i));
        }
        return indexes;
    }

    private List<String> getAllRegisteredPermissions() {
        return Bukkit.getPluginManager()
                .getPermissions()
                .stream()
                .map(Permission::getName)
                .sorted()
                .toList();
    }
}
