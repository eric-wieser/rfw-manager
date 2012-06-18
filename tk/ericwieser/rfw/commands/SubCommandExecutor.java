package tk.ericwieser.rfw.commands;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import tk.ericwieser.rfw.RFWManager;

public class SubCommandExecutor implements CommandExecutor {
	RFWManager plugin;
	String base;

	public SubCommandExecutor(RFWManager plugin, String base) {
		this.plugin = plugin;
		this.base = base;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,
	        String label, String[] args) {
		if (args.length < 1) return false;
		String subcommand = args[0];
		String[] newargs = Arrays.copyOfRange(args, 1, args.length);

		command = plugin.getCommand(base + subcommand);
		if (command != null) {
			command.execute(sender, base+" " + subcommand, newargs);
			return true;
		}
		return false;
	}
}
