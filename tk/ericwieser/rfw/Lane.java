package tk.ericwieser.rfw;

import static tk.ericwieser.util.ConfigUtil.getMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.Vector;

import tk.ericwieser.util.BlockData;
import tk.ericwieser.util.CompoundRegion;
import tk.ericwieser.util.ConfigUtil;
import tk.ericwieser.util.Nameable;
import tk.ericwieser.util.Region;

public class Lane extends GameComponent implements ConfigurationSerializable, Nameable {
	private CompoundRegion zone = new CompoundRegion();
	private List<WoolPlacement> wools;
	
	private String name;
	
	@Override
	public String getName() { return name; }
	@Override
	public void setName(String name) { this.name = name; }

	/** Set the game of the lane. Used for deserialization */
	@Override public void setGame(Game game) {
		super.setGame(game);
		for (Region r : zone) r.setWorld(game.getWorld());
		for (WoolPlacement w : wools) w.location.setWorld(game.getWorld());
	}

	public CompoundRegion getZone() { return zone; }
	public List<WoolPlacement> getWools() { return wools; }

	public Lane(Game g, String name, CompoundRegion zone) {
		this(g, name, zone, new ArrayList<WoolPlacement>());
	}

	public Lane(Game g, String name, CompoundRegion zone, List<WoolPlacement> wools) {
		super(g);
		this.zone = zone;
		this.name = name;
		this.wools = wools;
	}

	public static class WoolPlacement implements ConfigurationSerializable, Nameable {
		public Location location;
		public BlockData block;
		public String name;
		
		@Override
		public String getName() { return name; }
		@Override
		public void setName(String name) { this.name = name; }
		
		public WoolPlacement(Location l, BlockData b, String n) {
			location = l;
			block = b;
			name = n;
		}
		
		public static WoolPlacement deserialize(Map<String, Object> m) {
			// Get block data
			String blockdata = (String) m.get("block");
			if (blockdata == null)
				return null;
			BlockData b = BlockData.fromString(blockdata);
			
			// Rebuild the location
			Vector v = Vector.deserialize(getMap(m, "at"));
			Location l = new Location(
				null,
				v.getX(),
				v.getY(),
				v.getZ()
			);
			return new WoolPlacement(l, b, null);
		}
		
		@Override
		public Map<String, Object> serialize() {
			Map<String, Object> m = new HashMap<String, Object>();
			m.put("at", location.toVector().serialize());
			m.put("block", block.toString());
			return m;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Lane deserialize(Map<String, Object> m) {
		// Build the wool list
		List<WoolPlacement> wools = new ArrayList<>();
		Map<String, Map<String, Object>> woolsData = (Map<String, Map<String, Object>>) m.get("wools");
		wools = ConfigUtil.deserialize(woolsData, WoolPlacement.class);
		
//		for (Entry<String, Object> woolData : woolsData.entrySet()) {
//			WoolPlacement w = WoolPlacement.deserialize(asMap(woolData
//				.getValue()));
//			if (w == null)
//				return null;
//
//			w.name = woolData.getKey();
//			wools.add(w);
//		}

		Object oZoneData = m.get("zones");
		List<Map<String, Object>> zoneData = (List<Map<String, Object>>) oZoneData;

		return new Lane(null, null, CompoundRegion.deserialize(zoneData), wools);
	}

	@Override
	public Map<String, Object> serialize() {
		Map<String, Object> wool = ConfigUtil.serialize(wools);
		Map<String, Object> m = new HashMap<String, Object>();
		// m.put("world", world.getName());
		m.put("wools", wool);
		m.put("zones", zone.serialize());
		return m;
	}
}
