package tk.ericwieser.rfw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import static tk.ericwieser.util.ConfigUtil.*;

import tk.ericwieser.util.BlockData;
import tk.ericwieser.util.CompoundRegion;
import tk.ericwieser.util.Region;

public class Lane implements ConfigurationSerializable, Listener {
	private CompoundRegion      zone = new CompoundRegion();
	private List<WoolPlacement> wools;
	private String name;
	private Game game;
	
	public String getName() {
	    return name;
    }
	
	public void setName(String name) {
	    this.name = name;
    }
	public Game getGame() {
	    return game;
    }
	public void setGame(Game game) {
	    this.game = game;
	    for(Region r: zone) {
	    	r.setWorld(game.getWorld());
	    }
	    for(WoolPlacement w: wools) {
	    	w.location.setWorld(game.getWorld());
	    }
    }
	
	public CompoundRegion getZone() {
	    return zone;
    }
	
	public Lane(Game g, String name, CompoundRegion zone) {
	    this(g, name, zone, new ArrayList<WoolPlacement>());
    }
	public Lane(Game g, String name, CompoundRegion zone, List<WoolPlacement> wools) {
		this.game = g;
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
		Map<String, Object> woolsData = getMap(m, "wools");

		for(Entry<String, Object> woolData : woolsData.entrySet()) {
			WoolPlacement w = WoolPlacement.deserialize(asMap(woolData.getValue()));
			if(w == null) return null;
			
			w.name = woolData.getKey();
			wools.add(w);
		}
		
		Object oZoneData = m.get("zones");
		Bukkit.getLogger().info("" + oZoneData);
		
		List<Map<String, Object>> zoneData = (List<Map<String, Object>>) oZoneData;
		
		return new Lane(null, null, CompoundRegion.deserialize(zoneData), wools);
    
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
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerMoved(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		//not interested
		if(!game.hasPlayer(p)) return;
		
		//player still in arena
		boolean wasIn = getZone().contains(e.getFrom());
		boolean isIn = getZone().contains(e.getTo());
		
		if (wasIn && !isIn) {
			game.getPlugin().getLogger().info(p.getName() + " left lane \"" + getName() + "\"");
			p.sendMessage("You left lane \"" + getName() + "\"");
		}
		else if (!wasIn && isIn) {
			game.getPlugin().getLogger().info(p.getName() + " entered lane \"" + getName() + "\"");
			p.sendMessage("You entered lane \"" + getName() + "\"");
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void blockPlaced(BlockPlaceEvent e) {
		Block b = e.getBlock();
		for(WoolPlacement w : wools) {
			if(w.block.matches(b)) {
				game.sendMessage("The " + w.name + " was placed on the " + name);
				return;
			}
		}
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void blockDestroyed(BlockBreakEvent e) {
		Block b = e.getBlock();
		for(WoolPlacement w : wools) {
			if(w.block.matches(b)) {
				game.sendMessage("The " + w.name + " was removed on the " + name);
				return;
			}
		}
	}
}
