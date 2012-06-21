package tk.ericwieser.rfw;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import tk.ericwieser.rfw.commands.GameList;
import tk.ericwieser.rfw.commands.GameStart;
import tk.ericwieser.rfw.commands.GameStop;
import tk.ericwieser.rfw.commands.LaneDefine;
import tk.ericwieser.rfw.commands.LaneDiscard;
import tk.ericwieser.rfw.commands.SubCommandExecutor;
import tk.ericwieser.rfw.commands.SumoDefine;
import tk.ericwieser.rfw.commands.SumoStart;
import tk.ericwieser.rfw.commands.TeamColor;
import tk.ericwieser.rfw.commands.TeamCreate;
import tk.ericwieser.rfw.commands.TeamJoin;
import tk.ericwieser.rfw.commands.TeamList;
import tk.ericwieser.rfw.commands.TeamName;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;

public class RFWManager extends JavaPlugin implements Listener {
	Logger     log;

	TeamCreate teamCreate = new TeamCreate(this);
	TeamName teamName = new TeamName(this);
	TeamJoin teamJoin = new TeamJoin(this);
	TeamColor teamColor = new TeamColor(this);
	TeamList teamList = new TeamList(this);

	GameStart gameStart = new GameStart(this);
	GameStop gameStop = new GameStop(this);
	GameList gameList = new GameList(this);

	SumoDefine sumoDefine = new SumoDefine(this);
	SumoStart sumoStart = new SumoStart(this);

	LaneDefine laneDefine = new LaneDefine(this);
	LaneDiscard laneDiscard = new LaneDiscard(this);

	List<Game> games = new ArrayList<>();

	public Game getGame(Player p) {
		for (Game g : games) {
			if (g.hasPlayer(p)) return g;
		}
		return null;
	}

	@Override
	public void onEnable() {
		getCommand("teamcreate").setExecutor(teamCreate);
		getCommand("teamname").setExecutor(teamName);
		getCommand("teamjoin").setExecutor(teamJoin);
		getCommand("teamcolor").setExecutor(teamColor);
		getCommand("teamlist").setExecutor(teamList);
		getCommand("team").setExecutor(new SubCommandExecutor(this, "team"));

		getCommand("gamestart").setExecutor(gameStart);
		getCommand("gamestop").setExecutor(gameStop);
		getCommand("gamelist").setExecutor(gameList);
		getCommand("game").setExecutor(new SubCommandExecutor(this, "game"));

		getCommand("sumodefine").setExecutor(sumoDefine);
		getCommand("sumostart").setExecutor(sumoStart);
		getCommand("sumo").setExecutor(new SubCommandExecutor(this, "sumo"));
		
		getCommand("lanedefine").setExecutor(laneDefine);
		getCommand("lanediscard").setExecutor(laneDiscard);
		getCommand("lane").setExecutor(new SubCommandExecutor(this, "lane"));

		World df = getServer().getWorld("direct_fire");
		if (df != null) games.add(new Game(this, df));
	};

	@Override
	public void onDisable() {
		getLogger().info("Your plugin has been disabled.");
	}

	public List<Game> getGames() {
		return games;
	}

	public WorldEditPlugin getWorldEdit() {
		Plugin plugin = getServer().getPluginManager().getPlugin("WorldEdit");

		// WorldEdit may not be loaded
		if (plugin == null || !(plugin instanceof WorldEditPlugin)) return null;

		return (WorldEditPlugin) plugin;
	}
}
