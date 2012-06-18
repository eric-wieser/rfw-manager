package tk.ericwieser.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class CuboidRegion {
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
}
