package red.aviora.redmc.placeholders.utils;

import org.bukkit.entity.Player;
import red.aviora.redmc.placeholders.PlaceholdersPlugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public record PlaceholderParser(PlaceholderRegistryManager manager, Pattern pattern) {

	public String parseString(String text, Player player) {
		Matcher matcher = pattern.matcher(text);
		StringBuilder result = new StringBuilder();

		while (matcher.find()) {
			String key = matcher.group(1);
			String replacement = manager.resolve(key, player);
			if (replacement == null) {
				replacement = PlaceholdersPlugin.NULL_VALUE;
			}
			matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
		}
		matcher.appendTail(result);

		return result.toString();
	}
}
