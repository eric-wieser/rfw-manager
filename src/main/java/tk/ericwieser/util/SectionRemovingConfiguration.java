package tk.ericwieser.util;

import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * A class that prevents ConfigurationSections being generated, keeping the data
 * as a series of nested list and maps.
 * 
 * Without this, the implementation of ConfigurationSerializable.serialize
 * cannot work, since sometimes it recieves a map of maps, and other times, a
 * map of ConfigurationSections. This kills the java
 */
public class SectionRemovingConfiguration extends YamlConfiguration {
	@Override
	protected void convertMapsToSections(Map<?, ?> input, ConfigurationSection section) {
		for (Map.Entry<?, ?> entry : input.entrySet()) {
			String key = entry.getKey().toString();
			Object value = entry.getValue();

			if (value instanceof Map) {
				convertMapsToSections((Map<?, ?>) value, section.createSection(key));
			}
			//Was an else clause in the base class
			section.set(key, value);
		}
	}
}