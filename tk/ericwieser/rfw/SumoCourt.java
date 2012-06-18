package tk.ericwieser.rfw;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import tk.ericwieser.util.CuboidRegion;

import com.sk89q.worldedit.bukkit.selections.Selection;

public class SumoCourt implements Listener {
	private CuboidRegion zone;
	private Game      game;
	private List<Player> players;

	public SumoCourt(Game game) {
		this.game = game;
	}

	public void setZone(CuboidRegion zone) {
		this.zone = zone;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void playerMoved(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		//not interested
		if(players == null) return;
		if(!game.hasPlayer(p)) return;
		if(!players.contains(p)) return;
		
		//player still in arena
		if (zone.contains(e.getTo())) return;

		Bukkit.getLogger().info("Player moved out of ring");
		Bukkit.getLogger().info("At:"+e.getTo());
		Bukkit.getLogger().info(zone.getMin() + ":" + zone.getMax());
		
		players.remove(p);
		game.onSumoKnockout(p);
		if(players.size() == 1) {
			game.onSumoWon(players.get(0));
			players = null;
		}
	}
	
	public void start(List<Player> players) {
		this.players = players;
	}

	public List<Player> getPlayers() {
		List<Player> inArena = new ArrayList<Player>();
		if(zone == null) return inArena;
		List<Player> players = zone.getWorld().getPlayers();

		for (Player p : players) {
			Bukkit.getLogger().info(p.getLocation() + "");
			if (zone.contains(p.getLocation())) {
				inArena.add(p);
			}
		}
		return inArena;
	}
}
