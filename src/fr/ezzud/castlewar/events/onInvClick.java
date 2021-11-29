package fr.ezzud.castlewar.events;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.CastleKit;
import fr.ezzud.castlewar.api.CastleTeam;
import fr.ezzud.castlewar.api.TeamManager;
import fr.ezzud.castlewar.api.events.CWkitChangeEvent;
import fr.ezzud.castlewar.commands.players.kitsCMD;
import fr.ezzud.castlewar.commands.players.teamCMD;
import fr.ezzud.castlewar.gui.KitsGUI;
import fr.ezzud.castlewar.gui.TeamGUI;
import fr.ezzud.castlewar.methods.inATeam;
import fr.ezzud.castlewar.methods.messagesFormatter;
import fr.ezzud.castlewar.methods.managers.GUIManager;
import fr.ezzud.castlewar.methods.managers.configManager;
import net.md_5.bungee.api.ChatColor;

public class onInvClick implements Listener {
    Main plugin;
	YamlConfiguration guis = Main.guis;
	YamlConfiguration data = Main.data;
	YamlConfiguration kits = Main.kits;
	YamlConfiguration messages = Main.messages;
	ConfigurationSection chooseTeam = guis.getConfigurationSection("chooseTeam");
	ConfigurationSection chooseKit = guis.getConfigurationSection("chooseKit");
	ConfigurationSection team1 = guis.getConfigurationSection("chooseTeam.chooseItems.team1");
	ConfigurationSection team2 = guis.getConfigurationSection("chooseTeam.chooseItems.team2");
	ConfigurationSection none = guis.getConfigurationSection("chooseTeam.chooseItems.none");
    public onInvClick(Main instance) {
        plugin = instance;
    }	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e) {
		if(!e.getWhoClicked().getGameMode().equals(GameMode.CREATIVE)) {
	    	String[] teamItemInfo = plugin.getConfig().getConfigurationSection("teamChooseItem").getString("item").split(",");
	    	if(e.getCurrentItem() == null) return;
	    	if(e.getCurrentItem().getItemMeta() == null) return;
	        if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', teamItemInfo[2]))) {
	            if(e.getCurrentItem().getType() == Material.valueOf(teamItemInfo[0])) {
	           	 e.setCancelled(true);
	           	new teamCMD((Player) e.getWhoClicked());
	           	 e.getWhoClicked().getItemOnCursor().setAmount(0);
	            }
	       	 
	        }	
	    	String[] kitItemInfo = plugin.getConfig().getConfigurationSection("kitChooseItem").getString("item").split(",");
	    	if(e.getCurrentItem() == null) return;
	    	if(e.getCurrentItem().getItemMeta() == null) return;
	        if (e.getCurrentItem().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', kitItemInfo[2]))) {
	            if(e.getCurrentItem().getType() == Material.valueOf(kitItemInfo[0])) {
	           	 e.setCancelled(true);
	           	 new kitsCMD((Player) e.getWhoClicked());
	           	 e.getWhoClicked().getItemOnCursor().setAmount(0);
	            }
	       	 
	        }	
		}
		if (e.getView().getTitle().contains(ChatColor.translateAlternateColorCodes('&', chooseKit.getString("title")))) {
			e.setCancelled(true);
		    final ItemStack clickedItem = e.getCurrentItem();
		    if (clickedItem == null || clickedItem.getType().isAir()) return;	
		    final Player player = (Player) e.getWhoClicked();
		    ConfigurationSection items = chooseKit.getConfigurationSection("chooseItems");
		    for(int i = 0; i < items.getKeys(false).size(); i++) {
		    	 ConfigurationSection item = items.getConfigurationSection(String.valueOf(i));
		    	 if(clickedItem.getItemMeta().getDisplayName().contains(ChatColor.translateAlternateColorCodes('&', item.getString("item").split(",")[2]))) {
		    		 if(kits.getConfigurationSection("kits." + item.getString("kit")) == null) {
			        		player.sendMessage(messagesFormatter.formatMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("kits").getString("notExist"))));
			        		return;	 
		    		 }
		         	String permission = kits.getConfigurationSection("kits." + item.getString("kit")).getString("permission");
		         	String oldKit = null;
		         	if(data.getString(String.valueOf(player.getUniqueId())) == null) {
		         		oldKit = plugin.getConfig().getString("default_kit");
		         	} else {
		         		oldKit = data.getString(String.valueOf(player.getUniqueId()));
		         	}
		        	if(data.getString(String.valueOf(player.getUniqueId())) == null) {
						data.set(String.valueOf(player.getUniqueId()), plugin.getConfig().getString("default_kit"));
		    			try {
							data.save(new File("plugins/CastleWars/data.yml"));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
		    			Main.data = configManager.getData();
		        	}
		         	if (!player.hasPermission(permission) && !player.isOp()) {
		        		player.sendMessage(messagesFormatter.formatMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("kits").getString("noPermission"))));
		        		return;
		        	} else if(data.getString(String.valueOf(player.getUniqueId())).equalsIgnoreCase(item.getString("kit"))) {
		        		player.sendMessage(messagesFormatter.formatMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("kits").getString("alreadySelected"))));
		        		return;
		        	}
					data.set(String.valueOf(player.getUniqueId()), item.getString("kit"));
	    			try {
						data.save(new File("plugins/CastleWars/data.yml"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	    			Main.data = configManager.getData();
	    			player.sendMessage(messagesFormatter.formatMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("kits").getString("kitChange").replaceAll("%kitname%",item.getString("kit")))));
        			CWkitChangeEvent event = new CWkitChangeEvent(new CastleKit(oldKit), new CastleKit(item.getString("kit")));
					Bukkit.getPluginManager().callEvent(event);	
	    			new GUIManager(player).initializeKitsGUI(new TeamGUI(player).getInventory());
		    		new GUIManager(player).openInventory(player, new KitsGUI(player).getInventory());    	       	
		         	
		         	
		         	
				 }
		    }
		}
	    if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', chooseTeam.getString("title")))) {
	    	Scoreboard board = Main.board;
		    e.setCancelled(true);
		   	   int maxPlayer = plugin.getConfig().getInt("maxPlayers");
		   	   if(maxPlayer % 2 == 1) {
		   		   maxPlayer++;
		   	   }   
		   	   maxPlayer = maxPlayer/2;
		   	new TeamManager();
		    final ItemStack clickedItem = e.getCurrentItem();
		    if (clickedItem == null || clickedItem.getType().isAir()) return;
		    String team1Item = ChatColor.translateAlternateColorCodes('&', team1.getString("item").split(",")[2]);
		    String team2Item = ChatColor.translateAlternateColorCodes('&', team2.getString("item").split(",")[2]);
		    String noneItem = ChatColor.translateAlternateColorCodes('&', none.getString("item").split(",")[2]);
		    final Player player = (Player) e.getWhoClicked();
		    if(clickedItem.getItemMeta().getDisplayName().contains(team1Item)) {
		    	if(inATeam.checkTeam(player.getName()) == false) {
		    		List<String> newTeam = new ArrayList<>();
		    		for(String pl : board.getTeam("team1").getEntries()) {
		    			newTeam.add(pl);
		    		}
		    		if(newTeam.size() - 1 >= maxPlayer) {
		    			player.sendMessage(messagesFormatter.formatMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("teams").getString("full"))));
		    			return;
		    		}
		    		if(newTeam.size() > board.getTeam("team2").getEntries().size()) {
		    			player.sendMessage(messagesFormatter.formatMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("teams").getString("notBalanced"))));
		    			return;
		    		}
		    		TeamManager.addMemberToTeam(player, "team1");
		    		
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getColor() + player.getName() + "&r"));
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getColor() + player.getName() + "&r"));
					String msg = messagesFormatter.formatTeamMessage(messages.getConfigurationSection("events.teamChange").getString("join"), new CastleTeam("team1"));
		    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		    		for(Player p : Bukkit.getOnlinePlayers()) {
		    			if(p.getOpenInventory().getTitle().equalsIgnoreCase(e.getView().getTitle())) {
				    		new GUIManager(p).initializeTeamGUI(new TeamGUI(p).getInventory());
				    		new GUIManager(p).openInventory(p, new TeamGUI(p).getInventory());
		    			}
		    		}
		    			
		    		
		    	} else {
		    		if(inATeam.checkSpecificTeam(player.getName(), "team1") == false) {
 
			    		List<String> newTeam = new ArrayList<>();
			    		for(String pl : board.getTeam("team1").getEntries()) {
			    			newTeam.add(pl);
			    		}
			    		List<String> actualTeam = new ArrayList<>();
			    		for(String pl : board.getTeam("team2").getEntries()) {
			    			actualTeam.add(pl);
			    		}
			    		if(newTeam.size() - 1 >= maxPlayer) {
			    			player.sendMessage(messagesFormatter.formatMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("teams").getString("full"))));
			    			return;
			    		}	
			    		if(newTeam.size() + 1 > board.getTeam("team2").getEntries().size()) {
			    			player.sendMessage(messagesFormatter.formatMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("teams").getString("notBalanced"))));
			    			return;
			    		}
			    		TeamManager.removeMemberFromTeam(player, "team2");
			    		TeamManager.addMemberToTeam(player, "team1");
			    		
			    		
						player.setDisplayName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getColor() + player.getName() + "&r"));
			    		
						player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getColor() + player.getName() + "&r"));
						String msg = messagesFormatter.formatTeamMessage(messages.getConfigurationSection("events.teamChange").getString("join"), new CastleTeam("team1"));
			    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			    		for(Player p : Bukkit.getOnlinePlayers()) {
			    			if(p.getOpenInventory().getTitle().equalsIgnoreCase(e.getView().getTitle())) {
					    		new GUIManager(p).initializeTeamGUI(new TeamGUI(p).getInventory());
					    		new GUIManager(p).openInventory(p, new TeamGUI(p).getInventory());
			    			}
			    		}
		    		}
		    	}
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    } else if(clickedItem.getItemMeta().getDisplayName().contains(team2Item)) {
		    	if(inATeam.checkTeam(player.getName()) == false) {
		    		
		    		List<String> newTeam = new ArrayList<>();
		    		for(String pl : board.getTeam("team2").getEntries()) {
		    			newTeam.add(pl);
		    		}
		    		if(newTeam.size() - 1 >= maxPlayer) {
		    			player.sendMessage(messagesFormatter.formatMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("teams").getString("full"))));
		    			return;
		    		}
		    		if(newTeam.size() > board.getTeam("team1").getEntries().size()) {
		    			player.sendMessage(messagesFormatter.formatMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("teams").getString("notBalanced"))));
		    			return;
		    		}
		    		
		    		TeamManager.addMemberToTeam(player, "team2");
		    		
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam2().getColor() + player.getName() + "&r"));
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam2().getColor() + player.getName() + "&r"));
					String msg = messagesFormatter.formatTeamMessage(messages.getConfigurationSection("events.teamChange").getString("join"), new CastleTeam("team2"));
		    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		    		for(Player p : Bukkit.getOnlinePlayers()) {
		    			if(p.getOpenInventory().getTitle().equalsIgnoreCase(e.getView().getTitle())) {
				    		new GUIManager(p).initializeTeamGUI(new TeamGUI(p).getInventory());
				    		new GUIManager(p).openInventory(p, new TeamGUI(p).getInventory());
		    			}
		    		}
		    			
		    		
		    	} else {
		    		if(inATeam.checkSpecificTeam(player.getName(), "team2") == false) {
			    		List<String> newTeam = new ArrayList<>();
			    		for(String pl : board.getTeam("team2").getEntries()) {
			    			newTeam.add(pl);
			    		}
			    		List<String> actualTeam = new ArrayList<>();
			    		for(String pl : board.getTeam("team1").getEntries()) {
			    			actualTeam.add(pl);
			    		}
			    		
			    		if(newTeam.size() - 1 >= maxPlayer) {
			    			player.sendMessage(messagesFormatter.formatMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("teams").getString("full"))));
			    			return;
			    		}	
			    		if(newTeam.size() + 1 > board.getTeam("team1").getEntries().size()) {
			    			player.sendMessage(messagesFormatter.formatMessage(ChatColor.translateAlternateColorCodes('&', messages.getConfigurationSection("teams").getString("notBalanced"))));
			    			return;
			    		}
			    		TeamManager.removeMemberFromTeam(player, "team1");
			    		TeamManager.addMemberToTeam(player, "team2");
			    		
			    		
						player.setDisplayName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam2().getColor() + player.getName() + "&r"));
			    		
						player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam2().getColor() + player.getName() + "&r"));
						String msg = messagesFormatter.formatTeamMessage(messages.getConfigurationSection("events.teamChange").getString("join"), new CastleTeam("team2"));
			    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
			    		for(Player p : Bukkit.getOnlinePlayers()) {
			    			if(p.getOpenInventory().getTitle().equalsIgnoreCase(e.getView().getTitle())) {
					    		new GUIManager(p).initializeTeamGUI(new TeamGUI(p).getInventory());
					    		new GUIManager(p).openInventory(p, new TeamGUI(p).getInventory());
			    			}
			    		}
		    		}
		    	}		    	
		    } else if(clickedItem.getItemMeta().getDisplayName().contains(noneItem)) {
		    	if(inATeam.checkTeam(player.getName()) == true) {
		    		if(inATeam.checkSpecificTeam(player.getName(), "team1") == true) {
			    		List<String> actualTeam = new ArrayList<>();
			    		for(String pl : board.getTeam("team1").getEntries()) {
			    			actualTeam.add(pl);
			    		}
			    		TeamManager.removeMemberFromTeam(player, "team1");
		    		} else if(inATeam.checkSpecificTeam(player.getName(), "team2") == true) {
			    		List<String> actualTeam = new ArrayList<>();
			    		for(String pl : board.getTeam("team2").getEntries()) {
			    			actualTeam.add(pl);
			    		}
			    		TeamManager.removeMemberFromTeam(player, "team2");
		    		}
					String msg = messagesFormatter.formatTeamMessage(messages.getConfigurationSection("events.teamChange").getString("leave"), new CastleTeam("team1"));
		    		player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
		    		player.setDisplayName(ChatColor.translateAlternateColorCodes('&', player.getName()));
		    		player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', player.getName()));
		    		for(Player p : Bukkit.getOnlinePlayers()) {
		    			if(p.getOpenInventory().getTitle().equalsIgnoreCase(e.getView().getTitle())) {
				    		new GUIManager(p).initializeTeamGUI(new TeamGUI(p).getInventory());
				    		new GUIManager(p).openInventory(p, new TeamGUI(p).getInventory());
		    			}
		    		}
		    	}
		    }
	    	
	    }

	}

}
