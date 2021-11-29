package fr.ezzud.castlewar.methods;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.CastleTeam;
import net.md_5.bungee.api.ChatColor;

public class messagesFormatter {
	static Main plugin = Main.getInstance();
	static YamlConfiguration messages = Main.messages;
	public static String formatTeamMessage(String message, CastleTeam team) {
		message = message.replaceAll("%prefix%", ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("commands").getString("prefix")));
		message = message.replaceAll("%teamcolor%", ChatColor.translateAlternateColorCodes('&', team.getColor()));
		message = message.replaceAll("%teamname%", ChatColor.translateAlternateColorCodes('&', team.getName()));
		message = message.replaceAll("%teamprefix%", ChatColor.translateAlternateColorCodes('&', team.getPrefix()));
		return message;
	}
	
	public static String formatKillMessage(String message, Player victim, Player killer) {
		message = message.replaceAll("%prefix%", ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("commands").getString("prefix")));
		message = message.replaceAll("%player%", ChatColor.translateAlternateColorCodes('&', victim.getName()));		
		if(killer != null) {
			message = message.replaceAll("%killer%", ChatColor.translateAlternateColorCodes('&', killer.getName()));
		}
		return message;
	}
	
	public static String formatTimeMessage(String message, Integer timer) {
		message = message.replaceAll("%prefix%", ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("commands").getString("prefix")));
		message = message.replaceAll("%seconds%", ChatColor.translateAlternateColorCodes('&', String.valueOf(timer)));
		return message;
	}
	
	public static String formatMessage(String message) {
		message = message.replaceAll("%prefix%", ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("commands").getString("prefix")));
		return message;
	}
	
	public static String formatPlayerMessage(String message, Player player) {
		message = message.replaceAll("%prefix%", ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("commands").getString("prefix")));
		message = message.replaceAll("%player%", ChatColor.translateAlternateColorCodes('&', player.getName()));
		return message;
	}

	public static String formatJoinMessage(String message, Player player, int count) {
		message = message.replaceAll("%prefix%", ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("commands").getString("prefix")));
		message = message.replaceAll("%player%", ChatColor.translateAlternateColorCodes('&', player.getName()));
		message = message.replaceAll("%playercount%", ChatColor.translateAlternateColorCodes('&', String.valueOf(count)));
		message = message.replaceAll("%maxplayers%", ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("maxPlayers")));
		return message;
	}
	
}
