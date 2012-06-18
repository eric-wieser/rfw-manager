package tk.ericwieser.rfw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;

import tk.ericwieser.rfw.commands.*;

public class RFWManager extends JavaPlugin implements Listener {
	Logger     log;

	TeamCreate teamCreate = new TeamCreate(this);
	TeamName   teamName   = new TeamName(this);
	TeamJoin   teamJoin   = new TeamJoin(this);
	TeamColor  teamColor  = new TeamColor(this);
	TeamList   teamList   = new TeamList(this);

	GameStart  gameStart  = new GameStart(this);
	GameStop   gameStop   = new GameStop(this);
	GameList   gameList   = new GameList(this);

	SumoDefine sumoDefine = new SumoDefine(this);
	SumoStart  sumoStart  = new SumoStart(this);

	List<Game> games      = new ArrayList<>();

	public Game getGame(Player p) {
		for (Game g : games) {
			if (g.hasPlayer(p)) return g;
		}
		return null;
	}

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

		World df = getServer().getWorld("direct_fire");
		if (df != null) games.add(new Game(this, df));
	};

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
