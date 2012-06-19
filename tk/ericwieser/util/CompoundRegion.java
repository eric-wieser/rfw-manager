package tk.ericwieser.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

@SuppressWarnings("serial")
public class CompoundRegion extends LinkedList<Region> implements Region {
	public boolean contains(Location l) {
		for(Region r : this)
			if(r.contains(l))
				return true;
		return false;
	}
	public boolean contains(Vector v) {
		for(Region r : this)
			if(r.contains(v))
				return true;
		return false;
	}
	public boolean contains(BlockVector v) {
		for(Region r : this)
			if(r.contains(v))
				return true;
		return false;
	}
	
	public List<Map<String, Object>> serialize() {
		List<Map<String, Object>> result = new ArrayList<>();
		for(Region r : this)
			result.add(((ConfigurationSerializable) r).serialize());
		return result;
	}
	
	public static CompoundRegion deserialize(List<Map<String, Object>> data) {
		CompoundRegion regions = new CompoundRegion();
		for(Map<String, Object> regiondata : data) {
			regions.add(CuboidRegion.deserialize(regiondata));
		}
		return regions;
	}
}
