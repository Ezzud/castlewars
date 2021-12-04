package fr.ezzud.castlewar.events;

import java.util.ArrayList;
import java.util.List;

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
import org.bukkit.event.entity.EntityDamageEvent;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.CastlePlayer;
import fr.ezzud.castlewar.api.CastleTeam;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.api.TeamManager;
import fr.ezzud.castlewar.api.events.CWKillEvent;
import fr.ezzud.castlewar.api.events.CWendEvent;
import fr.ezzud.castlewar.methods.inATeam;
import fr.ezzud.castlewar.methods.messagesFormatter;
import fr.ezzud.castlewar.methods.countdowns.CountdownTimer;
import fr.ezzud.castlewar.methods.countdowns.EndCountdownTimer;
import fr.ezzud.castlewar.methods.countdowns.FWCountdownTimer;
import fr.ezzud.castlewar.methods.managers.FWManager;
import fr.ezzud.castlewar.methods.managers.kitManager;


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
	    		    			String[] sound = plugin.getConfig().getString("sounds.death").split(",");
	    		        		player.playSound(player.getLocation(), Sound.valueOf(sound[0]), Integer.parseInt(sound[1]), Integer.parseInt(sound[2]));
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
					   GameStateManager.GameStateText = "ending";
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
					    		        		if(inATeam.checkTeam(p.getName()) == false) continue; 
					    		        		if(inATeam.whichTeam(p.getName()) != null) {
						    		        		if(inATeam.whichTeam(p.getName()).equalsIgnoreCase("team2") ) {
								    		        	if(t.getSecondsLeft() % 2 == 0) {
								    		        		ChatColor teamColor = ChatColor.getByChar(TeamManager.getTeam2().getColor().charAt(1));
								    		        		FWManager.spawnFireworks(p.getLocation(), 1, FWManager.translateChatColorToColor(teamColor));
								    		        	}
						    		        		}				    		        			
					    		        		}

					    		        	}

					    		        }

					    		);
			  		    	FWtimer.scheduleTimer();						   
					   }
					   List<String> winCommands = plugin.getConfig().getConfigurationSection("endCommands").getStringList("win");
					   List<String> looseCommands = plugin.getConfig().getConfigurationSection("endCommands").getStringList("loose");
					   for(Player p : Bukkit.getOnlinePlayers()) {
						   if(inATeam.checkTeam(p.getName()) == false) continue;
						   if(inATeam.whichTeam(p.getName()).equalsIgnoreCase("team2")) {
							   for(String i : winCommands) {
								 Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), i.replaceAll("%player%", p.getName()));  
							   }
						   } else {
							   for(String i : looseCommands) {
								   Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), i.replaceAll("%player%", p.getName()));  
							   }
						   }
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
				    		        	if(plugin.getConfig().getBoolean("stopServerAtEnd") == true) {
				    		        		Bukkit.getServer().shutdown();
				    		        	}
				    		        	
	    		        	
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
	    		    			String[] sound = plugin.getConfig().getString("sounds.death").split(",");
	    		        		player.playSound(player.getLocation(), Sound.valueOf(sound[0]), Integer.parseInt(sound[1]), Integer.parseInt(sound[2]));
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
						   GameStateManager.GameStateText = "ending";
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
						    		        		if(inATeam.checkTeam(p.getName()) == false) continue; 
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
						   List<String> winCommands = plugin.getConfig().getConfigurationSection("endCommands").getStringList("win");
						   List<String> looseCommands = plugin.getConfig().getConfigurationSection("endCommands").getStringList("loose");
						   for(Player p : Bukkit.getOnlinePlayers()) {
							   if(inATeam.checkTeam(p.getName()) == false) continue;
							   if(inATeam.whichTeam(p.getName()).equalsIgnoreCase("team1")) {
								   for(String i : winCommands) {
									 Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), i.replaceAll("%player%", p.getName()));  
								   }
							   } else {
								   for(String i : looseCommands) {
									   Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), i.replaceAll("%player%", p.getName()));  
								   }
							   }
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
				    		        	if(plugin.getConfig().getBoolean("stopServerAtEnd") == true) {
				    		        		Bukkit.getServer().shutdown();
				    		        	}
	    		        	
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
				    		        	victim.sendMessage(messagesFormatter.formatMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("events.death.soldier.message").getString("respawned"))));
				    		        	victim.setGameMode(GameMode.SURVIVAL);
				    					if(inATeam.whichTeam(victim.getName()).equalsIgnoreCase("team1")) {
				    						String[] coords = team1Config.getString("soldier_spawnpoint").split(",");
				    						Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				    						victim.teleport(loc);
				    						victim.setHealth(20.0);
				    						victim.getInventory().clear();
				    						
				    					} else if(inATeam.whichTeam(victim.getName()).equalsIgnoreCase("team2")) {
				    						String[] coords = team2Config.getString("soldier_spawnpoint").split(",");
				    						Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				    						victim.teleport(loc);
				    						victim.setHealth(20.0);
				    						victim.getInventory().clear();	
				    					}
				    					new kitManager().setPlayerKit(victim);
				    					String[] sound = plugin.getConfig().getString("sounds.respawn").split(",");
			    		        		victim.playSound(victim.getLocation(), Sound.valueOf(sound[0]), Integer.parseInt(sound[1]), Integer.parseInt(sound[2]));
	    		        	
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
	   
	   @EventHandler
	   public void onDamageNoEntity(EntityDamageEvent event) {
		   if(event.getEntity() instanceof Player) {	   
			Player victim = (Player) event.getEntity();
			if(victim.getHealth() - event.getDamage() <= 0) {
				event.setCancelled(true);
			   YamlConfiguration messages = Main.messages;
			   if(victim.getName().equalsIgnoreCase(GameStateManager.team1King)) {
				   GameStateManager.GameState = false;
				   GameStateManager.createParticles = false;
			    	  ArrayList<?> list2 = new ArrayList<>(Bukkit.getOnlinePlayers());
			    	  list2.forEach((p) -> {
			    		  Player player = ((Player) p);
  		    			String[] sound = plugin.getConfig().getString("sounds.death").split(",");
		        		player.playSound(player.getLocation(), Sound.valueOf(sound[0]), Integer.parseInt(sound[1]), Integer.parseInt(sound[2]));
			    		  player.setGameMode(GameMode.SPECTATOR);
			    		  player.setDisplayName(ChatColor.RESET + player.getName());
			    		  player.setPlayerListName(ChatColor.RESET + player.getName());
			    	  });
			    	  String[] coords = plugin.getConfig().getString("spectator_spawnpoint").split(",");
					  Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
			    	  victim.teleport(loc);
			    	
					   String msg = messagesFormatter.formatKillMessage(messages.getConfigurationSection("events.death.king.message").getString("other"), victim, null);
					   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
	        			CWKillEvent killEvent = new CWKillEvent(null, new CastlePlayer(victim.getName()));
						Bukkit.getPluginManager().callEvent(killEvent);	
				   
				   String msg1 = messagesFormatter.formatTeamMessage(messages.getConfigurationSection("events.death.king").getString("victoryMessage"), new CastleTeam("team2"));
				   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg1));
				   GameStateManager.GameStateText = "ending";
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
				    		        		if(inATeam.checkTeam(p.getName()) == false) continue; 
				    		        		if(inATeam.whichTeam(p.getName()) != null) {
					    		        		if(inATeam.whichTeam(p.getName()).equalsIgnoreCase("team2") ) {
							    		        	if(t.getSecondsLeft() % 2 == 0) {
							    		        		ChatColor teamColor = ChatColor.getByChar(TeamManager.getTeam2().getColor().charAt(1));
							    		        		FWManager.spawnFireworks(p.getLocation(), 1, FWManager.translateChatColorToColor(teamColor));
							    		        	}
					    		        		}				    		        			
				    		        		}

				    		        	}

				    		        }

				    		);
		  		    	FWtimer.scheduleTimer();						   
				   }
				   List<String> winCommands = plugin.getConfig().getConfigurationSection("endCommands").getStringList("win");
				   List<String> looseCommands = plugin.getConfig().getConfigurationSection("endCommands").getStringList("loose");
				   for(Player p : Bukkit.getOnlinePlayers()) {
					   if(inATeam.checkTeam(p.getName()) == false) continue;
					   if(inATeam.whichTeam(p.getName()).equalsIgnoreCase("team2")) {
						   for(String i : winCommands) {
							 Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), i.replaceAll("%player%", p.getName()));  
						   }
					   } else {
						   for(String i : looseCommands) {
							   Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), i.replaceAll("%player%", p.getName()));  
						   }
					   }
				   }
        			CWendEvent event1 = new CWendEvent(new CastleTeam("team1"), new CastleTeam("team2"), new CastlePlayer(GameStateManager.getTeam1King()), new CastlePlayer(GameStateManager.getTeam2King()), "leave");
					Bukkit.getPluginManager().callEvent(event1);	
	  		    	  EndCountdownTimer timer = new EndCountdownTimer(plugin,
	  		    			plugin.getConfig().getConfigurationSection("countdowns.end").getInt("delayBeforeRestart"),
			    		        () ->  {
			    		        	String msg2 = messagesFormatter.formatTimeMessage(messages.getConfigurationSection("events.death").getString("restartingIn"), plugin.getConfig().getConfigurationSection("countdowns.end").getInt("delayBeforeRestart"));
			    		        	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg2));
			    		        },
			    		        () -> {    
			    		        	EndCountdownTimer.cancelTimer();
			    		        	new GameStateManager().stopGame();
			    		        	if(plugin.getConfig().getBoolean("stopServerAtEnd") == true) {
			    		        		Bukkit.getServer().shutdown();
			    		        	}
			    		        	
    		        	
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
  		    			String[] sound = plugin.getConfig().getString("sounds.death").split(",");
		        		player.playSound(player.getLocation(), Sound.valueOf(sound[0]), Integer.parseInt(sound[1]), Integer.parseInt(sound[2]));
			    		  player.setGameMode(GameMode.SPECTATOR);
			    		  player.setDisplayName(ChatColor.RESET + player.getName());
			    		  player.setPlayerListName(ChatColor.RESET + player.getName());
			    	  });
			    	  String[] coordinates = plugin.getConfig().getString("spectator_spawnpoint").split(",");
					  Location location = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[2]), Float.parseFloat(coordinates[3]), Float.parseFloat(coordinates[3]));
			    	  victim.teleport(location);
						   String msg = messagesFormatter.formatKillMessage(messages.getConfigurationSection("events.death.king.message").getString("other"), victim, null);
						   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg));
		        			CWKillEvent killEvent = new CWKillEvent(null, new CastlePlayer(victim.getName()));
							Bukkit.getPluginManager().callEvent(killEvent);	
					   
					   String msg1 = messagesFormatter.formatTeamMessage(messages.getConfigurationSection("events.death.king").getString("victoryMessage"), new CastleTeam("team1"));
					   Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg1));
					   GameStateManager.GameStateText = "ending";
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
					    		        		if(inATeam.checkTeam(p.getName()) == false) continue; 
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
					   List<String> winCommands = plugin.getConfig().getConfigurationSection("endCommands").getStringList("win");
					   List<String> looseCommands = plugin.getConfig().getConfigurationSection("endCommands").getStringList("loose");
					   for(Player p : Bukkit.getOnlinePlayers()) {
						   if(inATeam.checkTeam(p.getName()) == false) continue;
						   if(inATeam.whichTeam(p.getName()).equalsIgnoreCase("team1")) {
							   for(String i : winCommands) {
								 Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), i.replaceAll("%player%", p.getName()));  
							   }
						   } else {
							   for(String i : looseCommands) {
								   Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), i.replaceAll("%player%", p.getName()));  
							   }
						   }
					   }
	        			CWendEvent event1 = new CWendEvent(new CastleTeam("team1"), new CastleTeam("team2"), new CastlePlayer(GameStateManager.getTeam1King()), new CastlePlayer(GameStateManager.getTeam2King()), "leave");
						Bukkit.getPluginManager().callEvent(event1);	
		  		    	  EndCountdownTimer timer = new EndCountdownTimer(plugin,
		  		    			plugin.getConfig().getConfigurationSection("countdowns.end").getInt("delayBeforeRestart"),
				    		        () ->  {
				    		        	String msg2 = messagesFormatter.formatTimeMessage(messages.getConfigurationSection("events.death").getString("restartingIn"), plugin.getConfig().getConfigurationSection("countdowns.end").getInt("delayBeforeRestart"));
				    		        	Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', msg2));
				    		        },
			    		        () -> {   
			    		        	EndCountdownTimer.cancelTimer();
			    		        	new GameStateManager().stopGame();
			    		        	if(plugin.getConfig().getBoolean("stopServerAtEnd") == true) {
			    		        		Bukkit.getServer().shutdown();
			    		        	}
    		        	
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
			    	  String[] coordinates = plugin.getConfig().getString("spectator_spawnpoint").split(",");
					  Location location = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coordinates[0]), Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[2]), Float.parseFloat(coordinates[3]), Float.parseFloat(coordinates[3]));
			    	  victim.teleport(location);
	  		    	  CountdownTimer timer = new CountdownTimer(plugin,
	  		    			plugin.getConfig().getConfigurationSection("countdowns").getInt("respawnDelay"),
			    		        () ->  {
			    		        		String msg = messagesFormatter.formatKillMessage(messages.getConfigurationSection("events.death.soldier.message").getString("other"), victim, null);
			    		        		victim.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
					        			CWKillEvent killEvent = new CWKillEvent(null, new CastlePlayer(victim.getName()));
										Bukkit.getPluginManager().callEvent(killEvent);	
			    		        	
			    		        	
			    		        },
			    		        () -> {    
			    		        	victim.sendMessage(messagesFormatter.formatMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("events.death.soldier.message").getString("respawned"))));
			    		        	victim.setGameMode(GameMode.SURVIVAL);
			    					if(inATeam.whichTeam(victim.getName()).equalsIgnoreCase("team1")) {
			    						String[] coords = team1Config.getString("soldier_spawnpoint").split(",");
			    						Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
			    						victim.teleport(loc);
			    						victim.setHealth(20.0);
			    						victim.getInventory().clear();
			    						new kitManager().setPlayerKit(victim);
			    						
			    					} else if(inATeam.whichTeam(victim.getName()).equalsIgnoreCase("team2")) {
			    						String[] coords = team2Config.getString("soldier_spawnpoint").split(",");
			    						Location loc = new Location(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
			    						victim.teleport(loc);
			    						victim.setHealth(20.0);
			    						victim.getInventory().clear();
			    						new kitManager().setPlayerKit(victim);
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