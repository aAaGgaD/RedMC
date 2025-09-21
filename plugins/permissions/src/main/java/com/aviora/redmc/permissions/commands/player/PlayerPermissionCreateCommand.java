package com.aviora.redmc.permissions.commands.player;

import com.aviora.redmc.api.utils.ApiUtils;
import com.aviora.redmc.api.utils.LocaleManager;
import com.aviora.redmc.permissions.PermissionsPlugin;
import com.aviora.redmc.permissions.models.PermissionEntry;
import com.aviora.redmc.permissions.models.PlayerData;
import com.aviora.redmc.permissions.utils.PermissionManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
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

public class PlayerPermissionCreateCommand implements Command<CommandSourceStack> {

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		CommandSender sender = context.getSource().getSender();

		LocaleManager localeManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getLocaleManager();
		PermissionManager permissionManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

		String playerName = StringArgumentType.getString(context, "<player>").toLowerCase();
		String permission = StringArgumentType.getString(context, "<permission>").toLowerCase();
		int weight = IntegerArgumentType.getInteger(context, "<weight>");
		boolean allowed = BoolArgumentType.getBool(context, "<allowed>");

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

		PermissionEntry entry = new PermissionEntry(permission, weight, allowed);
		data.addPermission(entry);

		permissionManager.reloadAll();

		ApiUtils.sendCommandSenderMessageArgs(sender,
			localeManager.getMessage(sender, "player-perm-added"),
			"%player%", playerName,
			"%perm%", permission,
			"%prefix%", localeManager.getMessage(sender, "prefix")
		);

		return Command.SINGLE_SUCCESS;
	}
}
