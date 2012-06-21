package tk.ericwieser.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public interface Region {
	/** Check a block is fully contained within the region */
	public abstract boolean contains(BlockVector v);

	/** Check if a vector is contained within the region */
	public abstract boolean contains(Vector v);

	/** Like contains(Vector), but checks world as well */
	public abstract boolean contains(Location l);

	/** Change the world this region refers to */
	public abstract void setWorld(World world);
}