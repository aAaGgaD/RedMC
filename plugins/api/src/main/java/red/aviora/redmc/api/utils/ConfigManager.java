package red.aviora.redmc.api.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class ConfigManager {

	private final JavaPlugin plugin;
	private final List<String> resourceFiles = new ArrayList<>();
	private final Map<String, YamlConfiguration> configs = new HashMap<>();

	public ConfigManager(JavaPlugin plugin, String... resources) {
		this.plugin = plugin;
		registerResources(resources);
		load();
	}

	public JavaPlugin getCurrentPlugin() {
		return plugin;
	}

	public void registerResources(String... resources) {
		resourceFiles.addAll(Arrays.asList(resources));
	}

	public void load() {
		saveDefaults();
		loadAll();
	}

	public void reload() {
		configs.clear();
		load();
		ApiUtils.logArgs(
			"Configuration reloaded for plugin %plugin%.",
			"%plugin%", plugin.getName()
		);
	}

	public YamlConfiguration getConfig(String fileName) {
		return configs.get(fileName);
	}

	public String getString(String fileName, String path) {
		YamlConfiguration config = getConfig(fileName);
		return config.getString(path);
	}

	public String getString(String fileName, String path, String def) {
		YamlConfiguration config = getConfig(fileName);
		return (config != null) ? config.getString(path, def) : def;
	}

	public int getInt(String fileName, String path) {
		YamlConfiguration config = getConfig(fileName);
		return config.getInt(path);
	}

	public int getInt(String fileName, String path, int def) {
		YamlConfiguration config = getConfig(fileName);
		return (config != null) ? config.getInt(path, def) : def;
	}

	public double getDouble(String fileName, String path) {
		YamlConfiguration config = getConfig(fileName);
		return config.getDouble(path);
	}

	public double getDouble(String fileName, String path, double def) {
		YamlConfiguration config = getConfig(fileName);
		return (config != null) ? config.getDouble(path, def) : def;
	}

	public List<?> getList(String fileName, String path) {
		YamlConfiguration config = getConfig(fileName);
		return config.getList(path);
	}

	public List<?> getList(String fileName, String path, List<?> def) {
		YamlConfiguration config = getConfig(fileName);
		return (config != null) ? config.getList(path, def) : def;
	}

	public boolean getBoolean(String fileName, String path) {
		YamlConfiguration config = getConfig(fileName);
		return config.getBoolean(path);
	}

	public boolean getBoolean(String fileName, String path, boolean def) {
		YamlConfiguration config = getConfig(fileName);
		return (config != null) ? config.getBoolean(path, def) : def;
	}

	private void saveDefaults() {
		for (String resource : resourceFiles) {
			if (plugin.getResource(resource) != null) {
				File outFile = new File(plugin.getDataFolder(), resource);
				if (!outFile.exists()) {
					plugin.saveResource(resource, false);
					ApiUtils.logArgs(
						"Saved default resource \"%resource%\" for plugin %plugin%.",
						"%resource%", resource,
						"%plugin%", plugin.getName()
					);
				}
			} else {
				ApiUtils.logArgs(
					"Resource not found in JAR \"%resource%\" for plugin %plugin%.",
					"%resource%", resource,
					"%plugin%", plugin.getName()
				);
			}
		}
	}

	private void loadAll() {
		for (String resource : resourceFiles) {
			File file = new File(plugin.getDataFolder(), resource);
			if (file.exists()) {
				YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
				configs.put(resource, config);
			} else {
				ApiUtils.logArgs(
					"Config file \"%resource%\" missing for plugin %plugin%.",
					"%resource%", resource,
					"%plugin%", plugin.getName()
				);
			}
		}
	}
}
