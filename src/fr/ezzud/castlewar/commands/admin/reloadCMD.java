package fr.ezzud.castlewar.commands.admin;

import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.TeamManager;
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
			configManager.saveData();
			Main.guis = configManager.getGUIs();
			Main.kits = configManager.getKits();
			Main.data = configManager.getData();
			Scoreboard board = Main.board;
			Team team1 = board.getTeam("team1");
			Team team2 = board.getTeam("team2");
			team1.setPrefix(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getPrefix()));
			team2.setPrefix(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam2().getPrefix()));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aReloaded"));
		}

	}
}
