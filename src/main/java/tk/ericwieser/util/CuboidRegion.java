package tk.ericwieser.util;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;
import static tk.ericwieser.util.ConfigUtil.*;

import com.sk89q.worldedit.bukkit.selections.Selection;

/**
 * A cuboidal region with double coordinates, unlike the worldedit class that
 * uses integers.
 */
public class CuboidRegion implements ConfigurationSerializable, Region {
	static {
		ConfigurationSerialization.registerClass(CuboidRegion.class);
	}
	World world;
	Vector min;
	Vector max;

	public Vector getMin() { return min; }
	public void setMin(Vector min) { set(max, min);	}
	
	public Vector getMax() { return max; }
	public void setMax(Vector max) { set(max, min); }

	public World getWorld() { return world; }
	public void setWorld(World world) { this.world = world; }

	public CuboidRegion(World w, Vector a, Vector b) {
		world = w;
		set(a, b);
	}

	/**
	 * Make a region from a worldedit selection. Fixwa the inherent off-by-one
	 * error.
	 */
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

	/** check if a value is between two others, including lower bound */
	private boolean betweenLower(double lower, double x, double upper) {
		return lower <= x && x < upper;
	}

	/** check if a value is between two others, including both bounds */
	private boolean betweenBoth(double lower, double x, double upper) {
		return lower <= x && x <= upper;
	}

	/**
	 * Check if a block fully lies within a region, by checking its lower corner
	 * is greater than or equal to the lower bound of the region, and less than
	 * or equal to the upper bound of the region minus (1, 1, 1)
	 */
	@Override
	public boolean contains(BlockVector v) {
		return betweenBoth(min.getX(), v.getBlockX(), max.getX() - 1)
		    && betweenBoth(min.getY(), v.getBlockY(), max.getY() - 1)
		    && betweenBoth(min.getZ(), v.getBlockZ(), max.getZ() - 1);
	}

	/**
	 * Check if a vector lies within the region, including the lower bound but
	 * excluding the upper bound
	 **/
	@Override
	public boolean contains(Vector v) {
		return betweenLower(min.getX(), v.getX(), max.getX())
		    && betweenLower(min.getY(), v.getY(), max.getY())
		    && betweenLower(min.getZ(), v.getZ(), max.getZ());
	}

	@Override
	public boolean contains(Location l) {
		return l.getWorld() == world && contains(l.toVector());
	}

	/**
	 * adjust the X-size of the region
	 * @param positive amount to add to the upper bound
	 * @param negative amount to subtract from the lower bound
	 * @return this
	 */
	public CuboidRegion adjustX(double positive, double negative) {
		min.setX(min.getX() - negative);
		max.setX(max.getX() + positive);
		return this;
	}

	/**
	 * adjust the Y-size of the region
	 * @param positive amount to add to the upper bound
	 * @param negative amount to subtract from the lower bound
	 * @return this
	 */
	public CuboidRegion adjustY(double positive, double negative) {
		min.setY(min.getY() - negative);
		max.setY(max.getY() + positive);
		return this;
	}

	/**
	 * adjust the Z-size of the region
	 * @param positive amount to add to the upper bound
	 * @param negative amount to subtract from the lower bound
	 * @return this
	 */
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
		m.put("min", min.serialize());
		m.put("max", max.serialize());
		return m;
	}

	public static CuboidRegion deserialize(Map<String, Object> m) {
		return new CuboidRegion(
			null,
			Vector.deserialize(getMap(m, "min")),
			Vector.deserialize(getMap(m, "max"))
		);
	}
}
