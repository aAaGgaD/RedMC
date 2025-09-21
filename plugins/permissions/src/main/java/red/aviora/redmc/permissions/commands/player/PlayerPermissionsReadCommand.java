package red.aviora.redmc.permissions.commands.player;

import red.aviora.redmc.api.utils.ApiUtils;
import red.aviora.redmc.api.utils.LocaleManager;
import red.aviora.redmc.permissions.PermissionsPlugin;
import red.aviora.redmc.permissions.models.PermissionEntry;
import red.aviora.redmc.permissions.models.PlayerData;
import red.aviora.redmc.permissions.utils.PermissionManager;
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
						localeManager.getMessage(sender, "not-found"),
						"%name%", playerName,
						"%prefix%", localeManager.getMessage(sender, "prefix")
					)
				)
			).create();
		}

		List<PermissionEntry> perms = data.getPermissions();

		if (perms.isEmpty()) {
			ApiUtils.sendCommandSenderMessageArgs(sender,
				localeManager.getMessage(sender, "empty-list"),
				"%prefix%", localeManager.getMessage(sender, "prefix")
			);
		} else {
			ApiUtils.sendCommandSenderMessageArgs(sender,
				localeManager.getMessage(sender, "permissions-list-header"),
				"%prefix%", localeManager.getMessage(sender, "prefix")
			);

			for (PermissionEntry entry : perms) {
				ApiUtils.sendCommandSenderMessageArgs(sender,
					localeManager.getMessage(sender, "permissions-list-item"),
					"%permission%", entry.getName(),
					"%allowed%", String.valueOf(entry.isAllowed()),
					"%weight%", String.valueOf(entry.getWeight())
				);
			}
		}

		return Command.SINGLE_SUCCESS;
	}
}
