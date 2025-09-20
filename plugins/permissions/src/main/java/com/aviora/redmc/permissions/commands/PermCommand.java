package com.aviora.redmc.permissions.commands;

import com.aviora.redmc.permissions.commands.handlers.SubCommandHandler;
import com.aviora.redmc.permissions.commands.subcommands.ReloadCommand;
import com.aviora.redmc.permissions.commands.subcommands.group.*;
import com.aviora.redmc.permissions.commands.subcommands.player.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class PermCommand implements CommandExecutor {

    private final SubCommandHandler handler;

    public PermCommand() {
        handler = new SubCommandHandler();

        // === Group Commands ===
        handler.register("group list", new GroupListCommand(), "redmc.perm.group.list");
        handler.register("group add", new GroupAddCommand(), "redmc.perm.group.add");
        handler.register("group remove", new GroupRemoveCommand(), "redmc.perm.group.remove");

        // Edit group name: /perm group edit <id> name <newName>
        handler.register("group edit * name", new GroupEditNameCommand(), "redmc.perm.group.edit.name");

        // Group permission management
        handler.register("group edit * perms list", new GroupPermsListCommand(), "redmc.perm.group.perms.list");
        handler.register("group edit * perms add", new GroupPermsAddCommand(), "redmc.perm.group.perms.add");
        handler.register("group edit * perms remove", new GroupPermsRemoveCommand(), "redmc.perm.group.perms.remove");

        // === Player Permission Commands ===
        handler.register("player * perms list", new PlayerPermsListCommand(), "redmc.perm.player.perms.list");
        handler.register("player * perms add", new PlayerPermsAddCommand(), "redmc.perm.player.perms.add");
        handler.register("player * perms remove", new PlayerPermsRemoveCommand(), "redmc.perm.player.perms.remove");

        // Player group management
        handler.register("player * groups list", new PlayerGroupsListCommand(), "redmc.perm.player.groups.list");
        handler.register("player * groups add", new PlayerGroupsAddCommand(), "redmc.perm.player.groups.add");
        handler.register("player * groups remove", new PlayerGroupsRemoveCommand(), "redmc.perm.player.groups.remove");

        // === Reload Configuration/Database ===
        handler.register(
                "reload",
                new ReloadCommand(),
                "redmc.perm.reload"
        );
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return handler.dispatch(sender, command, label, args);
    }
}
