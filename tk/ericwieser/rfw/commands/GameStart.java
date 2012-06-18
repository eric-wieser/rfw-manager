package tk.ericwieser.rfw.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ericwieser.rfw.Game;
import tk.ericwieser.rfw.RFWManager;


public class GameStart implements CommandExecutor {
	RFWManager plugin;

	public GameStart(RFWManager plugin) { this.plugin = plugin; }
	
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

		g.start();
		return true;
    }

}
