package red.aviora.redmc.permissions.utils;

import red.aviora.redmc.api.utils.ApiUtils;
import red.aviora.redmc.api.utils.LocaleManager;
import red.aviora.redmc.permissions.PermissionsPlugin;
import red.aviora.redmc.permissions.models.Group;
import red.aviora.redmc.permissions.models.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PermissionManager {

	private final PermissionDataStorage storage;
	private final Map<String, Group> groups = new HashMap<>();
	private final Map<UUID, PlayerData> players = new HashMap<>();
	private final Map<UUID, PermissionAttachment> attachments = new HashMap<>();

	public PermissionManager() {
		storage = new PermissionDataStorage();
	}

	public void loadAll() {
		groups.clear();
		groups.putAll(storage.loadGroups());

		players.clear();
		players.putAll( storage.loadPlayers());

		ensureDefaultGroup();
	}

	public void saveAll() {
		storage.saveGroups(groups);
		storage.savePlayers(players);
	}

	public void reloadAll() {
		saveAll();
		loadAll();
		reapplyAll();
	}

	public Map<String, Group> getGroups() {
		return groups;
	}

	public Map<UUID, PlayerData> getPlayers() {
		return players;
	}

	public PlayerData getOrCreatePlayer(UUID uuid, String name) {
		if (players.containsKey(uuid)) {
			PlayerData data = players.get(uuid);
			data.setName(name);
			return data;
		}

		PlayerData newData = new PlayerData(uuid, name);
		String defaultGroup = JavaPlugin.getPlugin(PermissionsPlugin.class).getConfigManager().getString("config.yml", "default-group");

		if (!groups.containsKey(defaultGroup)) {
			Group group = new Group(defaultGroup, "Default");
			groups.put(defaultGroup, group);
		}

		newData.addGroup(defaultGroup);
		players.put(uuid, newData);
		return newData;
	}

	public void applyPermissions(Player player) {
		UUID uuid = player.getUniqueId();
		PlayerData data = getOrCreatePlayer(uuid, player.getName());
		Map<String, Boolean> resolved = PermissionResolver.resolve(data, groups);

		PermissionAttachment attachment = attachments.get(uuid);
		if (attachment != null) {
			attachment.remove();
		}

		attachment = player.addAttachment(JavaPlugin.getPlugin(PermissionsPlugin.class));
		attachments.put(uuid, attachment);

		for (Map.Entry<String, Boolean> entry : resolved.entrySet()) {
			attachment.setPermission(entry.getKey(), entry.getValue());
		}
	}

	private void ensureDefaultGroup() {
		String defaultId = JavaPlugin.getPlugin(PermissionsPlugin.class).getConfigManager().getString("config.yml", "default-group");
		if (!groups.containsKey(defaultId)) {
			Group group = new Group(defaultId, "Default");
			groups.put(defaultId, group);
			storage.saveGroups(groups);

			LocaleManager localeManager = JavaPlugin.getPlugin(PermissionsPlugin.class).getLocaleManager();
			ConsoleCommandSender console = Bukkit.getConsoleSender();
			console.sendMessage(
					ApiUtils.formatText(
						localeManager.getMessage(console, "ensure-default-group"),
						"%prefix%", localeManager.getMessage(console, "prefix")
					)
			);
		}
	}

	public void reapplyAll() {
		for (Player player : Bukkit.getOnlinePlayers()) {
			applyPermissions(player);
			player.updateCommands();
		}
	}
}
