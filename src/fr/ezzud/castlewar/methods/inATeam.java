package fr.ezzud.castlewar.methods;


import java.util.List;

import fr.ezzud.castlewar.api.CastleTeam;
public class inATeam {
	public static boolean checkTeam(String player) {
		
		if(CastleTeam.getMembers("team1") != null) {
			List<String> team1Members = CastleTeam.getMembers("team1");
			if(team1Members.contains(player)) {
				return true;
			}			
		}
		if(CastleTeam.getMembers("team2") != null) {
			List<String> team2Members = CastleTeam.getMembers("team2");
			if(team2Members.contains(player)) {
				return true;
			}		
		}

		
		return false;
	}
	
	public static boolean checkSpecificTeam(String player, String team) {
		if(CastleTeam.getMembers(team) != null) {
			List<String> teamMembers = CastleTeam.getMembers(team);
			if(teamMembers.contains(player)) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}	
	}
	
	
	public static String whichTeam(String player) {
		if(CastleTeam.getMembers("team1") != null) {
			List<String> team1Members = CastleTeam.getMembers("team1");
			if(team1Members.contains(player)) {
				return "team1";
			}			
		}
		if(CastleTeam.getMembers("team2") != null) {
			List<String> team2Members = CastleTeam.getMembers("team2");
			if(team2Members.contains(player)) {
				return "team2";
			}		
		}

		
		return null;
	}
}
