package red.aviora.redmc.placeholders.models;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface PlaceholderContext {

	String getValue(Player player);
}
