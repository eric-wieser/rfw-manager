package tk.ericwieser.rfw.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ericwieser.rfw.Game;
import tk.ericwieser.rfw.RFWManager;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

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
		CuboidSelection sel = (CuboidSelection) plugin.getWorldEdit().getSelection(p);
		Bukkit.getLogger().info(sel.getLength() + "," + sel.getWidth());
		Bukkit.getLogger().info(sel.getMinimumPoint() + ":" + sel.getMaximumPoint());
		sel = new CuboidSelection(sel.getWorld(),
				sel.getMinimumPoint().add(-0.35, 2.5, -0.35),
				sel.getMaximumPoint().add(1.35, 5.5, 1.35));
		Bukkit.getLogger().info(sel.getLength() + "," + sel.getWidth());
		Bukkit.getLogger().info(sel.getMinimumPoint() + ":" + sel.getMaximumPoint());
		g.getSumo().setZone(sel);
		
		return true;
	}

}
