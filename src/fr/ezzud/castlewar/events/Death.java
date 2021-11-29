package fr.ezzud.castlewar.events;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.CastlePlayer;
import fr.ezzud.castlewar.api.CastleTeam;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.api.TeamManager;
import fr.ezzud.castlewar.api.events.CWKillEvent;
import fr.ezzud.castlewar.api.events.CWendEvent;
import fr.ezzud.castlewar.methods.CountdownTimer;
import fr.ezzud.castlewar.methods.EndCountdownTimer;
import fr.ezzud.castlewar.methods.FWCountdownTimer;
import fr.ezzud.castlewar.methods.FWManager;
import fr.ezzud.castlewar.methods.inATeam;
import fr.ezzud.castlewar.methods.messagesFormatter;


public class Death implements Listener {
    Main plugin;
    
    public Death(Main instance) {
        plugin = instance;
    }
	   @EventHandler
	   public void onPlayerKill(EntityDamageByEntityEvent e)
	   {
		   if(e.isCancelled() == true) return;
		   if(GameStateManager.getGameState() == false) return;
		   if (e.getEntity() instanceof Player) {
			   Player victim = (Player) e.getEntity();
			   
			   if(victim.getHealth() - e.getDamage() < 1) {
				   
				   e.setCancelled(true);
				   YamlConfiguration messages = Main.messages;
				   if(victim.getName().equalsIgnoreCase(GameStateManager.team1King)) {
					   GameStateManager.GameState = false;
					   GameStateManager.createParticles = false;
				    	  ArrayList<?> list2 = new ArrayList<>(Bukkit.getOnlinePlayers());
				    	  list2.forEach((p) -> {
				    		  Player player = ((Player) p);
				    		  player.playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, 100, 100);
				    		  player.setGameMode(GameMode.SPECTATOR);
				    		  player.setDisplayName(ChatColor.RESET + player.getName());
				    		  player.setPlayerListName(ChatColor.RESET + player.getName());
				    	  });
				    	
					   if(e.getDamager() instanceof Player) {
						   String msg = messagesFormatter.formatKillMessage(messages.getConfigurationSection("events.death.king.message").getString("byPlayer"), victim, (Player) e.getDamager());
		        			CWKillEvent killEvent = new CWKillEvent(new CastlePlayer(e.getDamager().getName()), new CastlePlayer(victim.getName()));
							Bukkit.getPluginManager().callEvent(killEvent);	
						   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
					   } else {
						   String msg = messagesFormatter.formatKillMessage(messages.getConfigurationSection("events.death.king.message").getString("other"), victim, null);
						   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
		        			CWKillEvent killEvent = new CWKillEvent(null, new CastlePlayer(victim.getName()));
							Bukkit.getPluginManager().callEvent(killEvent);	
					   }
					   String msg = messagesFormatter.formatTeamMessage(messages.getConfigurationSection("events.death.king").getString("victoryMessage"), new CastleTeam("team2"));
					   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
					   if(plugin.getConfig().getConfigurationSection("gameRules").getBoolean("endFireworks") == true) {
						   FWCountdownTimer FWtimer = new FWCountdownTimer(plugin,
			  		    			6,
					    		        () ->  {
					    		        },
					    		        () -> {    
					    		        	FWCountdownTimer.cancelTimer();
		    		        	
					    		        },
					    		        (t) -> {
					    		        	for(Player p : Bukkit.getOnlinePlayers()) {
					    		        		if(inATeam.whichTeam(p.getName()).equalsIgnoreCase("team2") ) {
							    		        	if(t.getSecondsLeft() % 2 == 0) {
							    		        		ChatColor teamColor = ChatColor.getByChar(TeamManager.getTeam2().getColor().charAt(1));
							    		        		FWManager.spawnFireworks(p.getLocation(), 1, FWManager.translateChatColorToColor(teamColor));
							    		        	}
					    		        		}
					    		        	}

					    		        }

					    		);
			  		    	FWtimer.scheduleTimer();						   
					   }
	        			CWendEvent event = new CWendEvent(new CastleTeam("team1"), new CastleTeam("team2"), new CastlePlayer(GameStateManager.getTeam1King()), new CastlePlayer(GameStateManager.getTeam2King()), "leave");
						Bukkit.getPluginManager().callEvent(event);	
		  		    	  EndCountdownTimer timer = new EndCountdownTimer(plugin,
		  		    			plugin.getConfig().getConfigurationSection("countdowns.end").getInt("delayBeforeRestart"),
				    		        () ->  {
				    		        	String msg2 = messagesFormatter.formatTimeMessage(messages.getConfigurationSection("events.death").getString("restartingIn"), plugin.getConfig().getConfigurationSection("countdowns.end").getInt("delayBeforeRestart"));
				    		        	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg2));
				    		        },
				    		        () -> {    
				    		        	EndCountdownTimer.cancelTimer();
				    		        	new GameStateManager().stopGame();
				    		        	
	    		        	
				    		        },
				    		        (t) -> {
				    		        	for(Player p : Bukkit.getOnlinePlayers()) {
				    		        		p.setLevel(t.getSecondsLeft());
				    		        	}
				    		        }

				    		);
				    	  timer.scheduleTimer();
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
							   String msg = messagesFormatter.formatKillMessage(messages.getConfigurationSection("events.death.king.message").getString("byPlayer"), victim, (Player) e.getDamager());
							   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
			        			CWKillEvent killEvent = new CWKillEvent(new CastlePlayer(e.getDamager().getName()), new CastlePlayer(victim.getName()));
								Bukkit.getPluginManager().callEvent(killEvent);	
						   } else {
							   String msg = messagesFormatter.formatKillMessage(messages.getConfigurationSection("events.death.king.message").getString("other"), victim, null);
							   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
			        			CWKillEvent killEvent = new CWKillEvent(null, new CastlePlayer(victim.getName()));
								Bukkit.getPluginManager().callEvent(killEvent);	
						   }
						   String msg = messagesFormatter.formatTeamMessage(messages.getConfigurationSection("events.death.king").getString("victoryMessage"), new CastleTeam("team1"));
						   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
						   if(plugin.getConfig().getConfigurationSection("gameRules").getBoolean("endFireworks") == true) {
							   FWCountdownTimer FWtimer = new FWCountdownTimer(plugin,
				  		    			6,
						    		        () ->  {
						    		        },
						    		        () -> {    
						    		        	FWCountdownTimer.cancelTimer();
			    		        	
						    		        },
						    		        (t) -> {
						    		        	for(Player p : Bukkit.getOnlinePlayers()) {
						    		        		if(inATeam.whichTeam(p.getName()).equalsIgnoreCase("team1") ) {
								    		        	if(t.getSecondsLeft() % 2 == 0) {
								    		        		ChatColor teamColor = ChatColor.getByChar(TeamManager.getTeam1().getColor().charAt(1));
								    		        		FWManager.spawnFireworks(p.getLocation(), 1, FWManager.translateChatColorToColor(teamColor));
								    		        	}
						    		        		}
						    		        	}

						    		        }

						    		);
				  		    	FWtimer.scheduleTimer();
						   }
		        			CWendEvent event = new CWendEvent(new CastleTeam("team1"), new CastleTeam("team2"), new CastlePlayer(GameStateManager.getTeam1King()), new CastlePlayer(GameStateManager.getTeam2King()), "leave");
							Bukkit.getPluginManager().callEvent(event);	
			  		    	  CountdownTimer timer = new CountdownTimer(plugin,
			  		    			plugin.getConfig().getConfigurationSection("countdowns.end").getInt("delayBeforeRestart"),
					    		        () ->  {
					    		        	String msg2 = messagesFormatter.formatTimeMessage(messages.getConfigurationSection("events.death").getString("restartingIn"), plugin.getConfig().getConfigurationSection("countdowns.end").getInt("delayBeforeRestart"));
					    		        	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg2));
					    		        },
				    		        () -> {   
				    		        	CountdownTimer.cancelTimer();
				    		        	new GameStateManager().stopGame();
				    		        	new GameStateManager().checkStart();
				    		        	
	    		        	
				    		        },
				    		        (t) -> {
				    		        	for(Player p : Bukkit.getOnlinePlayers()) {
				    		        		p.setLevel(t.getSecondsLeft());
				    		        	}
				    		        }

				    		);
				    	  timer.scheduleTimer();
				   } else {
					   victim.setGameMode(GameMode.SPECTATOR);
						ConfigurationSection team1Config = plugin.getConfig().getConfigurationSection("team1");
						ConfigurationSection team2Config = plugin.getConfig().getConfigurationSection("team2");
		  		    	  CountdownTimer timer = new CountdownTimer(plugin,
		  		    			plugin.getConfig().getConfigurationSection("countdowns").getInt("respawnDelay"),
				    		        () ->  {
				    		        	if(e.getDamager() instanceof Player) {
				    		        		String msg = messagesFormatter.formatKillMessage(messages.getConfigurationSection("events.death.soldier.message").getString("byPlayer"), victim, (Player) e.getDamager());
				    		        		victim.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
						        			CWKillEvent killEvent = new CWKillEvent(new CastlePlayer(e.getDamager().getName()), new CastlePlayer(victim.getName()));
											Bukkit.getPluginManager().callEvent(killEvent);	
				    		        	} else {
				    		        		String msg = messagesFormatter.formatKillMessage(messages.getConfigurationSection("events.death.soldier.message").getString("other"), victim, null);
				    		        		victim.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
						        			CWKillEvent killEvent = new CWKillEvent(null, new CastlePlayer(victim.getName()));
											Bukkit.getPluginManager().callEvent(killEvent);	
				    		        	}
				    		        	
				    		        },
				    		        () -> {    
				    		        	victim.sendMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("events.death.soldier.message").getString("respawned")));
				    		        	victim.setGameMode(GameMode.SURVIVAL);
				    					if(inATeam.whichTeam(victim.getName()).equalsIgnoreCase("team1")) {
				    						String[] coords = team1Config.getString("soldier_spawnpoint").split(",");
				    						Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				    						victim.teleport(loc);
				    						victim.setHealth(20.0);
				    						
				    					} else if(inATeam.whichTeam(victim.getName()).equalsIgnoreCase("team2")) {
				    						String[] coords = team2Config.getString("soldier_spawnpoint").split(",");
				    						Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				    						victim.teleport(loc);
				    						victim.setHealth(20.0);
				    					}
	    		        	
				    		        },
				    		        (t) -> {
				    		        	String subtitle = messagesFormatter.formatTimeMessage(messages.getConfigurationSection("events.death.soldier.title").getString("subtitle"), t.getSecondsLeft());
				    		        	victim.sendTitle(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("events.death.soldier.title").getString("main")), ChatColor.translateAlternateColorCodes('&', subtitle), 2, 20, 2);
				    		        }

				    		);
				    	  timer.scheduleTimer();
				   }
			   }

		   }
	   }
}