package fr.ezzud.castlewar.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.methods.messagesFormatter;
import net.md_5.bungee.api.ChatColor;

public class startCMD {
	static Main plugin = Main.getInstance();
	YamlConfiguration messages = Main.messages;
	public startCMD(Player player) {
		if (player.hasPermission("castlewars.start") || player.isOp()) {
			if(GameStateManager.getGameState() == false) {
				Object[] array = Bukkit.getOnlinePlayers().toArray();
				if(array.length < plugin.getConfig().getInt("minPlayers")) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("commands.start").getString("notEnough"))));
					return;
				}
				new GameStateManager().startGame();
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("commands.start").getString("successfull"))));
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("commands.start").getString("alreadyStarted"))));
				return;
			}
			
			
		}

	}
}
