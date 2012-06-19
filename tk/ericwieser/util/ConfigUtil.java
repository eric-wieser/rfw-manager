package tk.ericwieser.util;

import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

public class ConfigUtil {
	@SuppressWarnings("unchecked")
    public static Map<String, Object> getMap(Map<String, Object> m, String key) {
		return (Map<String, Object>) m.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMap(Object o, String key) {
		return getMap((Map<String, Object>) o, key);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> getMap(ConfigurationSection o, String key) {
		return (Map<String, Object>) o.get(key);
	}
	
	@SuppressWarnings("unchecked")
    public static Map<String, Object> asMap(Object o) {
		return (Map<String, Object>) o;
	}
}
