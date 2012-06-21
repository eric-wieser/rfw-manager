package tk.ericwieser.rfw.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ericwieser.rfw.Game;
import tk.ericwieser.rfw.Lane;
import tk.ericwieser.rfw.RFWManager;

public class LaneDiscard implements CommandExecutor {
	RFWManager plugin;

	public LaneDiscard(RFWManager plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
	        String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		if(args.length != 1) return false;
		
		Player p = (Player) sender;
		Game g = plugin.getGame(p);

		if (g == null) {
			sender.sendMessage(ChatColor.RED + "Not in a game!");
			return false;
		}
		
		Lane lane = g.getLane(args[0]);
		
		if(lane == null) {
			sender.sendMessage(ChatColor.RED + "Lane doesn't exist");
			return false;
		}

		sender.sendMessage(ChatColor.BLUE + "Lane "+args[0]+" deleted");
		g.getLanes().remove(lane);
		
		g.saveMapConfig();

		return true;
	}

}
