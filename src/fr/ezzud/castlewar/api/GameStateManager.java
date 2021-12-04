package fr.ezzud.castlewar.api;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.Scoreboard;

import com.nametagedit.plugin.NametagEdit;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.events.CWstartEvent;
import fr.ezzud.castlewar.methods.RandomUtil;
import fr.ezzud.castlewar.methods.inATeam;
import fr.ezzud.castlewar.methods.messagesFormatter;
import fr.ezzud.castlewar.methods.countdowns.CountdownTimer;
import fr.ezzud.castlewar.methods.managers.configManager;
import fr.ezzud.castlewar.methods.managers.effectManager;
import fr.ezzud.castlewar.methods.managers.kitManager;
import fr.ezzud.castlewar.methods.managers.worldManager;
import net.md_5.bungee.api.ChatColor;

public class GameStateManager {
	public static boolean GameState;
	public Main plugin = Main.getInstance();
	YamlConfiguration messages = Main.messages;
	public static String GameStateText;
	public static String team1King;
	public static String team2King;
	public static Player king1Player;
	public static Player king2Player;
	public static boolean createParticles;
	public static Date startDate;
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
		GameStateText = "waiting";
		GameStateManager.createParticles = false;
		startDate = null;
		king1Player = null;
		king2Player = null;
		team1King = null;
		team2King = null;
		new TeamManager().initializeTeams();
		new TeamManager();
		checkStart();
		for(int i = 0; i < array.length; i++) {
			Player player = (Player) array[i];
    		
    		String[] coordsStr = plugin.getConfig().getString("lobby_spawnpoint").split(",");
    		Location coords = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world")), Double.parseDouble(coordsStr[0]), Double.parseDouble(coordsStr[1]), Double.parseDouble(coordsStr[2]), Float.parseFloat(coordsStr[3]), Float.parseFloat(coordsStr[4]));
        	for (Object cItem : player.getActivePotionEffects()) {
	               String potionEffectName = cItem.toString().split(":")[0];
	               PotionEffectType effect = PotionEffectType.getByName(potionEffectName);
	               player.removePotionEffect(effect);
	        }
        		player.setGameMode(GameMode.valueOf(plugin.getConfig().getString("spawnGamemode")));
        		player.teleport(coords);
        		player.setBedSpawnLocation(coords);
        		player.getInventory().clear();
			   	player.setHealth(20.0);
			   	player.setFoodLevel(20);
			   	player.setSaturation(20);
			   	player.setLevel(0);
			   	player.setExp(0);
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
					Bukkit.unloadWorld(Bukkit.getWorld(plugin.getConfig().getString("game_world")  + "-castlewar"), false);
					try {
						Path path = Paths.get(plugin.getConfig().getString("game_world") + "-castlewar");
						worldManager.deleteDirectoryStream(path);
					} catch (IOException e) {
						e.printStackTrace();
					}	
					
					
				}
		}
	}
	
	public void startGame() {
		Scoreboard board = Main.board;
		YamlConfiguration kitData = Main.data;
		File f = new File(plugin.getConfig().getString("game_world") + "-castlewar");
		if(!f.exists()) {
			worldManager.copyWorld(Bukkit.getWorld(plugin.getConfig().getString("game_world")), plugin.getConfig().getString("game_world") + "-castlewar");
		}
		GameStateText = "playing";
		Object[] array = Bukkit.getOnlinePlayers().toArray();
		if(array.length < plugin.getConfig().getInt("minPlayers")) {
			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("commands.start").getString("notEnough"))));
			return;
		}
		new TeamManager();
		for(int i = 0; i < array.length; i++) {
			Player player = (Player) array[i];
			player.getInventory().clear();
			player.getActivePotionEffects().clear();
			List<String> team1 = new ArrayList<>();
			List<String> team2 = new ArrayList<>();

			for(String pl : board.getTeam("team1").getEntries()) {
				team1.add(pl);
			}
			for(String pl : board.getTeam("team2").getEntries()) {
				team2.add(pl);
			}
			if(inATeam.checkTeam(player.getName()) == false) {
				
				if(team1.size() - 1 > team2.size() - 1) {
		    		TeamManager.addMemberToTeam(player, "team2");
					String msg = messagesFormatter.formatTeamMessage(messages.getConfigurationSection("events.teamChange").getString("join"), new CastleTeam("team2"));
		    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	    			
	    			
				} else if(team1.size() - 1 < team2.size() - 1) {
					TeamManager.addMemberToTeam(player, "team1");
					String msg = messagesFormatter.formatTeamMessage(messages.getConfigurationSection("events.teamChange").getString("join"), new CastleTeam("team1"));
		    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	    			
	    			
				} else {
					TeamManager.addMemberToTeam(player, "team1");
					String msg = messagesFormatter.formatTeamMessage(messages.getConfigurationSection("events.teamChange").getString("join"), new CastleTeam("team1"));
		    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
	    			
	    			
				}
				
			}
			player.setGameMode(GameMode.SURVIVAL);
		}
		team1King = RandomUtil.RandomPlayer(board.getTeam("team1").getEntries());
		team2King = RandomUtil.RandomPlayer(board.getTeam("team2").getEntries());
		createParticles = true;
		startDate = new Date();
		king1Player = Bukkit.getPlayer(team1King);
		king2Player = Bukkit.getPlayer(team2King);
		GameState = true;
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
				List<String> msgList = new ArrayList<>();
				for(String i1 : messages.getConfigurationSection("events.starting").getStringList("kingMessage")) {
					msgList.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatTeamMessage(i1 , new CastleTeam("team1"))));
				}
				String[] strarray = new String[msgList.size()];
				msgList.toArray(strarray);
	    		player.sendMessage(strarray);
				if(plugin.getConfig().getString("rankType").equalsIgnoreCase("prefix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', king_rank + player.getDisplayName()));
					if(Bukkit.getServer().getPluginManager().getPlugin("NameTagEdit") != null) {
						NametagEdit.getApi().setPrefix(team1King, ChatColor.translateAlternateColorCodes('&', king_rank) + NametagEdit.getApi().getNametag(king1Player).getPrefix());
					} else {
						player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', king_rank + player.getDisplayName()));
					}
					
					
				} else if(plugin.getConfig().getString("rankType").equalsIgnoreCase("suffix")) {
					
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + king_rank));
					if(Bukkit.getServer().getPluginManager().getPlugin("NameTagEdit") != null) {
						NametagEdit.getApi().setSuffix(team1King, ChatColor.translateAlternateColorCodes('&', king_rank));
					} else {
						player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + king_rank));
					}
				}
				String[] coords = team1Config.getString("king_spawnpoint").split(",");
				Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				player.teleport(loc);
				new kitManager().setKingKit(player);
				
				
			} else if(player.getName().equalsIgnoreCase(team2King)) {
				List<String> msgList = new ArrayList<>();
				for(String i1 : messages.getConfigurationSection("events.starting").getStringList("kingMessage")) {
					msgList.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatTeamMessage(i1 , new CastleTeam("team2"))));
				}
				String[] strarray = new String[msgList.size()];
				msgList.toArray(strarray);
	    		player.sendMessage(strarray);
				if(plugin.getConfig().getString("rankType").equalsIgnoreCase("prefix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', king_rank + player.getDisplayName()));
					if(Bukkit.getServer().getPluginManager().getPlugin("NameTagEdit") != null) {
						NametagEdit.getApi().setPrefix(team1King, ChatColor.translateAlternateColorCodes('&', king_rank) + NametagEdit.getApi().getNametag(king2Player).getPrefix());
					} else {
						player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', king_rank + player.getDisplayName()));
					}
				} else if(plugin.getConfig().getString("rankType").equalsIgnoreCase("suffix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + king_rank));
					if(Bukkit.getServer().getPluginManager().getPlugin("NameTagEdit") != null) {
						NametagEdit.getApi().setSuffix(team2King, ChatColor.translateAlternateColorCodes('&', king_rank));
					} else {
						player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + king_rank));
					}
				}
				String[] coords = team2Config.getString("king_spawnpoint").split(",");
				Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				player.teleport(loc);
				new kitManager().setKingKit(player);
	           
			} else {
				if(plugin.getConfig().getString("rankType").equalsIgnoreCase("prefix")) {
					
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', soldier_rank + player.getDisplayName()));
					if(Bukkit.getServer().getPluginManager().getPlugin("NameTagEdit") != null) {
						NametagEdit.getApi().setPrefix(team1King, ChatColor.translateAlternateColorCodes('&', soldier_rank) + NametagEdit.getApi().getNametag(player).getPrefix());
					} else {
						player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', soldier_rank + player.getDisplayName()));
					}
					
				} else if(plugin.getConfig().getString("rankType").equalsIgnoreCase("suffix")) {
					
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + soldier_rank ));
					if(Bukkit.getServer().getPluginManager().getPlugin("NameTagEdit") != null) {
						NametagEdit.getApi().setSuffix(player, ChatColor.translateAlternateColorCodes('&', soldier_rank));
					} else {
						player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + soldier_rank ));
					}
					
				}	
				if(inATeam.whichTeam(player.getName()).equalsIgnoreCase("team1")) {
					List<String> msgList = new ArrayList<>();
					for(String i1 : messages.getConfigurationSection("events.starting").getStringList("soldierMessage")) {
						msgList.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatTeamMessage(i1 , new CastleTeam("team1")).replace("%king%", new CastleTeam("team1").getKing().getName())));
					}
					String[] strarray = new String[msgList.size()];
					msgList.toArray(strarray);
		    		player.sendMessage(strarray);
					String[] coords = team1Config.getString("soldier_spawnpoint").split(",");
					Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
					player.teleport(loc);
				} else if(inATeam.whichTeam(player.getName()).equalsIgnoreCase("team2")) {
					List<String> msgList = new ArrayList<>();
					for(String i1 : messages.getConfigurationSection("events.starting").getStringList("soldierMessage")) {
						msgList.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatTeamMessage(i1 , new CastleTeam("team2")).replace("%king%", new CastleTeam("team2").getKing().getName())));
					}
					String[] strarray = new String[msgList.size()];
					msgList.toArray(strarray);
		    		player.sendMessage(strarray);
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
    			CWstartEvent event = new CWstartEvent(new CastleTeam("team1"), new CastleTeam("team2"), new CastlePlayer(GameStateManager.getTeam1King()), new CastlePlayer(GameStateManager.getTeam2King()));
				Bukkit.getPluginManager().callEvent(event);	
				

				
			}
		}
		
		
	}
	
	public void checkStart() {
		if(Bukkit.getOnlinePlayers().size() >= plugin.getConfig().getInt("minPlayers")) {
			if(Main.starting == false) {
		    	  CountdownTimer timer = new CountdownTimer(plugin,
		    		        plugin.getConfig().getConfigurationSection("countdowns.starting").getInt("delayBeforeGameStart"),
		    		        () ->  {
		    		        	Main.starting = true;
		    		        	GameStateText = "starting";
		    		        	String msg = messagesFormatter.formatTimeMessage(messages.getConfigurationSection("events.starting").getString("startingMessage"), plugin.getConfig().getConfigurationSection("countdowns.starting").getInt("delayBeforeGameStart"));
		    		        	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
		    		        },
		    		        () -> {    
		    		        	String msg = messagesFormatter.formatMessage(messages.getConfigurationSection("events.starting").getString("startedMessage"));
		    		        	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
		    		        	new GameStateManager().startGame();
		    		        	Object[] array = Bukkit.getOnlinePlayers().toArray();
		    		    		for(int i = 0; i < array.length; i++) {
		    		    			Player player = (Player) array[i];
		    		        		player.setLevel(0);
		    		    		}
		    		        	Main.starting = false;
		    		        	CountdownTimer.cancelTimer();
		    		        },
		    		        (t) -> {
		    		        	if(GameStateManager.GameState == true) {
		    		        		
		    		        		Main.starting = false;
			    		        	Object[] array = Bukkit.getOnlinePlayers().toArray();
			    		    		for(int i = 0; i < array.length; i++) {
			    		    			Player player = (Player) array[i];
			    		        		player.setLevel(0);
			    		    		}
			    		        	CountdownTimer.cancelTimer();
			    		        	GameStateText = "playing";
		    		        	}
		    		        	if(Bukkit.getOnlinePlayers().size() < plugin.getConfig().getInt("minPlayers")) {
		    		        		CountdownTimer.cancelTimer();
		    		        		Main.starting = false;
		    		        		GameStateText = "waiting";
			    		        	for(Player p : Bukkit.getOnlinePlayers()) {
			    		        		if(Main.starting == false) {
			    		        			p.setLevel(0);
			    		        		} else {
			    		        			p.setLevel(t.getSecondsLeft());
			    		        		}
			    		        	}
		    		        	}
		    		        	if(Bukkit.getOnlinePlayers().size() == plugin.getConfig().getInt("maxPlayers")) {
		    		        		t.reduceTimer(plugin.getConfig().getConfigurationSection("countdowns.starting").getInt("reducedDelayIfMaxPlayers"));
		    		        	}
		    		        	
		    		        	for(Player p : Bukkit.getOnlinePlayers()) {
		    		        		if(GameStateManager.GameState == true) {
		    		        			p.setLevel(0);
		    		        			GameStateText = "playing";
		    		        		} else {
		    		        			p.setLevel(t.getSecondsLeft());
		    		        			GameStateText = "starting";
		    		        		}
		    		        		
		    		        	}
		    		        }

		    		);
		    	  timer.scheduleTimer();     				
			}

		}
	}
}
