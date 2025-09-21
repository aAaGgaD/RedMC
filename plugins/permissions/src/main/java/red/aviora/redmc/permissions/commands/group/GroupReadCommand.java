package red.aviora.redmc.permissions.commands.group;

import red.aviora.redmc.api.utils.ApiUtils;
import red.aviora.redmc.api.utils.LocaleManager;
import red.aviora.redmc.permissions.PermissionsPlugin;
import red.aviora.redmc.permissions.models.Group;
import red.aviora.redmc.permissions.utils.PermissionManager;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.context.CommandContext;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class GroupReadCommand implements Command<CommandSourceStack> {

	@Override
	public int run(CommandContext<CommandSourceStack> context) {
		CommandSender sender = context.getSource().getSender();

		LocaleManager localeManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getLocaleManager();
		PermissionManager permissionManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getPermissionManager();

		if (permissionManager.getGroups().isEmpty()) {
			ApiUtils.sendCommandSenderMessageArgs(sender,
				localeManager.getMessage(sender, "empty-list"),
				"%prefix%", localeManager.getMessage(sender, "prefix")
			);
		} else {
			ApiUtils.sendCommandSenderMessageArgs(sender,
				localeManager.getMessage(sender, "group-list-header"),
				"%prefix%", localeManager.getMessage(sender, "prefix")
			);

			for (Group group : permissionManager.getGroups().values()) {
				ApiUtils.sendCommandSenderMessageArgs(sender,
					localeManager.getMessage(sender, "group-list-item"),
					"%id%", group.getId(),
					"%name%", group.getDisplayName(),
					"%prefix%", localeManager.getMessage(sender, "prefix")
				);
			}
		}

		return Command.SINGLE_SUCCESS;
	}
}