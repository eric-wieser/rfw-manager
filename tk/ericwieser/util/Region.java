package tk.ericwieser.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public interface Region {

	/** Check a block is fully contained within the region */
	public abstract boolean contains(BlockVector v);

	public abstract boolean contains(Vector v);

	public abstract boolean contains(Location l);

	public abstract void setWorld(World world);

}