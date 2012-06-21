package tk.ericwieser.rfw.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ericwieser.rfw.Game;
import tk.ericwieser.rfw.RFWManager;


public class GameList implements CommandExecutor {
	RFWManager plugin;

	public GameList(RFWManager plugin) { this.plugin = plugin; }
	
	@Override
    public boolean onCommand(CommandSender sender, Command command,
            String label, String[] args) {
		sender.sendMessage("running games:");
		for(Game g : plugin.getGames()) {
			String name = g.getWorld().getName();
			if(sender instanceof Player && g.hasPlayer((Player) sender))
				sender.sendMessage(ChatColor.GOLD +"*"+ChatColor.WHITE + name);
			else
				sender.sendMessage(" "+name);
		}
		return true;
    }

}
