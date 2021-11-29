package fr.ezzud.castlewar.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
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
	private String ident;
	static Main plugin = Main.getInstance();
	static YamlConfiguration teamMembers = Main.teams;
	
	public CastleTeam(String team) {
		this.ident = team;
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

	public Player getKing() {
		if(this.ident.equalsIgnoreCase("team1")) {
			return GameStateManager.king1Player;
		} else {
			return GameStateManager.king2Player;
		}
	}
	
	public List<String> getMembers() {
		Scoreboard board = Main.board;
		Team tm = board.getTeam(this.ident);
		List<String> list = new ArrayList<>();
		for(String pl : tm.getEntries()) {
			list.add(pl);
		}
		return list;
	}
	
}
