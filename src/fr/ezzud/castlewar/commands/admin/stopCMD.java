package fr.ezzud.castlewar.commands.admin;

import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;
import net.md_5.bungee.api.ChatColor;

public class stopCMD {
	static Main plugin = Main.getInstance();
	
	public stopCMD(Player player) {
		if (player.hasPermission("castlewars.stop") || player.isOp()) {
			if(GameStateManager.getGameState() == true) {
				new GameStateManager().stopGame();
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aStopped"));
			} else {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cGame not started"));
				return;
			}
			
			
		}

	}
}
