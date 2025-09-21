package red.aviora.redmc.placeholders.registries;

import org.bukkit.Bukkit;
import red.aviora.redmc.placeholders.PlaceholdersPlugin;
import red.aviora.redmc.placeholders.utils.PlaceholderRegistry;

public class GeneralRegistryGenerator implements RegistryGenerator {

	public PlaceholderRegistry generate() {
		PlaceholderRegistry base = new PlaceholderRegistry();

		base.set("CurrentTime", p -> p == null ? PlaceholdersPlugin.NULL_VALUE : p.getName());
		base.set("PlayerName", p -> p == null ? PlaceholdersPlugin.NULL_VALUE : p.getName());
		base.set("PlayerUuid", p -> p == null ? PlaceholdersPlugin.NULL_VALUE : p.getUniqueId().toString());
		base.set("PlayerWorld", p -> p == null ? PlaceholdersPlugin.NULL_VALUE : p.getWorld().getName());
		base.set("PlayerHealth", p -> p == null ? PlaceholdersPlugin.NULL_VALUE : String.valueOf((int) p.getHealth()));
		base.set("CurrentServerOnline", p -> String.valueOf(Bukkit.getOnlinePlayers().size()));
		base.set("ServerOnlineMaximum", p -> String.valueOf(Bukkit.getMaxPlayers()));

		return base;
	}
}
