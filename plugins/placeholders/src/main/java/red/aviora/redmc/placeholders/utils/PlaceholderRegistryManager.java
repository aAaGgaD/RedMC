package red.aviora.redmc.placeholders.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderRegistryManager {

	private final List<Entry> registries = new ArrayList<>();

	public void addRegistry(PlaceholderRegistry registry, int priority) {
		registries.add(new Entry(priority, registry));
		registries.sort((a, b) -> Integer.compare(b.priority, a.priority));
	}

	public String resolve(String key, Player player) {
		for (Entry entry : registries) {
			String value = entry.registry.get(key, player);
			if (value != null) {
				return value;
			}
		}
		return null;
	}

	private static class Entry {
		int priority;
		PlaceholderRegistry registry;

		Entry(int priority, PlaceholderRegistry registry) {
			this.priority = priority;
			this.registry = registry;
		}
	}
}
