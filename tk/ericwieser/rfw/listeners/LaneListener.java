package tk.ericwieser.rfw.listeners;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import tk.ericwieser.rfw.Lane;
import tk.ericwieser.rfw.Lane.WoolPlacement;

public class LaneListener implements Listener {
	private Lane lane;

	public LaneListener(Lane l) {
		this.lane = l;
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void playerMoved(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		// not interested
		if (!lane.getGame().hasPlayer(p))
			return;

		// player still in arena
		boolean wasIn = lane.getZone().contains(e.getFrom());
		boolean isIn = lane.getZone().contains(e.getTo());

		if (wasIn && !isIn) {
			lane.getPlugin().getLogger()
				.info(p.getName() + " left lane \"" + lane.getName() + "\"");
			p.sendMessage("You left lane \"" + lane.getName() + "\"");
		}
		else if (!wasIn && isIn) {
			lane.getPlugin().getLogger()
				.info(p.getName() + " entered lane \"" + lane.getName() + "\"");
			p.sendMessage("You entered lane \"" + lane.getName() + "\"");
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void blockPlaced(BlockPlaceEvent e) {
		Block b = e.getBlock();
		for (WoolPlacement w : lane.getWools()) {
			if (w.block.matches(b) && w.location.equals(b.getLocation())) {
				lane.getGame().sendMessage("The " + w.name + " was placed on the " + lane.getName());
				return;
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void blockDestroyed(BlockBreakEvent e) {
		Block b = e.getBlock();
		for (WoolPlacement w : lane.getWools()) {
			if (w.block.matches(b) && w.location.equals(b.getLocation())) {
				lane.getGame().sendMessage("The " + w.name + " was removed on the "
						+ lane.getName());
				return;
			}
		}
	}
	
}
