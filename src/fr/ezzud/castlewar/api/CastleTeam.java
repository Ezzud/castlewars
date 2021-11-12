package fr.ezzud.castlewar.api;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import fr.ezzud.castlewar.Main;

public class CastleTeam {
	private String name;
	private ConfigurationSection team;
	private String color;
	private String soldier_spawnpoint;
	private String king_spawnpoint;
	private List<String> soldiers;
	private String king;
	static Main plugin = Main.getInstance();
	static FileConfiguration teams = Main.getTeamFile();
	
	public CastleTeam(String team) {
		this.team = (ConfigurationSection) plugin.getConfig().getConfigurationSection(team);
		this.name = this.team.getString("name");
		this.color = this.team.getString("color");
		this.soldier_spawnpoint = this.team.getString("soldier_spawnpoint");
		this.king_spawnpoint = this.team.getString("king_spawnpoint");
		this.soldiers = teams.getStringList(team);
		this.king = teams.getString(team + "_king");
	}
	
	public String getName() {
		return this.name;
	}

	public String getColor() {
		return this.color;
	}
	
	public String getSoldierSpawnPoint() {
		return this.soldier_spawnpoint;
	}
	
	public String getKingSpawnPoint() {
		return this.king_spawnpoint;
	}

	public String getKing() {
		return this.king;
	}

	public List<String> getSoldiers() {
		return this.soldiers;
	}
	
}
