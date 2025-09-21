package com.aviora.redmc.permissions.commands.player;

import com.aviora.redmc.api.utils.ApiUtils;
import com.aviora.redmc.api.utils.LocaleManager;
import com.aviora.redmc.permissions.PermissionsPlugin;
import com.aviora.redmc.permissions.models.PermissionEntry;
import com.aviora.redmc.permissions.models.PlayerData;
import com.aviora.redmc.permissions.utils.PermissionManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class PlayerPermissionsReadCommand implements Command<CommandSourceStack> {

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		CommandSender sender = context.getSource().getSender();

		LocaleManager localeManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getLocaleManager();
		PermissionManager permissionManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

		String playerName = StringArgumentType.getString(context, "<player>").toLowerCase();

		OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
		PlayerData data = permissionManager.getPlayers().get(offlinePlayer.getUniqueId());

		if (data == null) {
			throw new SimpleCommandExceptionType(
				MessageComponentSerializer.message().serialize(
					ApiUtils.formatText(
						localeManager.getMessage(sender, "player-not-found"),
						"%player%", playerName,
						"%prefix%", localeManager.getMessage(sender, "prefix")
					)
				)
			).create();
		}

		List<PermissionEntry> perms = data.getPermissions();

		if (perms.isEmpty()) {
			ApiUtils.sendCommandSenderMessageArgs(sender,
				localeManager.getMessage(sender, "player-perms-is-empty"),
				"%player%", playerName,
				"%prefix%", localeManager.getMessage(sender, "prefix")
			);
		} else {
			ApiUtils.sendCommandSenderMessageArgs(sender,
				localeManager.getMessage(sender, "player-perms-title"),
				"%player%", playerName,
				"%prefix%", localeManager.getMessage(sender, "prefix")
			);

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
		}

		return Command.SINGLE_SUCCESS;
	}
}
