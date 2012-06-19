package tk.ericwieser.rfw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Wolf;
import org.bukkit.material.Wool;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.blocks.ClothColor;

import tk.ericwieser.util.BlockData;
import tk.ericwieser.util.CompoundRegion;
import tk.ericwieser.util.CuboidRegion;

public class Lane implements ConfigurationSerializable{
	private CompoundRegion      zone = new CompoundRegion();
	private List<WoolPlacement> wools;
	private String name;
	
	public String getName() {
	    return name;
    }
	
	public void setName(String name) {
	    this.name = name;
    }
	
	public CompoundRegion getZone() {
	    return zone;
    }

	private boolean blockMatches(Block block, BlockState state) {
		return (state.getLocation().equals(block.getLocation()) &&
		        state.getType().equals(block.getType()) && state.getRawData() == block
		        .getData());

	}

	public Lane(String name, CompoundRegion zone) {
	    this(name, zone, new ArrayList<WoolPlacement>());
    }
	public Lane(String name, CompoundRegion zone, List<WoolPlacement> wools) {
	    this.zone = zone;
	    this.name = name;
	    this.wools = wools;
    }
	
	static class WoolPlacement implements ConfigurationSerializable{
		public Location location;
		public BlockData block;
		public String name;

		public WoolPlacement(Location l, BlockData b, String n) {
			location = l;
			block = b;
			name = n;
		}

		@SuppressWarnings("unchecked")
		public static WoolPlacement deserialize(Map<String, Object> m) {
			// Get block data
			String blockdata = (String) m.get("block");
			if(blockdata == null) return null;
			BlockData b = BlockData.fromString(blockdata);
			
			// Rebuild the location
			MemorySection locdata = (MemorySection) m.get("at");
			Location l = new Location(
			        null,
			        locdata.getDouble("x"),
			        locdata.getDouble("y"),
			        locdata.getDouble("z")
			);
			return new WoolPlacement(l, b, null);
		}
		
		@Override
	    public Map<String, Object> serialize() {
		    Map<String, Object> m = new HashMap<String, Object>();
		    // m.put("world", world.getName());
		    m.put("at", location.toVector().serialize());
		    m.put("block", block.toString());
		    return m;
	    }
		
	}

	@SuppressWarnings("unchecked")
    public static Lane deserialize(Map<String, Object> m) {
		//Build the wool list
		List<WoolPlacement> wools = new ArrayList<>();
		MemorySection woolsData = (MemorySection) m.get("wools");

		for(String name : woolsData.getKeys(false)) {
			Bukkit.getLogger().info(name);
		}

		for(String name : woolsData.getKeys(false)) {
			ConfigurationSection woolData = woolsData.getConfigurationSection(name);

			WoolPlacement w = WoolPlacement.deserialize(woolData.getValues(false));
			if(w == null) return null;
			
			w.name = name;
			wools.add(w);
		}

		Object oZoneData = m.get("zones");
		Bukkit.getLogger().info("" + oZoneData);
		
		List<Map<String, Object>> zoneData = (List<Map<String, Object>>) oZoneData;
		
		return new Lane(null, CompoundRegion.deserialize(zoneData), wools);
    }
	
	@Override
    public Map<String, Object> serialize() {
		Map<String, Object> wool = new HashMap<>();
		for(WoolPlacement w : wools) {
			wool.put(w.name, w);
		}
		
	    Map<String, Object> m = new HashMap<String, Object>();
	    // m.put("world", world.getName());
	    m.put("wools", wool);
	    m.put("zones", zone.serialize());
	    return m;
    }
}
