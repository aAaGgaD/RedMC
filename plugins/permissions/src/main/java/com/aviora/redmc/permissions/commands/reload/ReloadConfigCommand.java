package com.aviora.redmc.permissions.commands.reload;

import com.aviora.redmc.api.utils.ApiUtils;
import com.aviora.redmc.api.utils.ConfigManager;
import com.aviora.redmc.api.utils.LocaleManager;
import com.aviora.redmc.permissions.PermissionsPlugin;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadConfigCommand implements Command<CommandSourceStack> {

	public int run(CommandContext<CommandSourceStack> context) {
		CommandSender sender = context.getSource().getSender();

		ConfigManager configManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getConfigManager();
		LocaleManager localeManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getLocaleManager();

		configManager.reload();

		ApiUtils.sendCommandSenderMessageArgs(sender,
			localeManager.getMessage(sender, "reload-config"),
			"%prefix%", localeManager.getMessage(sender, "prefix")
		);

		return Command.SINGLE_SUCCESS;
	}
}
