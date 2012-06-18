package tk.ericwieser.rfw.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.ericwieser.rfw.Game;
import tk.ericwieser.rfw.RFWManager;
import tk.ericwieser.rfw.SumoCourt;
import tk.ericwieser.rfw.Team;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.sk89q.worldedit.regions.CuboidRegion;

public class SumoStart implements CommandExecutor {
	RFWManager plugin;

	public SumoStart(RFWManager plugin) {
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
		
		List<Player> players = g.getSumo().getPlayers();
		if(players.size() < 1/*2*/)
			sender.sendMessage(ChatColor.RED + "Not enough players in the ring");
		
		//List<Team> represented = new ArrayList<Team>();
		
		sender.sendMessage(ChatColor.BLUE + "" + players.size() + " in the ring");
		
//		for(Player q : players) {
//			Team t = g.getTeam(p);
//			if(t == null) {
//				sender.sendMessage(ChatColor.RED + "Player " + q.getName() + " has no team!");
//				return false;
//			}
//			else if(represented.contains(t)) {
//				sender.sendMessage(ChatColor.RED + "Team " + t.getName() + " has too many representatives");
//				return false;
//			}
//			else {
//				represented.add(t);
//			}
//		}
//		sender.sendMessage(ChatColor.BLUE + "" + represented.size() + " teams represented");
//		for(Team t : g.getTeams()) {
//			if(!represented.contains(t)) {
//				sender.sendMessage(ChatColor.RED + "Team " + t.getName() + " has no representatives");
//				return false;
//			}
//		}
		
		g.getSumo().start(players);
		g.sendMessage("Sumo match started!");
		
		return false;
	}

}
