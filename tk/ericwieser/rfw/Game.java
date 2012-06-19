package tk.ericwieser.rfw;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import tk.ericwieser.util.CuboidRegion;


public class Game {
	public enum State {SETUP, RUNNING};
	private World world;
	private State state = State.SETUP;
	
	private RFWManager plugin;
	private TeamListener teamListener = new TeamListener(this);
	
	private List<Team> teams = new ArrayList<Team>();
	private Map<String, Team> playerTeams = new HashMap<String, Team>();
	
	private SumoCourt sumo = new SumoCourt(this);
	
	private File configFile;
	private FileConfiguration config;
	
	public Game(RFWManager plugin, World world) {
		configFile = new File(world.getWorldFolder(), "rfwconfig.yml");
		if(!configFile.exists()) throw new IllegalArgumentException("World not compatible");
		config = YamlConfiguration.loadConfiguration(configFile);
		loadMapConfig();
		
		this.plugin = plugin;
		this.world = world;
		PluginManager pmgr = plugin.getServer().getPluginManager();
		pmgr.registerEvents(teamListener, plugin);
		pmgr.registerEvents(sumo, plugin);
	}
	
	@SuppressWarnings("unchecked")
    public void loadMapConfig() {
		if(config.contains("sumo")) {
    		ConfigurationSection s =  config.getConfigurationSection("sumo");
    		CuboidRegion zone = CuboidRegion.deserialize(
    				s.getConfigurationSection("zone").getValues(false)
    		);
    		sumo.setZone(zone);
		}
	}
	
	public void saveMapConfig() {
		if( sumo.getZone() != null) {
    		Map<String, Object> sumoconfig = new HashMap<>();
    		sumoconfig.put("zone", sumo.getZone().serialize());
    		config.set("sumo", sumoconfig);
		}
		try {
	        config.save(configFile);
        } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
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
		if(t.getPlayers().size() == 0) {
			teams.remove(t);
		}
	}
	
	public void onSumoWon(Player winner) {
		for(Player p : world.getPlayers())
			p.sendMessage(winner.getName() + " won the sumo!");
	}
	public void onSumoKnockout(Player loser) {
		for(Player p : world.getPlayers())
			p.sendMessage(loser.getName() + " was knocked out!");
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
	public void sendMessage(String s) {
		for(Player p : world.getPlayers())
			p.sendMessage(s);
	}
	public void stop() {
		state = State.SETUP;
		for(Player p : world.getPlayers())
			p.sendMessage("Leaving team chat mode");
	}

	public Team getTeam(String teamName) {
	    for(Team t : teams)
	    	if(t.getName().equals(teamName))
	    		return t;
	    return null;
    }
	public Team getTeam(Player p) {
		return playerTeams.get(p.getName());
	}

	public List<Team> getTeams() { return teams; }
	public SumoCourt getSumo() { return sumo; }
	public RFWManager getPlugin() { return plugin; }

}
