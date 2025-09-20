package com.aviora.redmc.permissions.commands.handlers;

import com.aviora.redmc.api.utils.ApiUtils;
import com.aviora.redmc.api.utils.LocaleManager;
import com.aviora.redmc.permissions.PermissionsPlugin;
import com.aviora.redmc.permissions.commands.annotations.SubCommandMeta;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class SubCommandHandler {

    private final Map<List<String>, SubCommandMeta> subCommands = new HashMap<>();

    public void register(String path, CommandExecutor executor, String permission) {
        List<String> pathParts = Arrays.stream(path.split(" ")).map(String::toLowerCase).toList();
        subCommands.put(pathParts, new SubCommandMeta(pathParts, "", permission, true, executor));
    }

    public boolean dispatch(CommandSender sender, Command command, String label, String[] args) {
        LocaleManager localeManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getLocaleManager();

        List<String> inputPath = Arrays.stream(args).map(String::toLowerCase).toList();

        for (Map.Entry<List<String>, SubCommandMeta> entry : subCommands.entrySet()) {
            List<String> registeredPath = entry.getKey();

            if (inputPath.size() < registeredPath.size()) continue;

            List<String> wildcardArgs = new ArrayList<>();
            boolean match = true;

            for (int i = 0; i < registeredPath.size(); i++) {
                String expected = registeredPath.get(i);
                String actual = inputPath.get(i);

                if (expected.equals("*")) {
                    wildcardArgs.add(actual);
                    continue;
                }

                if (!expected.equalsIgnoreCase(actual)) {
                    match = false;
                    break;
                }
            }

            if (!match) continue;

            SubCommandMeta meta = entry.getValue();

            if (!meta.getPermission().isEmpty() && !sender.hasPermission(meta.getPermission())) {
                ApiUtils.sendCommandSenderMessageArgs(sender,
                        localeManager.getMessage(sender, "no-permission"),
                        "%prefix%", localeManager.getMessage(sender, "prefix"));
                return true;
            }

            if (meta.getPermission().isEmpty() && meta.isOpOnly() && !sender.isOp()) {
                ApiUtils.sendCommandSenderMessageArgs(sender,
                        localeManager.getMessage(sender, "no-permission"),
                        "%prefix%", localeManager.getMessage(sender, "prefix"));
                return true;
            }

            String[] remaining = Arrays.copyOfRange(args, registeredPath.size(), args.length);
            String[] finalArgs = new String[wildcardArgs.size() + remaining.length];

            int index = 0;
            for (String s : wildcardArgs) finalArgs[index++] = s;
            for (String s : remaining) finalArgs[index++] = s;

            return meta.getExecutor().onCommand(sender, command, label, finalArgs);
        }

        ApiUtils.sendCommandSenderMessageArgs(sender,
                localeManager.getMessage(sender, "unknown-subcommand"),
                "%subcommand%", String.join(" ", args),
                "%prefix%", localeManager.getMessage(sender, "prefix"));

        return true;
    }
}
