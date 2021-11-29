package fr.ezzud.castlewar.api;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import com.nametagedit.plugin.NametagEdit;
import fr.ezzud.castlewar.Main;

public class TeamManager {
	static Main plugin = Main.getInstance();
	ConfigurationSection team1Info = plugin.getConfig().getConfigurationSection("team1");
	ConfigurationSection team2Info = plugin.getConfig().getConfigurationSection("team2");
	private static CastleTeam team1;
	private static CastleTeam team2;
	private static CastleTeam noteam;
	
	YamlConfiguration teams = Main.teams;
	public TeamManager() {
		team1 = new CastleTeam("team1");
		team2 = new CastleTeam("team2");
		noteam = new CastleTeam("noTeam");
	}
	
	public static CastleTeam getTeam1() {
		return team1;		
	}
	
	public static CastleTeam getTeam2() {
		return team2;		
	}
	
	public static CastleTeam getNoTeam() {
		return noteam;		
	}
	
	public void initializeTeams() {
		Scoreboard board = Main.board;	
		for(Team tm : board.getTeams()) {
			tm.unregister();
		}

		
		Team team1 = board.registerNewTeam("team1");
		team1.addEntry("team1");
		team1.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OTHER_TEAMS);
		team1.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
		team1.setAllowFriendlyFire(false);
		
		Team team2 = board.registerNewTeam("team2");
		team2.addEntry("team2");
		team2.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);
		team2.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.FOR_OTHER_TEAMS);
		team2.setAllowFriendlyFire(false);
	}
	
	
	public static void addMemberToTeam(Player player, String team) {
		Scoreboard board = Main.board;
		if(board.getTeam(team) != null) {
			Team team_ = board.getTeam(team);
			team_.addEntry(player.getName());
			if(team.equals("team1")) {
				
				player.setDisplayName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getPrefix() + player.getName()));
				if(Bukkit.getServer().getPluginManager().getPlugin("NameTagEdit") != null) {
					NametagEdit.getApi().setPrefix(player, TeamManager.getTeam1().getPrefix());
				} else {
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getPrefix() + player.getName()));
				}
				
			} else {
				
				player.setDisplayName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam2().getPrefix() + player.getName()));
				if(Bukkit.getServer().getPluginManager().getPlugin("NameTagEdit") != null) {
					NametagEdit.getApi().setPrefix(player, TeamManager.getTeam2().getPrefix());
				} else {
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam2().getPrefix() + player.getName()));
				}
			}
			
		}
	}
	
	public static void removeMemberFromTeam(Player player, String team) {
		Scoreboard board = Main.board;
		if(board.getTeam(team) != null) {
			Team team_ = board.getTeam(team);
			team_.removeEntry(player.getName());
			
			player.setDisplayName(player.getName());
			if(Bukkit.getServer().getPluginManager().getPlugin("NameTagEdit") != null) {
				NametagEdit.getApi().clearNametag(player);
			} else {
				player.setPlayerListName(player.getName());
			}
		}
	}

	
	public static void clearTeam(String team) {
		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard board = manager.getNewScoreboard();
		if(board.getTeam(team) != null) {
			for(String i: board.getTeam(team).getEntries()) {
				if(Bukkit.getServer().getPluginManager().getPlugin("NameTagEdit") != null) {
					NametagEdit.getApi().clearNametag(Bukkit.getPlayer(i));
				}
			}
			board.getTeam(team).unregister();	
			board.registerNewTeam(team);
		}

	}
	
}
