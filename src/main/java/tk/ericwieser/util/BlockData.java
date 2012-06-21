package tk.ericwieser.util;

import org.bukkit.Material;
import org.bukkit.block.Block;

import com.sk89q.worldedit.blocks.ClothColor;

public class BlockData {
	public Material mat;
	public byte     data;

	public BlockData(Material m, byte d) {
		mat = m;
		data = d;
	}

	@Override
	public int hashCode() {
		return mat.hashCode() ^ new Byte(data).hashCode();
	}

	@Override
	public boolean equals(Object o) {
		// if the object is a mismatched type, its not equal
		if (o == null || !(o instanceof BlockData)) return false;

		// otherwise, check that the data is all equivalent
		BlockData ob = (BlockData) o;
		return ob.mat.equals(mat) && ob.data == data;
	}

	// does this block data match the given block?
	public boolean matches(Block b) {
		// matches if materials and metadata are same
		return (b != null && b.getType().equals(mat)
		&& (data == -1 || data == b.getData()));
	}
	
	public String getWoolColor() {
		if(mat != Material.WOOL) return null;
		ClothColor c = ClothColor.fromID(data);
		if(c != null)
			return c.getName();
		else
			return null;
	}

	/**
	 * Convert to a human readable form, eg "wool:red", "stairs:3", or "dirt"
	 */
	@Override
	public String toString() {
		String materialName = mat.toString();
		
		if(data == -1)
			return materialName;
		
		String dataStr = Integer.toString(data);
		
		//Give wool a pretty name
		if(mat == Material.WOOL) {
			ClothColor c = ClothColor.fromID(data);
			if(c != null)
				dataStr = c.getName();
		}
		
		return materialName + ":" + dataStr;
	}

	/**
	 * Convert from a human readable form, eg "wool:red", "stairs:3", or "dirt"
	 */
	public static BlockData fromString(String s) {
		String[] parts = s.split(":", 2);
		String name = parts[0];

		Material material = Material.matchMaterial(name);
		byte data = -1;

		//Data value specified
		if (parts.length == 2) {
			String dataStr = parts[1];
			
			//parse a color string for wool
			if (material == Material.WOOL) {
				ClothColor c = ClothColor.lookup(dataStr);
				if (c != null) data = (byte) c.getID();
			} else {
				data = Byte.parseByte(dataStr);
			}
		}
		return new BlockData(material, data);
	}

	public static BlockData from(Block b) {
		return new BlockData(b.getType(), b.getData());
	}
}
