package tk.ericwieser.rfw;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Team {
	public Game game;
	private ChatColor color;
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
	
	public void setColor(String color) {
		color = color.toLowerCase();
		if("red"    .equals(color)) this.color = ChatColor.RED;
		if("green"  .equals(color)) this.color = ChatColor.GREEN;
		if("blue"   .equals(color)) this.color = ChatColor.BLUE;
		if("aqua"   .equals(color)) this.color = ChatColor.AQUA;
		if("magenta".equals(color)) this.color = ChatColor.LIGHT_PURPLE;
		if("purple" .equals(color)) this.color = ChatColor.LIGHT_PURPLE;
    }

	public void setName(String name) {
		this.name = name;
    }

	public List<Player> getPlayers() { return players; }
	public String getName() { return name; }
	public ChatColor getColor() { return color; }
}
