package fr.ezzud.castlewar.commands.admin;

import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.methods.configManager;
import net.md_5.bungee.api.ChatColor;

public class reloadCMD {
	static Main plugin = Main.getInstance();
	
	public reloadCMD(Player player) {
		if (player.hasPermission("castlewars.reload") || player.isOp()) {
			plugin.reloadConfig();
			plugin.saveConfig();
			configManager.saveKits();
			configManager.saveGUIs();
			configManager.saveTeams();
			Main.teams = configManager.getTeams();
			Main.guis = configManager.getGUIs();
			Main.kits = configManager.getKits();
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReloaded"));
		}

	}
}
