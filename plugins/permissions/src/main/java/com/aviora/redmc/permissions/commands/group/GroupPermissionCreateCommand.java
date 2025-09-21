package com.aviora.redmc.permissions.commands.group;

import com.aviora.redmc.api.utils.ApiUtils;
import com.aviora.redmc.api.utils.LocaleManager;
import com.aviora.redmc.permissions.PermissionsPlugin;
import com.aviora.redmc.permissions.models.Group;
import com.aviora.redmc.permissions.models.PermissionEntry;
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
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class GroupPermissionCreateCommand implements Command<CommandSourceStack> {

	@Override
	public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
		CommandSender sender = context.getSource().getSender();

		LocaleManager localeManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getLocaleManager();
		PermissionManager permissionManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

		String groupId = StringArgumentType.getString(context, "<group>").toLowerCase();
		String permission = StringArgumentType.getString(context, "<permission>").toLowerCase();
		int weight = IntegerArgumentType.getInteger(context, "<weight>");
		boolean allowed = BoolArgumentType.getBool(context, "<allowed>");

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

		PermissionEntry entry = new PermissionEntry(permission, weight, allowed);
		group.addPermission(entry);

		permissionManager.reloadAll();

		ApiUtils.sendCommandSenderMessageArgs(sender,
			localeManager.getMessage(sender, "permission-added"),
			"%perm%", permission,
			"%prefix%", localeManager.getMessage(sender, "prefix")
		);

		return Command.SINGLE_SUCCESS;
	}
}
