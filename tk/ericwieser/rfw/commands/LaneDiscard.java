package tk.ericwieser.rfw.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ericwieser.rfw.Game;
import tk.ericwieser.rfw.Lane;
import tk.ericwieser.rfw.RFWManager;
import tk.ericwieser.util.CompoundRegion;
import tk.ericwieser.util.CuboidRegion;

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
		
		String laneName = args[0];
		Lane existing = g.getLane(args[0]);
		
		CuboidRegion c =
		                 new CuboidRegion(plugin.getWorldEdit().getSelection(p))
		                         .adjustX(0.3, 0.3)
		                         .adjustZ(0.3, 0.3);
		
		if(existing != null) {
			sender.sendMessage("Added blocks to existing lane");
			existing.getZone().add(c);
		} else {
			sender.sendMessage("Creating new lane");
			CompoundRegion r = new CompoundRegion();
			r.add(c);
			g.getLanes().add(new Lane(laneName, r));
		}
		
		g.saveMapConfig();

		return true;
	}

}
