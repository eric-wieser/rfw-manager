package tk.ericwieser.rfw.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ericwieser.rfw.Game;
import tk.ericwieser.rfw.RFWManager;
import tk.ericwieser.rfw.Team;


public class TeamName implements CommandExecutor {
	RFWManager plugin;

	public TeamName(RFWManager plugin) { this.plugin = plugin; }
	
	@Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
		if(args.length != 1) return false;
		
		String teamName = args[0];
		
		if(!(sender instanceof Player)) return false;
		
		Player p = (Player) sender;
		Game g = plugin.getGame(p);
		
		if(g == null) {
			sender.sendMessage(ChatColor.RED + "Not in a game!");
			return false;
		}
		
		Team t = g.getTeam(p);
		if(t == null) {
			sender.sendMessage(ChatColor.RED + "Not in a team");
			return false;
		}
		
		t.setName(teamName);
		return true;
    }

}
