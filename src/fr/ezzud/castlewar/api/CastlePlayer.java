package fr.ezzud.castlewar.api;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.methods.inATeam;

public class CastlePlayer {
	public YamlConfiguration data = Main.data;
	private Player player;
	private boolean isInATeam;
	private boolean isAKing;
	private String team;
	private String kit;
	private String name;
	public CastlePlayer(String pl) {
		this.player = Bukkit.getPlayer(pl);
		this.isInATeam = inATeam.checkTeam(pl);
		this.isAKing = inATeam.isKing(pl);
		this.team = inATeam.whichTeam(pl);
		this.kit = data.getString(String.valueOf(Bukkit.getPlayer(pl).getUniqueId()));
		this.name = pl;			
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public String getTeam() {
		if(this.isInATeam == true) {
			return this.team;
		} else {
			return null;
		}
	}
	
	public boolean isKing() {
		return this.isAKing;
	}
	
	public String getKit() {
		return this.kit;
	}
	
	public String getName() {
		return this.name;
	}
}
