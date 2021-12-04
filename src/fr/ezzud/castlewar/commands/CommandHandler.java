package fr.ezzud.castlewar.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.commands.admin.reloadCMD;
import fr.ezzud.castlewar.commands.admin.setLobbyCMD;
import fr.ezzud.castlewar.commands.admin.setSpawnCMD;
import fr.ezzud.castlewar.commands.admin.startCMD;
import fr.ezzud.castlewar.commands.admin.stopCMD;
import fr.ezzud.castlewar.commands.players.helpCMD;
import fr.ezzud.castlewar.commands.players.kingsCMD;
import fr.ezzud.castlewar.commands.players.kitsCMD;
import fr.ezzud.castlewar.commands.players.teamCMD;
import fr.ezzud.castlewar.methods.inATeam;
import fr.ezzud.castlewar.methods.messagesFormatter;
import net.md_5.bungee.api.ChatColor;

public class CommandHandler implements CommandExecutor {
    static Main plugin = Main.getInstance();
    static YamlConfiguration messages = Main.messages;
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender.equals(Bukkit.getConsoleSender())) return false;
		Player player = Bukkit.getPlayer(sender.getName());
		if(args.length <= 0) {
			sender.sendMessage("Usage: /castlewar");
	   		return true;
	   	}
	   	switch(args[0]) {
	   		case "help":
	   			new helpCMD(player);
	   			break;
	   		case "menu":
	   			if(GameStateManager.getGameState() == false) {
	   				new teamCMD(player);
	   			}
	   			break;
	   		case "kings":
	   			if(GameStateManager.getGameState() == true) {
	   				new kingsCMD(player);
	   			}
	   		case "reload":
	   			new reloadCMD(player);
	   			break;
	   		case "kits":
	   			if(inATeam.isKing(player.getName()) == false) {
	   				new kitsCMD(player);
	   			}
	   			break;
	   		case "start":
	   			new startCMD(player);
	   			break;
	   		case "stop":
	   			new stopCMD(player);
	   			break;
	   		case "setspawn":
	   			new setSpawnCMD(player, args);
	   			break;
	   		case "setlobby":
	   			new setLobbyCMD(player);
	   			break;
	   		default:
	   			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getString("commands.unknown"))));
	   			break;
	   	}
	   	
	   	return true;
	
	}
}