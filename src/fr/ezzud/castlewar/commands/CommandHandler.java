package fr.ezzud.castlewar.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.commands.admin.reloadCMD;
import fr.ezzud.castlewar.commands.players.kitsCMD;
import fr.ezzud.castlewar.commands.players.teamCMD;
import fr.ezzud.castlewar.methods.inATeam;

public class CommandHandler implements CommandExecutor {
    static Main plugin = Main.getInstance();
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player player = Bukkit.getPlayer(sender.getName());
		if(args.length <= 0) {
			sender.sendMessage("Usage: /castlewar");
	   		return true;
	   	}
	   	switch(args[0]) {
	   		case "menu":
	   			if(GameStateManager.getGameState() == false) {
	   				new teamCMD(player);
	   			}
	   			break;
	   		case "reload":
	   			new reloadCMD(player);
	   			break;
	   		case "kits":
	   			if(inATeam.isKing(player.getName()) == false) {
	   				new kitsCMD(player);
	   			}
	   			
	   			break;
	   		default:
	   			sender.sendMessage("No command");
	   			break;
	   	}
	   	
	   	return true;
	
	}
}