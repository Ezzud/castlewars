package fr.ezzud.castlewar.methods;


import java.util.Set;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;
public class inATeam {
	static Scoreboard board = Main.board;
	static Main plugin = Main.getInstance();
	public static boolean checkTeam(String player) {

		Team team1 = board.getTeam("team1");
		if(team1 == null) return false;
		Team team2 = board.getTeam("team2");
		if(team2 == null) return false;
		if(team1.getEntries() != null) {
			Set<String> team1Members = team1.getEntries() ;
			if(team1Members.contains(player)) {
				return true;
			}			
		}
		if(team2.getEntries() != null) {
			Set<String> team2Members = team2.getEntries() ;
			if(team2Members.contains(player)) {
				return true;
			}			
		}

		
		return false;
	}
	
	public static boolean checkSpecificTeam(String player, String team) {
		Team team1 = board.getTeam(team);
		if(team1.getEntries() != null) {
			Set<String> team1Members = team1.getEntries() ;
			if(team1Members.contains(player)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}	
	}
	
	
	public static String whichTeam(String player) {
		Scoreboard board = Main.board;
		Team team1 = board.getTeam("team1");
		Team team2 = board.getTeam("team2");
		if(team1.getEntries() != null) {
			Set<String> team1Members = team1.getEntries() ;
			if(team1Members.contains(player)) {
				return "team1";
			}			
		}
		if(team2.getEntries() != null) {
			Set<String> team2Members = team2.getEntries() ;
			if(team2Members.contains(player)) {
				return "team2";
			}			
		}

		
		return null;
	}
	
	public static boolean isKing(String player) {
		if(player.equalsIgnoreCase(GameStateManager.team1King)) {
			return true;
		}
		if(player.equalsIgnoreCase(GameStateManager.team2King)) {
			return true;
		}
		return false;
	}
}
