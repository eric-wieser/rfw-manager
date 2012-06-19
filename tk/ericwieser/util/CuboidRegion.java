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
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import static tk.ericwieser.util.ConfigUtil.*;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class CuboidRegion implements ConfigurationSerializable, Region  {
	static { ConfigurationSerialization.registerClass(CuboidRegion.class); }
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

	private boolean betweenLower(double lower, double x, double upper) {
		return lower <= x && x < upper;
	}
	private boolean betweenUpper(double lower, double x, double upper) {
		return lower < x && x <= upper;
	}

	/* (non-Javadoc)
     * @see tk.ericwieser.util.Region#contains(org.bukkit.util.BlockVector)
     */
	@Override
    public boolean contains(BlockVector v) {
		return  betweenLower(min.getX(), v.getBlockX(), max.getX()) &&
		        betweenLower(min.getY(), v.getBlockY(), max.getY()) &&
		        betweenLower(min.getZ(), v.getBlockZ(), max.getZ()) &&
		        betweenUpper(min.getX(), v.getBlockX() + 1, max.getX()) &&
		        betweenUpper(min.getY(), v.getBlockY() + 1, max.getY()) &&
		        betweenUpper(min.getZ(), v.getBlockZ() + 1, max.getZ());
	}

	/* (non-Javadoc)
     * @see tk.ericwieser.util.Region#contains(org.bukkit.util.Vector)
     */
	@Override
    public boolean contains(Vector v) {
		return  betweenLower(min.getX(), v.getX(), max.getX()) &&
		        betweenLower(min.getY(), v.getY(), max.getY()) &&
		        betweenLower(min.getZ(), v.getZ(), max.getZ());
	}
	
	/* (non-Javadoc)
     * @see tk.ericwieser.util.Region#contains(org.bukkit.Location)
     */
	@Override
    public boolean contains(Location l) {
		return l.getWorld() == world && contains(l.toVector());
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
	public void setWorld(World world) {
		this.world = world;
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
	
	public Region clone() {
		return new CuboidRegion(world, min, max);
	}

	@Override
    public Map<String, Object> serialize() {
	    Map<String, Object> m = new HashMap<String, Object>();
	    // m.put("world", world.getName());
	    m.put("min", min.serialize());
	    m.put("max", max.serialize());
	    return m;	    
    }
	
	@SuppressWarnings("unchecked")
    public static CuboidRegion deserialize(Map<String, Object> m) {		
		return new CuboidRegion(
				null,
				Vector.deserialize(getMap(m, "min")),
				Vector.deserialize(getMap(m, "max"))
		);
	}
}
