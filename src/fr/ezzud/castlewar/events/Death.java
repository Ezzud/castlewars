package fr.ezzud.castlewar.events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.methods.CountdownTimer;
import fr.ezzud.castlewar.methods.inATeam;
import net.md_5.bungee.api.ChatColor;


public class Death implements Listener {
    Main plugin;
    
    public Death(Main instance) {
        plugin = instance;
    }
	   @EventHandler
	   public void onPlayerKill(EntityDamageByEntityEvent e)
	   {
		   if(GameStateManager.getGameState() == false) return;
		   if (e.getEntity() instanceof Player) {
			   Player victim = (Player) e.getEntity();
			   
			   if(victim.getHealth() - e.getDamage() < 1) {
				   e.setCancelled(true);
				   if(victim.getName().equalsIgnoreCase(GameStateManager.team1King)) {
					   GameStateManager.GameState = false;
				    	  ArrayList<?> list2 = new ArrayList<>(Bukkit.getOnlinePlayers());
				    	  list2.forEach((p) -> {
				    		  Player player = ((Player) p);
				    		  player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 100, 100);
				    		  player.setGameMode(GameMode.SPECTATOR);
				    		  player.setDisplayName(ChatColor.RESET + player.getName());
				    		  player.setPlayerListName(ChatColor.RESET + player.getName());
				    	  });
					   if(e.getDamager() instanceof Player) {
						   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', e.getDamager().getName() + " &ehas killed the enemy king"));
					   } else {
						   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&eThe king of team 1 died"));
					   }
					   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&eVictory of team 2"));
				   } else if(victim.getName().equalsIgnoreCase(GameStateManager.team2King)) {
					   GameStateManager.GameState = false;
				    	  ArrayList<?> list2 = new ArrayList<>(Bukkit.getOnlinePlayers());
				    	  list2.forEach((p) -> {
				    		  Player player = ((Player) p);
				    		  player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 100, 100);
				    		  player.setGameMode(GameMode.SPECTATOR);
				    		  player.setDisplayName(ChatColor.RESET + player.getName());
				    		  player.setPlayerListName(ChatColor.RESET + player.getName());
				    	  });
					   if(e.getDamager() instanceof Player) {
						   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', e.getDamager().getName() + " &ehas killed the enemy king"));
					   } else {
						   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&eThe king of team 2 died"));
					   }
					   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&eVictory of team 1"));
				   } else {
					   victim.setGameMode(GameMode.SPECTATOR);
						ConfigurationSection team1Config = plugin.getConfig().getConfigurationSection("team1");
						ConfigurationSection team2Config = plugin.getConfig().getConfigurationSection("team2");
		  		    	  CountdownTimer timer = new CountdownTimer(plugin,
				    		        5,
				    		        () ->  {
				    		        	victim.sendMessage("Vous êtes mort!");
				    		        },
				    		        () -> {    
				    		        	victim.sendMessage("Réapparition!");
				    		        	victim.setGameMode(GameMode.SURVIVAL);
				    					if(inATeam.whichTeam(victim.getName()).equalsIgnoreCase("team1")) {
				    						String[] coords = team1Config.getString("soldier_spawnpoint").split(",");
				    						Location loc = new Location(Bukkit.getWorld(coords[5]), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				    						victim.teleport(loc);
				    					} else if(inATeam.whichTeam(victim.getName()).equalsIgnoreCase("team2")) {
				    						String[] coords = team2Config.getString("soldier_spawnpoint").split(",");
				    						Location loc = new Location(Bukkit.getWorld(coords[5]), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				    						victim.teleport(loc);
				    					}
	    		        	
				    		        },
				    		        (t) -> {
				    		        	victim.sendTitle(ChatColor.translateAlternateColorCodes('&', "Réapparition dans"), ChatColor.translateAlternateColorCodes('&', String.valueOf(t.getSecondsLeft())), 2, 20, 2);
				    		        }

				    		);
				    	  timer.scheduleTimer();
				   }
			   }

		   }
	   }
}