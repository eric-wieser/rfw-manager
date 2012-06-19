package tk.ericwieser.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class CuboidRegion implements ConfigurationSerializable  {
	World  world;
	Vector min;
	Vector max;
	
	public CuboidRegion(World w, Vector a, Vector b) {
		world = w;
		set(a, b);
	}
	
	public CuboidRegion(Selection r) {
		world = r.getWorld();
		min = r.getMinimumPoint().toVector();
		max = r.getMaximumPoint().toVector().add(new Vector(1, 1, 1));
	}

	public void set(Vector a, Vector b) {
		min = new Vector(
		        Math.min(a.getX(), b.getX()),
		        Math.min(a.getY(), b.getY()),
		        Math.min(a.getZ(), b.getZ())
		      );
		max = new Vector(
		        Math.max(a.getX(), b.getX()),
		        Math.max(a.getY(), b.getY()),
		        Math.max(a.getZ(), b.getZ())
		      );
	}

	private boolean between(double lower, double x, double upper) {
		return lower <= x && upper > x;
	}

	public boolean contains(Location l) {
		return l.getWorld() == world &&
				between(min.getX(), l.getX(), max.getX()) &&
		        between(min.getY(), l.getY(), max.getY()) &&
		        between(min.getZ(), l.getZ(), max.getZ());
	}

	public Vector getMax() {
		return max;
	}

	public void setMax(Vector max) {
		set(max, min);
	}

	public World getWorld() {
		return world;
	}

	public Vector getMin() {
		return min;
	}

	public void setMin(Vector min) {
		set(max, min);
	}
	
	public CuboidRegion adjustX(double positive, double negative) {
		min.setX(min.getX() - negative);
		max.setX(max.getX() + positive);
		return this;
	}
	public CuboidRegion adjustY(double positive, double negative) {
		min.setY(min.getY() - negative);
		max.setY(max.getY() + positive);
		return this;
	}
	public CuboidRegion adjustZ(double positive, double negative) {
		min.setZ(min.getZ() - negative);
		max.setZ(max.getZ() + positive);
		return this;
	}
	
	public CuboidRegion clone() {
		return new CuboidRegion(world, min, max);
	}

	@Override
    public Map<String, Object> serialize() {
	    Map<String, Object> m = new HashMap<String, Object>();
	    m.put("world", world.getName());
	    m.put("min", min.serialize());
	    m.put("max", max.serialize());
	    return m;	    
    }
	
	@SuppressWarnings("unchecked")
    public static CuboidRegion deserialize(Map<String, Object> m) {
		MemorySection min = (MemorySection) m.get("min");
		MemorySection max = (MemorySection) m.get("max");
		String worldName = (String) m.get("world");
		Bukkit.getLogger().info("["+worldName+']');
		return new CuboidRegion(
				Bukkit.getServer().getWorld(worldName),
				Vector.deserialize(min.getValues(false)),
				Vector.deserialize(max.getValues(false))
		);
	}
}
