package fr.ezzud.castlewar.api;

import fr.ezzud.castlewar.Main;

public class TeamManager {
	static Main plugin = Main.getInstance();
	private CastleTeam team1;
	private CastleTeam team2;
	public TeamManager() {
		this.team1 = new CastleTeam("team1");
		this.team2 = new CastleTeam("team2");
	}
	
	public CastleTeam getTeam1() {
		return this.team1;		
	}
	
	public CastleTeam getTeam2() {
		return this.team2;		
	}
	
}
