package fr.ezzud.castlewar.api;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import fr.ezzud.castlewar.Main;

public class TeamManager {
	static Main plugin = Main.getInstance();
	ConfigurationSection team1Info = plugin.getConfig().getConfigurationSection("team1");
	ConfigurationSection team2Info = plugin.getConfig().getConfigurationSection("team2");
	private static CastleTeam team1;
	private static CastleTeam team2;
	YamlConfiguration teams = Main.teams;
	public TeamManager() {
		team1 = new CastleTeam("team1");
		team2 = new CastleTeam("team2");
	}
	
	public static CastleTeam getTeam1() {
		return team1;		
	}
	
	public static CastleTeam getTeam2() {
		return team2;		
	}
	

	
}
