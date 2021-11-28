package fr.ezzud.castlewar.events;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.methods.CountdownTimer;
import fr.ezzud.castlewar.methods.configManager;
import fr.ezzud.castlewar.methods.inATeam;
import net.md_5.bungee.api.ChatColor;

public class Join implements Listener {
	YamlConfiguration data = Main.data;
    Main plugin;
    public Join(Main instance) {
        plugin = instance;
    }	

    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
    	e.setJoinMessage(null);
    	Player player = e.getPlayer();
        	if(GameStateManager.getGameState() == false) {
        		
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
  			   	if(data.getString(String.valueOf(player.getUniqueId())) == null) {
  			   		data.set(String.valueOf(player.getUniqueId()), plugin.getConfig().getString("default_kit"));
  					try {
  						data.save(new File("plugins/CastleWars/data.yml"));
  					} catch (IOException e1) {
  						e1.printStackTrace();
  					}
  					Main.data = configManager.getData();
  			   	}
  			   	
  			   	
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
        		
        		new GameStateManager().checkStart();
        	} else {
        		if(inATeam.checkTeam(player.getName()) == false) {
        			player.setGameMode(GameMode.SPECTATOR);
            		String[] coordsStr = plugin.getConfig().getString("spectators_spawnpoint").split(",");
            		Location coords = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world")), Double.parseDouble(coordsStr[0]), Double.parseDouble(coordsStr[1]), Double.parseDouble(coordsStr[2]), Float.parseFloat(coordsStr[3]), Float.parseFloat(coordsStr[4]));
            		player.teleport(coords);
        		} else {
					   player.setGameMode(GameMode.SPECTATOR);
						ConfigurationSection team1Config = plugin.getConfig().getConfigurationSection("team1");
						ConfigurationSection team2Config = plugin.getConfig().getConfigurationSection("team2");
		  		    	  CountdownTimer timer = new CountdownTimer(plugin,
				    		        5,
				    		        () ->  {
				    		        	player.sendMessage("Vous êtes mort!");
				    		        },
				    		        () -> {    
				    		        	player.sendMessage("Réapparition!");
				    		        	player.setGameMode(GameMode.SURVIVAL);
				    					if(inATeam.whichTeam(player.getName()).equalsIgnoreCase("team1")) {
				    						String[] coords = team1Config.getString("soldier_spawnpoint").split(",");
				    						Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				    						player.teleport(loc);
				    						player.setHealth(20.0);
				    						
				    					} else if(inATeam.whichTeam(player.getName()).equalsIgnoreCase("team2")) {
				    						String[] coords = team2Config.getString("soldier_spawnpoint").split(",");
				    						Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				    						player.teleport(loc);
				    						player.setHealth(20.0);
				    					}
	    		        	
				    		        },
				    		        (t) -> {
				    		        	player.sendTitle(ChatColor.translateAlternateColorCodes('&', "Réapparition dans"), ChatColor.translateAlternateColorCodes('&', String.valueOf(t.getSecondsLeft())), 2, 20, 2);
				    		        }

				    		);
				    	  timer.scheduleTimer();
        		}
        	}
        	 
           
         
       }
}