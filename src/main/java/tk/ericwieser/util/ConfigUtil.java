package tk.ericwieser.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

/**
 * Utilities for deserialization, to hide away repetitive casting.
 * 
 */
public class ConfigUtil {
	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> getMap(Map<String, Object> m, String key) {
		return (Map<String, T>) m.get(key);
	}

	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> getMap(Object o, String key) {
		return getMap(o, key);
	}

	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> getMap(ConfigurationSection o, String key) {
		return (Map<String, T>) o.get(key);
	}


	@SuppressWarnings("unchecked")
	public static <T> Map<String, T> asMap(Object o) {
		return (Map<String, T>) o;
	}

	/**
	 * Serialize a list of named objects into name=>map pairs
	 */
	//Craziest method signature I've ever used
	public static <T extends Nameable & ConfigurationSerializable>
	              Map<String, Object> serialize(Iterable<T> list) {
		Map<String, Object> map = new HashMap<>();
		for(T n : list) {
			map.put(n.getName(), n.serialize());
		}
		return map;
	}

	/**
	 * deserialize a map of name=>map pairs into a list of named objects
	 */
	//Craziest method signature I've ever used
	@SuppressWarnings("unchecked")
	public static <T extends Nameable & ConfigurationSerializable>
	              List<T> deserialize(Map<String, Map<String, Object>> map, Class<T> clazz) {
		
		List<T> deserialized = new ArrayList<>();
		for(Entry<String, Map<String, Object>> e : map.entrySet()) {
			T value = (T) ConfigurationSerialization.deserializeObject(e.getValue(), clazz);
			if(value == null)
				return null;
			value.setName(e.getKey());
			deserialized.add(value);
		}
		return deserialized;
	}
}
