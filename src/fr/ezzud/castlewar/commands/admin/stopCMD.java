package fr.ezzud.castlewar.commands.admin;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.methods.messagesFormatter;
import net.md_5.bungee.api.ChatColor;

public class stopCMD {
	static Main plugin = Main.getInstance();
	YamlConfiguration messages = Main.messages;
	public stopCMD(Player player) {
		if (player.hasPermission("castlewars.admin.stop") || player.isOp()) {
			if(GameStateManager.getGameState() == true) {
				new GameStateManager().stopGame();
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("commands.stop").getString("successfull"))));
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("commands.stop").getString("alreadyStopped"))));
				return;
			}
			
			
		}

	}
}
