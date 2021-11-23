package fr.ezzud.castlewar.api;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.methods.RandomUtil;
import fr.ezzud.castlewar.methods.configManager;
import fr.ezzud.castlewar.methods.inATeam;
import net.md_5.bungee.api.ChatColor;

public class GameStateManager {
	public static boolean GameState;
	public Main plugin = Main.getInstance();
	public static String team1King;
	public static String team2King;
	public static Player king1Player;
	public static Player king2Player;
	public static boolean getGameState() {
		return GameState;
	}
	public static String getTeam1King() {
		return team1King;
	}
	public static String getTeam2King() {
		return team2King;
	}
	public void startGame() {
		
		YamlConfiguration team = Main.teams;
		GameState = true;
		Bukkit.broadcastMessage("Game Started!");
		Object[] array = Bukkit.getOnlinePlayers().toArray();
		new TeamManager();
		for(int i = 0; i < array.length; i++) {
			Player player = (Player) array[i];
			Bukkit.getLogger().info("Choosing team of " + player.getName());
			List<String> team1 = CastleTeam.getMembers("team1");
			List<String> team2 = CastleTeam.getMembers("team2");
			
			if(inATeam.checkTeam(player.getName()) == false) {
				
				if(team1.size() - 1 > team2.size() - 1) {
		    		List<String> lst = team.getStringList("team2"); 
		    		lst.add(player.getName());
		    		team.set("team2", lst);
		    		try {
		    			team.save(new File("plugins/CastleWars/teams.yml"));
		    		} catch (IOException e1) {
		    			e1.printStackTrace();
		    		}
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam2().getColor() + player.getName() + "&r"));
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam2().getColor() + player.getName() + "&r"));
	    			player.sendMessage("You joined the team 2");
	    			
	    			
				} else if(team1.size() - 1 < team2.size() - 1) {
		    		List<String> lst = team.getStringList("team1"); 
		    		lst.add(player.getName());
		    		team.set("team1", lst);
		    		try {
		    			team.save(new File("plugins/CastleWars/teams.yml"));
		    		} catch (IOException e1) {
		    			e1.printStackTrace();
		    		}
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getColor() + player.getName() + "&r"));
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getColor() + player.getName() + "&r"));
	    			player.sendMessage("You joined the team 1");	
	    			
	    			
				} else {
		    		List<String> lst = team.getStringList("team1"); 
		    		lst.add(player.getName());
		    		team.set("team1", lst);
		    		try {
		    			team.save(new File("plugins/CastleWars/teams.yml"));
		    		} catch (IOException e1) {
		    			e1.printStackTrace();
		    		}
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getColor() + player.getName() + "&r"));
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getColor() + player.getName() + "&r"));
	    			player.sendMessage("You joined the team 1");	
	    			
	    			
				}
				
			}
		}
		Main.teams = configManager.getTeams();
		team1King = RandomUtil.RandomPlayer(Main.teams.getStringList("team1"));
		team2King = RandomUtil.RandomPlayer(Main.teams.getStringList("team2"));
		king1Player = Bukkit.getPlayer(team1King);
		king2Player = Bukkit.getPlayer(team2King);
		for(int i = 0; i < array.length; i++) {
			Player player = (Player) array[i];
			String king_rank = plugin.getConfig().getString("king_rank");
			String soldier_rank = plugin.getConfig().getString("soldier_rank");
			ConfigurationSection team1Config = plugin.getConfig().getConfigurationSection("team1");
			ConfigurationSection team2Config = plugin.getConfig().getConfigurationSection("team2");
			
			if(player.getName().equalsIgnoreCase(team1King)) {
				player.sendMessage("You are the king 1!");
				if(plugin.getConfig().getString("rankType").equalsIgnoreCase("prefix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', king_rank + player.getDisplayName()));
				} else if(plugin.getConfig().getString("rankType").equalsIgnoreCase("suffix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + king_rank ));
				}
				String[] coords = team1Config.getString("king_spawnpoint").split(",");
				Location loc = new Location(Bukkit.getWorld(coords[5]), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				player.teleport(loc);
				
			} else if(player.getName().equalsIgnoreCase(team2King)) {
				player.sendMessage("You are the king 2!");
				if(plugin.getConfig().getString("rankType").equalsIgnoreCase("prefix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', king_rank + player.getDisplayName()));
				} else if(plugin.getConfig().getString("rankType").equalsIgnoreCase("suffix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + king_rank ));
				}
				String[] coords = team2Config.getString("king_spawnpoint").split(",");
				Location loc = new Location(Bukkit.getWorld(coords[5]), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				player.teleport(loc);
			} else {
				if(plugin.getConfig().getString("rankType").equalsIgnoreCase("prefix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', soldier_rank + player.getDisplayName()));
				} else if(plugin.getConfig().getString("rankType").equalsIgnoreCase("suffix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + soldier_rank ));
				}	
				if(inATeam.whichTeam(player.getName()).equalsIgnoreCase("team1")) {
					String[] coords = team1Config.getString("soldier_spawnpoint").split(",");
					Location loc = new Location(Bukkit.getWorld(coords[5]), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
					player.teleport(loc);
				} else if(inATeam.whichTeam(player.getName()).equalsIgnoreCase("team2")) {
					String[] coords = team2Config.getString("soldier_spawnpoint").split(",");
					Location loc = new Location(Bukkit.getWorld(coords[5]), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
					player.teleport(loc);
				}
			}
		}
		
		
	}
}
