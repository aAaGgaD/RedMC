package com.aviora.redmc.permissions.commands.annotations;

import org.bukkit.command.CommandExecutor;

import java.util.List;

public record SubCommandMeta(List<String> path, String description, String permission, boolean opOnly,
                             CommandExecutor executor) {

    public List<String> getPath() {
        return path;
    }

    public String getDescription() {
        return description;
    }

    public String getPermission() {
        return permission;
    }

    public boolean isOpOnly() {
        return opOnly;
    }

    public CommandExecutor getExecutor() {
        return executor;
    }

    public int getWildcardCount() {
        int count = 0;
        for (String part : path) {
            if (part.equals("*")) count++;
        }
        return count;
    }
}
