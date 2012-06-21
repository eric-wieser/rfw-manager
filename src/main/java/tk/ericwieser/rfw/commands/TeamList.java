package tk.ericwieser.rfw.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ericwieser.rfw.Game;
import tk.ericwieser.rfw.RFWManager;
import tk.ericwieser.rfw.Team;


public class TeamList implements CommandExecutor {
	RFWManager plugin;

	public TeamList(RFWManager plugin) { this.plugin = plugin; }
	
	@Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {		
		
		if(!(sender instanceof Player)) return false;
		
		Player p = (Player) sender;
		Game g = plugin.getGame(p);
		
		if(g == null) {
			sender.sendMessage(ChatColor.RED + "Not in a game!");
			return false;
		}
		
		for(Team t : g.getTeams()) {
			String msg = t.getColor() + t.getName() + ChatColor.WHITE;
			if(sender instanceof Player && g.hasPlayer((Player) sender))
				sender.sendMessage(ChatColor.GOLD +" *"+msg);
			else
				sender.sendMessage("  "+msg);
			
			for(Player q : t.getPlayers()) {
				sender.sendMessage("    "+q.getName());
			}
		}
		
		return true;
    }

}
