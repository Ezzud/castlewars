package fr.ezzud.castlewar.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;
import net.md_5.bungee.api.ChatColor;

public class startCMD {
	static Main plugin = Main.getInstance();
	
	public startCMD(Player player) {
		if (player.hasPermission("castlewars.start") || player.isOp()) {
			if(GameStateManager.getGameState() == false) {
				Object[] array = Bukkit.getOnlinePlayers().toArray();
				if(array.length < plugin.getConfig().getInt("minPlayers")) {
					Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "Not enough player to start the game"));
					return;
				}
				new GameStateManager().startGame();
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aStarted"));
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cGame already started"));
				return;
			}
			
			
		}

	}
}
