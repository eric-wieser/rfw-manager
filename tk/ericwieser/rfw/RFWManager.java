package tk.ericwieser.rfw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import tk.ericwieser.rfw.commands.*;

public class RFWManager extends JavaPlugin implements Listener {
	Logger log;
	
	TeamCreate teamCreate = new TeamCreate(this);
	TeamName teamName = new TeamName(this);
	TeamColor teamColor = new TeamColor(this);
	TeamList teamList = new TeamList(this);
	
	GameStart gameStart = new GameStart(this);
	GameStop gameStop = new GameStop(this);
	GameList gameList = new GameList(this);
	
	List<Game> games = new ArrayList<>();
	
	public Game getGame(Player p) {
		for(Game g : games) {
			if(g.hasPlayer(p)) return g;
		}
		return null;
	}
	
	public void onEnable(){
		getCommand("teamcreate").setExecutor(teamCreate);
		getCommand("teamname").setExecutor(teamName);
		getCommand("teamcolor").setExecutor(teamColor);
		getCommand("teamlist").setExecutor(teamList);
		getCommand("team").setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command,
			        String label, String[] args) {
				if(args.length < 1) return true;
				String subcommand = args[0];
				String[] newargs = Arrays.copyOfRange(args, 1, args.length);

				command = RFWManager.this.getCommand("team"+subcommand);
				if(command != null) {
					command.execute(sender, "team "+subcommand, newargs);
					return true;
				}
				return false;
			}
		});
		
		getCommand("gamestart").setExecutor(gameStart);
		getCommand("gamestop").setExecutor(gameStop);
		getCommand("gamelist").setExecutor(gameList);
		getCommand("game").setExecutor(new CommandExecutor() {
			@Override
			public boolean onCommand(CommandSender sender, Command command,
			        String label, String[] args) {
				if(args.length < 1) return true;
				String subcommand = args[0];
				String[] newargs = Arrays.copyOfRange(args, 1, args.length);

				command = RFWManager.this.getCommand("game"+subcommand);
				if(command != null) {
					command.execute(sender, "game "+subcommand, newargs);
					return true;
				}
				return false;
			}
		});
		
		World df = getServer().getWorld("direct_fire");
		if(df != null) games.add(new Game(this, df));
	};
 
	public void onDisable(){
		getLogger().info("Your plugin has been disabled.");
	}

	public List<Game> getGames() { return games; }
}
