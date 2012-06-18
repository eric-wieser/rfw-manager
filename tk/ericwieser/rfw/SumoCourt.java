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

import com.sk89q.worldedit.bukkit.selections.Selection;

public class SumoCourt implements Listener {
	private Selection zone;
	private Game      game;
	private List<Player> players;

	public SumoCourt(Game game) {
		this.game = game;
	}

	public void setZone(Selection zone) {
		this.zone = zone;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void playerMoved(PlayerMoveEvent e) {
		Player p = e.getPlayer();
		//not interested
		if(players == null) return;
		if(!players.contains(p)) return;
		
		//player still in arena
		if (zone.contains(e.getTo())) return;

		Bukkit.getLogger().fine("Player moved out of ring");
		Bukkit.getLogger().fine("At:"+e.getTo());
		
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
		List<Entity> entities = zone.getWorld().getEntities();

		for (Entity e : entities) {
			if (e instanceof Player && zone.contains(e.getLocation())) {
				inArena.add((Player) e);
			}
		}
		return inArena;
	}
}
