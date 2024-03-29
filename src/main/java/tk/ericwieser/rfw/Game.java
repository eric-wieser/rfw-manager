package tk.ericwieser.rfw;
import static tk.ericwieser.util.ConfigUtil.getMap;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import tk.ericwieser.rfw.listeners.ChatListener;
import tk.ericwieser.rfw.listeners.LaneListener;
import tk.ericwieser.util.ConfigUtil;
import tk.ericwieser.util.CuboidRegion;
import tk.ericwieser.util.SectionRemovingConfiguration;


public class Game {
	public enum State {SETUP, RUNNING};
	private World world;
	private State state = State.SETUP;
	
	private RFWManager plugin;
	private ChatListener teamListener = new ChatListener(this);
	
	private List<Team> teams = new ArrayList<Team>();
	private List<Lane> lanes = new ArrayList<Lane>();
	private Map<String, Team> playerTeams = new HashMap<String, Team>();
	
	private SumoCourt sumo = new SumoCourt(this);
	
	private File configFile;
	private FileConfiguration config;
	
	private PluginManager pmgr;
	
	public Game(RFWManager plugin, World world) {
		configFile = new File(world.getWorldFolder(), "rfwconfig.yml");
		if(!configFile.exists()) throw new IllegalArgumentException("World not compatible");
		config = new SectionRemovingConfiguration();
		try {
			config.load(configFile);
		} catch (IOException | InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.plugin = plugin;
		this.world = world;
		pmgr = plugin.getServer().getPluginManager();
		pmgr.registerEvents(teamListener, plugin);
		pmgr.registerEvents(sumo, plugin);
		loadMapConfig();
	}
	
	public void loadMapConfig() {
		if(config.contains("sumo")) {
			CuboidRegion zone = CuboidRegion.deserialize(
				getMap(getMap(config, "sumo"), "zone")
			);
			sumo.setZone(zone);
		}
		
		if(config.contains("lanes")) {
			lanes.clear();
			List<Lane> lanes = ConfigUtil.deserialize(
				ConfigUtil.<Map<String, Object>>getMap(config, "lanes"),
				Lane.class
			);
			
			for(Lane l : lanes) {
				l.setGame(this);
				pmgr.registerEvents(new LaneListener(l), plugin);
			}
		}
	}
	
	public void saveMapConfig() {
		if( sumo.getZone() != null) {
			Map<String, Object> sumoconfig = new HashMap<>();
			sumoconfig.put("zone", sumo.getZone().serialize());
			config.set("sumo", sumoconfig);
		}

		config.set("lanes", ConfigUtil.serialize(lanes));
		
		
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

	public Team getTeam(Player p) {
		return playerTeams.get(p.getName());
	}
	
	public Team getTeam(String teamName) {
		for(Team t : teams)
			if(t.getName().equals(teamName))
				return t;
		return null;
	}
	
	public Lane getLane(String laneName) {
		for(Lane l : lanes)
			if(l.getName().equals(laneName))
				return l;
		return null;
	}
	
	public List<Team> getTeams() { return teams; }
	public List<Lane> getLanes() { return lanes; }
	public SumoCourt getSumo() { return sumo; }
	public RFWManager getPlugin() { return plugin; }
}
