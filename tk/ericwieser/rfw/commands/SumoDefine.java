package tk.ericwieser.rfw.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ericwieser.rfw.Game;
import tk.ericwieser.rfw.RFWManager;
import tk.ericwieser.util.CuboidRegion;

public class SumoDefine implements CommandExecutor {
	RFWManager plugin;

	public SumoDefine(RFWManager plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command,
	        String label, String[] args) {
		if (!(sender instanceof Player)) return false;

		Player p = (Player) sender;
		Game g = plugin.getGame(p);

		if (g == null) {
			sender.sendMessage(ChatColor.RED + "Not in a game!");
			return false;
		}
		CuboidRegion r =
		                 new CuboidRegion(plugin.getWorldEdit().getSelection(p))
		                         .adjustX(0.3, 0.3)
		                         .adjustY(3, -0.1)
		                         .adjustZ(0.3, 0.3);
		//Bukkit.getLogger().info(r.getMin() + ":" + r.getMax());
		g.getSumo().setZone(r);
		g.saveMapConfig();

		return true;
	}

}
