package com.aviora.redmc.api.commands;

import com.aviora.redmc.api.utils.ApiUtils;
import com.aviora.redmc.api.utils.ConfigManager;
import com.aviora.redmc.api.utils.LocaleManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public record ReloadCommand(ConfigManager configManager, LocaleManager localeManager) implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        configManager.reload();

        String prefix = localeManager.getMessage(sender, "prefix");
        String reloadLocale = localeManager.getMessage(sender, "reload");

        ApiUtils.sendCommandSenderMessageArgs(
                sender,
                "%message%",
                "%message%", reloadLocale,
                "%prefix%", prefix
        );

        return true;
    }
}
