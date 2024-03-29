package tk.ericwieser.rfw.commands;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ericwieser.rfw.Game;
import tk.ericwieser.rfw.RFWManager;
import tk.ericwieser.rfw.Team;


public class TeamCreate implements CommandExecutor {
	RFWManager plugin;

	public TeamCreate(RFWManager plugin) { this.plugin = plugin; }
	
	@Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
		if(args.length < 1) return false;
		
		String teamName = StringUtils.join(args, " ", 1, args.length);
		
		if(!(sender instanceof Player)) return false;
		
		Player p = (Player) sender;
		Game g = plugin.getGame(p);
		
		if(g == null) {
			sender.sendMessage(ChatColor.RED + "Not in a game!");
			return false;
		}
		
		Team t = g.AddTeam(teamName);
		if(t == null) {
			sender.sendMessage(ChatColor.RED + "Can't create another team");
			return false;
		}

		Team old = g.getTeam(p);
		if(old != null) {
			old.removePlayer(p);
		}
		
		t.addPlayer(p);
		return true;
    }

}
