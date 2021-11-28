package fr.ezzud.castlewar.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.ezzud.castlewar.Main;

public class CastleTeam {
	private String name;
	private ConfigurationSection team;
	private String color;
	private String soldier_spawnpoint;
	private String king_spawnpoint;
	private String prefix;
	static Main plugin = Main.getInstance();
	static YamlConfiguration teamMembers = Main.teams;
	
	public CastleTeam(String team) {
		this.team = plugin.getConfig().getConfigurationSection(team);
		this.name = this.team.getString("name");
		this.color = this.team.getString("color");
		this.prefix = this.team.getString("prefix");
		this.soldier_spawnpoint = this.team.getString("soldier_spawnpoint");
		this.king_spawnpoint = this.team.getString("king_spawnpoint");
	}
	
	public String getName() {
		return this.name;
	}

	public String getColor() {
		return this.color;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	public String getSoldierSpawnPoint() {
		return this.soldier_spawnpoint;
	}
	
	public String getKingSpawnPoint() {
		return this.king_spawnpoint;
	}

	public static List<String> getMembers(String team) {
		Scoreboard board = Main.board;
		Team tm = board.getTeam(team);
		List<String> list = new ArrayList<>();
		for(String pl : tm.getEntries()) {
			list.add(pl);
		}
		return list;
	}
	
}
