package red.aviora.redmc.placeholders.utils;

import org.bukkit.entity.Player;
import red.aviora.redmc.placeholders.models.PlaceholderContext;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderRegistry {

	private final Map<String, PlaceholderContext> placeholders = new HashMap<>();

	public void set(String key, PlaceholderContext context) {
		placeholders.put(key, context);
	}

	public String get(String key, Player player) {
		PlaceholderContext context = placeholders.get(key);
		return context != null ? context.getValue(player) : null;
	}
}
