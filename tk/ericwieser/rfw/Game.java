package tk.ericwieser.rfw;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.entity.Player;


public class Game {
	public enum State {SETUP, RUNNING};
	private World world;
	private State state = State.SETUP;
	
	private RFWManager plugin;
	private TeamListener teamListener = new TeamListener(this);
	
	private List<Team> teams = new ArrayList<Team>();
	private Map<String, Team> playerTeams = new HashMap<String, Team>();
	
	public Game(RFWManager plugin, World world) {
		this.plugin = plugin;
		this.world = world;
		plugin.getServer().getPluginManager().registerEvents(teamListener, plugin);
	}
	
	public World getWorld() { return world; }
	public State getState() { return state; }
	
	
	public boolean hasPlayer(Player p) {
		return p.getWorld() == world;
	}
	
	public void onPlayerAddedToTeam(Player p, Team t) {
		playerTeams.put(p.getName(), t);
	}

	public void onPlayerRemovedFromTeam(Player p, Team t) {
		playerTeams.remove(p.getName());
	}
	public Team AddTeam(String teamName) {
	    Team t = new Team(teamName, this);
	    teams.add(t);
	    return t;
    }
	public void start() {
		for(Team t : teams) {
    		for(Player p : t.getPlayers()) {
    			//p.setHealth(20);
    			//p.setFoodLevel(20);
    			p.sendMessage("Entering team chat mode");
    		}
		}
		state = State.RUNNING;
	}
	public void stop() {
		state = State.SETUP;
		for(Player p : world.getPlayers())
			p.sendMessage("Entering team chat mode");
	}

	public Team getTeam(String teamName) {
	    for(Team t : teams)
	    	if(t.getName() == teamName)
	    		return t;
	    return null;
    }
	public Team getTeam(Player p) {
		return playerTeams.get(p.getName());
	}

	public List<Team> getTeams() { return teams; }
}
