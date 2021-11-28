package fr.ezzud.castlewar.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.methods.CountdownTimer;
import fr.ezzud.castlewar.methods.RandomUtil;
import fr.ezzud.castlewar.methods.configManager;
import fr.ezzud.castlewar.methods.effectManager;
import fr.ezzud.castlewar.methods.inATeam;
import fr.ezzud.castlewar.methods.kitManager;
import fr.ezzud.castlewar.methods.worldManager;
import net.md_5.bungee.api.ChatColor;

public class GameStateManager {
	public static boolean GameState;
	public Main plugin = Main.getInstance();
	public static String team1King;
	public static String team2King;
	public static Player king1Player;
	public static Player king2Player;
	public static boolean createParticles;
	public static boolean getGameState() {
		return GameState;
	}
	public static String getTeam1King() {
		return team1King;
	}
	public static String getTeam2King() {
		return team2King;
	}
	@SuppressWarnings("deprecation")
	public void stopGame() {
		Object[] array = Bukkit.getOnlinePlayers().toArray();
		GameStateManager.GameState = false;
		king1Player = null;
		king2Player = null;
		team1King = null;
		team2King = null;
		TeamManager.clearTeam("team1");
		TeamManager.clearTeam("team2");
		new TeamManager();
		for(int i = 0; i < array.length; i++) {
			Player player = (Player) array[i];
    		
    		String[] coordsStr = plugin.getConfig().getString("lobby_spawnpoint").split(",");
    		Location coords = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world")), Double.parseDouble(coordsStr[0]), Double.parseDouble(coordsStr[1]), Double.parseDouble(coordsStr[2]), Float.parseFloat(coordsStr[3]), Float.parseFloat(coordsStr[4]));
        	for (Object cItem : player.getActivePotionEffects()) {
	               String potionEffectName = cItem.toString().split(":")[0];
	               PotionEffectType effect = PotionEffectType.getByName(potionEffectName);
	               player.removePotionEffect(effect);
	        }
        		player.teleport(coords);
        		player.setBedSpawnLocation(coords);
        		player.getInventory().clear();
			   	player.setHealth(20.0);
			   	player.setFoodLevel(20);
			   	player.setSaturation(20);
			   	player.setLevel(0);
			   	player.setExp(0);
			   	player.setGameMode(GameMode.valueOf(plugin.getConfig().getString("spawnGamemode")));
			   	player.setDisplayName(player.getName());
			   	player.setPlayerListName(player.getName());
			   	player.setStatistic(Statistic.TIME_SINCE_REST, 0);
			   	
			   	ConfigurationSection teamItem = plugin.getConfig().getConfigurationSection("teamChooseItem");
			   	if(teamItem.getBoolean("enabled") == true) {
	  			   	String[] itemInfo = teamItem.getString("item").split(",");
	        		final ItemStack item = new ItemStack(Material.valueOf(itemInfo[0]), 1, Byte.parseByte(itemInfo[1]));
	                final ItemMeta meta = item.getItemMeta();     
	                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemInfo[2]));
	                item.setItemMeta(meta);
	        		player.getInventory().setItem(teamItem.getInt("slot"), item);			   		
			   	}

			   	
			   	ConfigurationSection kitItem = plugin.getConfig().getConfigurationSection("kitChooseItem");
			   	if(kitItem.getBoolean("enabled") == true) {
	  			   	String[] itemInfo = kitItem.getString("item").split(",");
	        		final ItemStack item = new ItemStack(Material.valueOf(itemInfo[0]), 1, Byte.parseByte(itemInfo[1]));
	                final ItemMeta meta = item.getItemMeta();     
	                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemInfo[2]));
	                item.setItemMeta(meta);
	        		player.getInventory().setItem(kitItem.getInt("slot"), item);			   		
			   	}
			   	
			   	
				File f = new File(plugin.getConfig().getString("game_world") + "-castlewar");
				if(f.exists()) {
					worldManager.unloadWorld(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"));
					try {
						Path path = Paths.get(plugin.getConfig().getString("game_world") + "-castlewar");
						worldManager.deleteDirectoryStream(path);
					} catch (IOException e) {
						e.printStackTrace();
					}		
				}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void startGame() {
		Scoreboard board = Main.board;
		YamlConfiguration kitData = Main.data;
		File f = new File(plugin.getConfig().getString("game_world") + "-castlewar");
		if(!f.exists()) {
			worldManager.copyWorld(Bukkit.getWorld(plugin.getConfig().getString("game_world")), plugin.getConfig().getString("game_world") + "-castlewar");
		}
		GameState = true;
		Bukkit.broadcastMessage("Game Started!");
		Object[] array = Bukkit.getOnlinePlayers().toArray();
		if(array.length < plugin.getConfig().getInt("minPlayers")) {
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "Not enough player to start the game"));
			return;
		}
		new TeamManager();
		for(int i = 0; i < array.length; i++) {
			Player player = (Player) array[i];
			player.getInventory().clear();
			player.getActivePotionEffects().clear();
			Bukkit.getLogger().info("Choosing team of " + player.getName());
			List<String> team1 = new ArrayList<>();
			List<String> team2 = new ArrayList<>();

			for(OfflinePlayer pl : board.getTeam("team1").getPlayers()) {
				team1.add(pl.getName());
			}
			for(OfflinePlayer pl : board.getTeam("team2").getPlayers()) {
				team2.add(pl.getName());
			}
			if(inATeam.checkTeam(player.getName()) == false) {
				
				if(team1.size() - 1 > team2.size() - 1) {
		    		TeamManager.addMemberToTeam(player, "team2");
	    			player.sendMessage("You joined the team 2");
	    			
	    			
				} else if(team1.size() - 1 < team2.size() - 1) {
					TeamManager.addMemberToTeam(player, "team1");
	    			player.sendMessage("You joined the team 1");	
	    			
	    			
				} else {
					TeamManager.addMemberToTeam(player, "team1");
	    			player.sendMessage("You joined the team 1");	
	    			
	    			
				}
				
			}
			player.setGameMode(GameMode.SURVIVAL);
		}
		team1King = RandomUtil.RandomPlayer(board.getTeam("team1").getEntries());
		team2King = RandomUtil.RandomPlayer(board.getTeam("team2").getEntries());
		createParticles = true;
		king1Player = Bukkit.getPlayer(team1King);
		king2Player = Bukkit.getPlayer(team2King);
		effectManager em = new effectManager();
		em.createSpiralAroundPlayer(king1Player);
		em.createSpiralAroundPlayer(king2Player);
		em.applyGlowing(king1Player);
		em.applyGlowing(king2Player);
		for(int i = 0; i < array.length; i++) {
			Player player = (Player) array[i];
			String king_rank = plugin.getConfig().getString("king_rank");
			String soldier_rank = plugin.getConfig().getString("soldier_rank");
			ConfigurationSection team1Config = plugin.getConfig().getConfigurationSection("team1");
			ConfigurationSection team2Config = plugin.getConfig().getConfigurationSection("team2");
			
			if(player.getName().equalsIgnoreCase(team1King)) {
				player.sendMessage("You are the king 1!");
				if(plugin.getConfig().getString("rankType").equalsIgnoreCase("prefix")) {
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', king_rank + player.getDisplayName()));
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', king_rank + player.getDisplayName()));
				} else if(plugin.getConfig().getString("rankType").equalsIgnoreCase("suffix")) {
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + king_rank));
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + king_rank));
				}
				String[] coords = team1Config.getString("king_spawnpoint").split(",");
				Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				player.teleport(loc);
				new kitManager().setKingKit(player);
				
				
			} else if(player.getName().equalsIgnoreCase(team2King)) {
				player.sendMessage("You are the king 2!");
				if(plugin.getConfig().getString("rankType").equalsIgnoreCase("prefix")) {
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', king_rank + player.getDisplayName()));
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', king_rank + player.getDisplayName()));
					
				} else if(plugin.getConfig().getString("rankType").equalsIgnoreCase("suffix")) {
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + king_rank));
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + king_rank));
				}
				String[] coords = team2Config.getString("king_spawnpoint").split(",");
				Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				player.teleport(loc);
				new kitManager().setKingKit(player);
	           
			} else {
				if(plugin.getConfig().getString("rankType").equalsIgnoreCase("prefix")) {
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', soldier_rank + player.getDisplayName()));
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', soldier_rank + player.getDisplayName()));
					
				} else if(plugin.getConfig().getString("rankType").equalsIgnoreCase("suffix")) {
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + soldier_rank ));
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + soldier_rank ));
					
				}	
				if(inATeam.whichTeam(player.getName()).equalsIgnoreCase("team1")) {
					String[] coords = team1Config.getString("soldier_spawnpoint").split(",");
					Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
					player.teleport(loc);
				} else if(inATeam.whichTeam(player.getName()).equalsIgnoreCase("team2")) {
					String[] coords = team2Config.getString("soldier_spawnpoint").split(",");
					Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
					player.teleport(loc);
				}


				
				if(kitData.getKeys(false).contains(String.valueOf(player.getUniqueId())) == false) {
					kitData.set(String.valueOf(player.getUniqueId()), plugin.getConfig().getString("default_kit"));
	    			try {
						kitData.save(new File("plugins/CastleWars/data.yml"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	    			Main.data = configManager.getData();
				}
				new kitManager().setPlayerKit(player);
				
				

				
			}
		}
		
		
	}
	
	public void checkStart() {
		Bukkit.getLogger().info("Checking");
		if(Bukkit.getOnlinePlayers().size() >= plugin.getConfig().getInt("minPlayers")) {
			Bukkit.getLogger().info("More");
			if(Main.starting == false) {
				Bukkit.getLogger().info("Not Starting");
		    	  CountdownTimer timer = new CountdownTimer(plugin,
		    		        60,
		    		        () ->  {
		    		        	Main.starting = true;
		    		        	Bukkit.broadcastMessage("Starting in 60...");
		    		        },
		    		        () -> {    
		    		        	Bukkit.broadcastMessage("Started"); 
		    		        	new GameStateManager().startGame();
		    		        	for(Player p : Bukkit.getOnlinePlayers()) {
		    		        		Player pl = Bukkit.getPlayer(p.getName());
		    		        		pl.setLevel(0);
		    		        	}	
		    		        	Main.starting = false;
		    		        	CountdownTimer.cancelTimer();
		    		        },
		    		        (t) -> {
		    		        	if(GameStateManager.GameState == true) {
		    		        		
		    		        		Main.starting = false;
			    		        	for(Player p : Bukkit.getOnlinePlayers()) {
	  		    		        		p.setLevel(0);
				    		        }
			    		        	CountdownTimer.cancelTimer();
		    		        	}
		    		        	if(Bukkit.getOnlinePlayers().size() < plugin.getConfig().getInt("minPlayers")) {
		    		        		CountdownTimer.cancelTimer();
		    		        		Bukkit.broadcastMessage("Stopped");
		    		        		Main.starting = false;
			    		        	for(Player p : Bukkit.getOnlinePlayers()) {
			    		        		p.setLevel(0);
			    		        	}
		    		        	}
		    		        	if(Bukkit.getOnlinePlayers().size() == plugin.getConfig().getInt("maxPlayers")) {
		    		        		t.reduceTimer(10);
		    		        	}
		    		        	for(Player p : Bukkit.getOnlinePlayers()) {
		    		        		p.setLevel(t.getSecondsLeft());
		    		        	}
		    		        }

		    		);
		    	  timer.scheduleTimer();     				
			}

		}
	}
}
