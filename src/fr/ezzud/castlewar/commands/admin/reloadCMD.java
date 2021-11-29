package fr.ezzud.castlewar.commands.admin;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.TeamManager;
import fr.ezzud.castlewar.methods.messagesFormatter;
import fr.ezzud.castlewar.methods.managers.configManager;
import net.md_5.bungee.api.ChatColor;

public class reloadCMD {
	static Main plugin = Main.getInstance();
	YamlConfiguration messages = Main.messages;
	public reloadCMD(Player player) {
		if (player.hasPermission("castlewars.admin.reload") || player.isOp()) {
			plugin.reloadConfig();
			plugin.saveConfig();
			configManager.saveKits();
			configManager.saveGUIs();
			configManager.saveData();
			configManager.saveMessages();
			Main.guis = configManager.getGUIs();
			Main.kits = configManager.getKits();
			Main.data = configManager.getData();
			Main.messages = configManager.getMessages();
			Scoreboard board = Main.board;
			Team team1 = board.getTeam("team1");
			Team team2 = board.getTeam("team2");
			team1.setPrefix(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getPrefix()));
			team2.setPrefix(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam2().getPrefix()));
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("commands.reload").getString("successfull"))));
		}

	}
}
