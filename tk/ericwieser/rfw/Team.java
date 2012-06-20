package tk.ericwieser.rfw;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import tk.ericwieser.util.Nameable;

public class Team implements Nameable {
	public Game game;
	private ChatColor color = ChatColor.YELLOW;
	private String name;

	public Team(String name, Game g) {
		super();
		this.name = name;
		this.game = g;
	}

	private List<Player> players = new ArrayList<Player>();
	
	public void addPlayer(Player p) {
		players.add(p);
		game.onPlayerAddedToTeam(p, this);
	}
	
	public void removePlayer(Player p) {
		players.remove(p);
		game.onPlayerRemovedFromTeam(p, this);
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	private ChatColor fromString(String color) {
		color = color.toUpperCase();
		try {
			ChatColor c = ChatColor.valueOf(color);
			if(c.isColor())
				return c;
		} catch(IllegalArgumentException e) { }
		return null;
	}
	
	public void setColor(String color) {
		ChatColor c = fromString(color);
		if(c != null)
			this.color = c;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Player> getPlayers() { return players; }
	public String getName() { return name; }
	public ChatColor getColor() { return color; }
}
