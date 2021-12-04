package fr.ezzud.castlewar.methods;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.CastleKit;
import fr.ezzud.castlewar.api.CastleTeam;
import fr.ezzud.castlewar.api.GameStateManager;
import net.md_5.bungee.api.ChatColor;

public class messagesFormatter {
	static Main plugin = Main.getInstance();
	static YamlConfiguration messages = Main.messages;
	static YamlConfiguration data = Main.data;
	public static String formatTeamMessage(String message, CastleTeam team) {
		message = message.replaceAll("%prefix%", ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("commands").getString("prefix")));
		message = message.replaceAll("%teamcolor%", ChatColor.translateAlternateColorCodes('&', team.getColor()));
		message = message.replaceAll("%teamname%", ChatColor.translateAlternateColorCodes('&', team.getName()));
		message = message.replaceAll("%teamprefix%", ChatColor.translateAlternateColorCodes('&', team.getPrefix()));
		message = message.replaceAll("%kingname%", ChatColor.translateAlternateColorCodes('&', team.getKing().getName()));
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
	
	
	public static String formatScoreboard(String message, Player player, int count, CastleTeam team, int hours, int minutes, int seconds) {
		message = message.replaceAll("%prefix%", ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("commands").getString("prefix")));
		message = message.replaceAll("%player%", ChatColor.translateAlternateColorCodes('&', player.getName()));
		message = message.replaceAll("%playercount%", ChatColor.translateAlternateColorCodes('&', String.valueOf(count)));
		message = message.replaceAll("%maxplayers%", ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("maxPlayers")));
		if(GameStateManager.GameState == true) {
			message = message.replaceAll("%teamcolor%", ChatColor.translateAlternateColorCodes('&', team.getColor()));
			message = message.replaceAll("%teamname%", ChatColor.translateAlternateColorCodes('&', team.getName()));
			message = message.replaceAll("%teamprefix%", ChatColor.translateAlternateColorCodes('&', team.getPrefix()));
			message = message.replaceAll("%kingname%", ChatColor.translateAlternateColorCodes('&', team.getKing().getName()));
			message = message.replaceAll("%kinghealth%", ChatColor.translateAlternateColorCodes('&', String.valueOf((int)team.getKing().getHealth())));
			if(team.getKing().getName().equalsIgnoreCase(player.getName())) {
				message = message.replaceAll("%rank%", ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("scoreboard.ranks").getString("king")));
				message = message.replaceAll("%kitname%", ChatColor.translateAlternateColorCodes('&', "King"));
			} else {
				if(data.getString(String.valueOf(player.getUniqueId())) == null) {
					message = message.replaceAll("%kitname%", ChatColor.translateAlternateColorCodes('&', new CastleKit(plugin.getConfig().getString("default_kit")).getName()));
				} else {
					message = message.replaceAll("%kitname%", ChatColor.translateAlternateColorCodes('&', new CastleKit(data.getString(String.valueOf(player.getUniqueId()))).getName()));
				}
				message = message.replaceAll("%rank%", ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("scoreboard.ranks").getString("soldier")));
			}
		} else {
			if(data.getString(String.valueOf(player.getUniqueId())) == null) {
				message = message.replaceAll("%kitname%", ChatColor.translateAlternateColorCodes('&', new CastleKit(plugin.getConfig().getString("default_kit")).getName()));
			} else {
				message = message.replaceAll("%kitname%", ChatColor.translateAlternateColorCodes('&', new CastleKit(data.getString(String.valueOf(player.getUniqueId()))).getName()));
			}
		}
		message = message.replaceAll("%gamestate%", ChatColor.translateAlternateColorCodes('&', formatJoinMessage(messages.getConfigurationSection("gamestates").getString(GameStateManager.GameStateText), player, count)));
		message = message.replaceAll("%h%", ChatColor.translateAlternateColorCodes('&', String.valueOf(hours)));
		message = message.replaceAll("%m%", ChatColor.translateAlternateColorCodes('&', String.valueOf(minutes)));
		
		message = message.replaceAll("%s%", ChatColor.translateAlternateColorCodes('&', String.valueOf(seconds)));
		return message;
	}

	public static String formatScoreboardSpec(String message, Player player, int x,int y,int z) {
		message = message.replaceAll("%prefix%", ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("commands").getString("prefix")));
		message = message.replaceAll("%player%", ChatColor.translateAlternateColorCodes('&', player.getName()));
		if(GameStateManager.GameState == true) {
			CastleTeam team1 = new CastleTeam("team1");
			CastleTeam team2 = new CastleTeam("team2");
			message = message.replaceAll("%team1color%", ChatColor.translateAlternateColorCodes('&', team1.getColor()));
			message = message.replaceAll("%team1name%", ChatColor.translateAlternateColorCodes('&', team1.getName()));
			message = message.replaceAll("%team1prefix%", ChatColor.translateAlternateColorCodes('&', team1.getPrefix()));
			message = message.replaceAll("%king1name%", ChatColor.translateAlternateColorCodes('&', team1.getKing().getName()));
			message = message.replaceAll("%king1health%", ChatColor.translateAlternateColorCodes('&', String.valueOf((int)team1.getKing().getHealth())));
			message = message.replaceAll("%team2color%", ChatColor.translateAlternateColorCodes('&', team2.getColor()));
			message = message.replaceAll("%team2name%", ChatColor.translateAlternateColorCodes('&', team2.getName()));
			message = message.replaceAll("%team2prefix%", ChatColor.translateAlternateColorCodes('&', team2.getPrefix()));
			message = message.replaceAll("%king2name%", ChatColor.translateAlternateColorCodes('&', team2.getKing().getName()));
			message = message.replaceAll("%king2health%", ChatColor.translateAlternateColorCodes('&', String.valueOf((int)team2.getKing().getHealth())));
		}
		message = message.replaceAll("%gamestate%", ChatColor.translateAlternateColorCodes('&', formatJoinMessage(messages.getConfigurationSection("gamestates").getString(GameStateManager.GameStateText), player, 0)));
		message = message.replaceAll("%h%", ChatColor.translateAlternateColorCodes('&', String.valueOf(x)));
		message = message.replaceAll("%m%", ChatColor.translateAlternateColorCodes('&', String.valueOf(y)));
		message = message.replaceAll("%s%", ChatColor.translateAlternateColorCodes('&', String.valueOf(z)));
		return message;
	}
	
}
