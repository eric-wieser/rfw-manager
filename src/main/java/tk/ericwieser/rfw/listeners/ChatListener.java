package tk.ericwieser.rfw.listeners;

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import tk.ericwieser.rfw.Game;
import tk.ericwieser.rfw.Team;

public class ChatListener implements Listener {
	Game game;
	
	@EventHandler
	public void playerChatted(PlayerChatEvent e) {
		Player p = e.getPlayer();

		if (p.getWorld() != game.getWorld())
			return;

		Team t = game.getTeam(p);
		ChatColor nameColor = t == null ? ChatColor.RESET : t.getColor();
		String message = (
			ChatColor.GRAY + "<" +
			nameColor + p.getName() +
			ChatColor.GRAY + ">" +
			ChatColor.WHITE + e.getMessage()
		);

		e.setFormat(message);

		if (game.getState() == Game.State.RUNNING) {
			Iterator<Player> iter = e.getRecipients().iterator();
			while (iter.hasNext()) {
				// if listener is on a team, and its not the same team as the
				// speaker, remove them from the recipients list
				Team ot = game.getTeam(iter.next());
				if (ot != null && ot != t)
					iter.remove();
			}
		}
	}

	public ChatListener(Game game) {
		this.game = game;
	}

}
