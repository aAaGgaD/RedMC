package com.aviora.redmc.permissions.commands.group;

import com.aviora.redmc.api.utils.ApiUtils;
import com.aviora.redmc.api.utils.LocaleManager;
import com.aviora.redmc.permissions.PermissionsPlugin;
import com.aviora.redmc.permissions.models.Group;
import com.aviora.redmc.permissions.models.PermissionEntry;
import com.aviora.redmc.permissions.utils.PermissionManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.MessageComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class GroupPermissionsReadCommand implements Command<CommandSourceStack> {

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		CommandSender sender = context.getSource().getSender();

		LocaleManager localeManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getLocaleManager();
		PermissionManager permissionManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

		String groupId = StringArgumentType.getString(context, "<group>").toLowerCase();

		if (!permissionManager.getGroups().containsKey(groupId)) {
			throw new SimpleCommandExceptionType(
				MessageComponentSerializer.message().serialize(
					ApiUtils.formatText(
						localeManager.getMessage(sender, "group-not-found"),
						"%name%", groupId,
						"%prefix%", localeManager.getMessage(sender, "prefix")
					)
				)
			).create();
		}

		Group group = permissionManager.getGroups().get(groupId);

		List<PermissionEntry> perms = group.getPermissions();

		if (perms.isEmpty()) {
			throw new SimpleCommandExceptionType(
				MessageComponentSerializer.message().serialize(
					ApiUtils.formatText(
						localeManager.getMessage(sender, "group-perms-empty"),
						"%group%", groupId,
						"%prefix%", localeManager.getMessage(sender, "prefix")
					)
				)
			).create();
		}

		ApiUtils.sendCommandSenderMessageArgs(sender,
			localeManager.getMessage(sender, "group-perms-title"),
			"%group%", groupId,
			"%prefix%", localeManager.getMessage(sender, "prefix")
		);

		for (int i = 0; i < perms.size(); i++) {
			PermissionEntry entry = perms.get(i);
			ApiUtils.sendCommandSenderMessageArgs(sender,
				localeManager.getMessage(sender, "group-perms-item"),
				"%index%", String.valueOf(i),
				"%perm%", entry.getName(),
				"%allowed%", String.valueOf(entry.isAllowed()),
				"%weight%", String.valueOf(entry.getWeight()),
				"%prefix%", localeManager.getMessage(sender, "prefix")
			);
		}

		return Command.SINGLE_SUCCESS;
	}
}
