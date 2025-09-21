package com.aviora.redmc.permissions.commands.group;

import com.aviora.redmc.api.utils.ApiUtils;
import com.aviora.redmc.api.utils.LocaleManager;
import com.aviora.redmc.permissions.PermissionsPlugin;
import com.aviora.redmc.permissions.models.Group;
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

public class GroupPermissionDeleteCommand implements Command<CommandSourceStack> {

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		CommandSender sender = context.getSource().getSender();

		LocaleManager localeManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getLocaleManager();
		PermissionManager permissionManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

		String groupId = StringArgumentType.getString(context, "<group>").toLowerCase();
		String permission = StringArgumentType.getString(context, "<permission>").toLowerCase();

		if (!permissionManager.getGroups().containsKey(groupId)) {
			throw new SimpleCommandExceptionType(
				MessageComponentSerializer.message().serialize(
					ApiUtils.formatText(
						localeManager.getMessage(sender, "group-not-found"),
						"%group%", groupId,
						"%prefix%", localeManager.getMessage(sender, "prefix")
					)
				)
			).create();
		}

		Group group = permissionManager.getGroups().get(groupId);

		boolean exists = group.getPermissions().stream()
			.anyMatch(p -> p.getName().equalsIgnoreCase(permission));

		if (exists) {
			group.removePermission(permission);

			permissionManager.reloadAll();

			ApiUtils.sendCommandSenderMessageArgs(sender,
				localeManager.getMessage(sender, "permission-removed"),
				"%perm%", permission,
				"%prefix%", localeManager.getMessage(sender, "prefix"));
		} else {
			throw new SimpleCommandExceptionType(
				MessageComponentSerializer.message().serialize(
					ApiUtils.formatText(
						localeManager.getMessage(sender, "group-perms-delete-error"),
						"%index%", permission,
						"%prefix%", localeManager.getMessage(sender, "prefix")
					)
				)
			).create();
		}

		return Command.SINGLE_SUCCESS;
	}
}
