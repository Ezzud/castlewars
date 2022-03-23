package fr.ezzud.castlewar.events;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.CastleTeam;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.api.TeamManager;
import fr.ezzud.castlewar.methods.inATeam;
import net.md_5.bungee.api.ChatColor;

public class Chat implements Listener {
	
    Main plugin;
    public Chat(Main instance) {
        plugin = instance;
    } 
    
    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
    	if(event.isCancelled()) return;
    	ConfigurationSection config = plugin.getConfig().getConfigurationSection("chatFormat");
    	if(!config.getBoolean("enabled")) return;
    	
    	Player player = event.getPlayer();
    	event.setCancelled(true);
    	if(GameStateManager.GameState) {
    		
        if(!config.getConfigurationSection("teamChat").getBoolean("enabled")) {
            	if(!inATeam.checkTeam(player.getName())) {
               		CastleTeam team = TeamManager.getNoTeam();
               		String message = ChatColor.translateAlternateColorCodes('&', config.getString("format")) ;
               		message = message.replaceAll("%teamcolor%", ChatColor.translateAlternateColorCodes('&', team.getColor()));
               		message = message.replaceAll("%teamname%", ChatColor.translateAlternateColorCodes('&', team.getName()));
               		message = message.replaceAll("%teamprefix%", ChatColor.translateAlternateColorCodes('&', team.getPrefix()));
               		message = message.replaceAll("%player%", ChatColor.translateAlternateColorCodes('&', player.getName()));
               		if(config.getBoolean("allowColors") || player.hasPermission(config.getString("colorsPermission")) || player.isOp()) {
               			message = message.replaceAll("%message%", ChatColor.translateAlternateColorCodes('&', event.getMessage()));
               		} else {
               			message = message.replaceAll("%message%", event.getMessage());
               		}
               		
               		Bukkit.broadcastMessage(message);		   
            	} else {
                	if(inATeam.whichTeam(player.getName()).equalsIgnoreCase("team1")) {
                		CastleTeam team = TeamManager.getTeam1();
                		String message = ChatColor.translateAlternateColorCodes('&', config.getString("format")) ;
                		message = message.replaceAll("%teamcolor%", ChatColor.translateAlternateColorCodes('&', team.getColor()));
                		message = message.replaceAll("%teamname%", ChatColor.translateAlternateColorCodes('&', team.getName()));
                		message = message.replaceAll("%teamprefix%", ChatColor.translateAlternateColorCodes('&', team.getPrefix()));
                		message = message.replaceAll("%player%", ChatColor.translateAlternateColorCodes('&', player.getName()));
                   		if(config.getBoolean("allowColors") || player.hasPermission(config.getString("colorsPermission")) || player.isOp()) {
                   			message = message.replaceAll("%message%", ChatColor.translateAlternateColorCodes('&', event.getMessage()));
                   		} else {
                   			message = message.replaceAll("%message%", event.getMessage());
                   		}
                		Bukkit.broadcastMessage(message);
                	} else {
                		CastleTeam team = TeamManager.getTeam2();
                		String message = ChatColor.translateAlternateColorCodes('&', config.getString("format")) ;
                		message = message.replaceAll("%teamcolor%", ChatColor.translateAlternateColorCodes('&', team.getColor()));
                		message = message.replaceAll("%teamname%", ChatColor.translateAlternateColorCodes('&', team.getName()));
                		message = message.replaceAll("%teamprefix%", ChatColor.translateAlternateColorCodes('&', team.getPrefix()));
                		message = message.replaceAll("%player%", ChatColor.translateAlternateColorCodes('&', player.getName()));
                   		if(config.getBoolean("allowColors") || player.hasPermission(config.getString("colorsPermission")) || player.isOp()) {
                   			message = message.replaceAll("%message%", ChatColor.translateAlternateColorCodes('&', event.getMessage()));
                   		} else {
                   			message = message.replaceAll("%message%", event.getMessage());
                   		}
                		Bukkit.broadcastMessage(message);
                	}       		
            	}

        	} else {
            	if(!inATeam.checkTeam(player.getName())) {
               		CastleTeam team = TeamManager.getNoTeam();
               		String message = ChatColor.translateAlternateColorCodes('&', config.getString("format")) ;
               		message = message.replaceAll("%teamcolor%", ChatColor.translateAlternateColorCodes('&', team.getColor()));
               		message = message.replaceAll("%teamname%", ChatColor.translateAlternateColorCodes('&', team.getName()));
               		message = message.replaceAll("%teamprefix%", ChatColor.translateAlternateColorCodes('&', team.getPrefix()));
               		message = message.replaceAll("%player%", ChatColor.translateAlternateColorCodes('&', player.getName()));
               		if(config.getBoolean("allowColors") || player.hasPermission(config.getString("colorsPermission")) || player.isOp()) {
               			message = message.replaceAll("%message%", ChatColor.translateAlternateColorCodes('&', event.getMessage()));
               		} else {
               			message = message.replaceAll("%message%", event.getMessage());
               		}
               		for(Player p : Bukkit.getOnlinePlayers()) {
               			if(!inATeam.checkTeam(p.getName())) {
               				p.sendMessage(message);
               			}
               		}
            	} else {
            		char prefix = event.getMessage().charAt(0);
                	if(prefix == config.getConfigurationSection("teamChat").getString("globalChatPrefix").charAt(0)) {
                		if(event.getMessage().length() <= 1) return;
                		CastleTeam team = new CastleTeam(inATeam.whichTeam(player.getName()));
                		String message = ChatColor.translateAlternateColorCodes('&', config.getConfigurationSection("teamChat").getString("globalChatFormat"));
                   		message = message.replaceAll("%teamcolor%", ChatColor.translateAlternateColorCodes('&', team.getColor()));
                   		message = message.replaceAll("%teamname%", ChatColor.translateAlternateColorCodes('&', team.getName()));
                   		message = message.replaceAll("%teamprefix%", ChatColor.translateAlternateColorCodes('&', team.getPrefix()));
                   		message = message.replaceAll("%player%", ChatColor.translateAlternateColorCodes('&', player.getName()));
                   		if(config.getBoolean("allowColors") || player.hasPermission(config.getString("colorsPermission")) || player.isOp()) {
                   			message = message.replaceAll("%message%", ChatColor.translateAlternateColorCodes('&', event.getMessage().substring(1)));
                   		} else {
                   			message = message.replaceAll("%message%", event.getMessage());
                   		}
                   		Bukkit.broadcastMessage(message);
                	} else {
                		CastleTeam team = new CastleTeam(inATeam.whichTeam(player.getName()));
                		String message = ChatColor.translateAlternateColorCodes('&', config.getConfigurationSection("teamChat").getString("privateChatFormat"));
                   		message = message.replaceAll("%teamcolor%", ChatColor.translateAlternateColorCodes('&', team.getColor()));
                   		message = message.replaceAll("%teamname%", ChatColor.translateAlternateColorCodes('&', team.getName()));
                   		message = message.replaceAll("%teamprefix%", ChatColor.translateAlternateColorCodes('&', team.getPrefix()));
                   		message = message.replaceAll("%player%", ChatColor.translateAlternateColorCodes('&', player.getName()));
                   		if(config.getBoolean("allowColors") || player.hasPermission(config.getString("colorsPermission")) || player.isOp()) {
                   			message = message.replaceAll("%message%", ChatColor.translateAlternateColorCodes('&', event.getMessage()));
                   		} else {
                   			message = message.replaceAll("%message%", event.getMessage());
                   		}
                   		for(String p : team.getMembers()) {
                   			if(p.equalsIgnoreCase("team1")) return;
                   			if(p.equalsIgnoreCase("team2")) return;
                   			Player pl = Bukkit.getPlayer(p);
                   			pl.sendMessage(message);
                   		}
                	}

            	}

        	}
    	} else {
        	if(!inATeam.checkTeam(player.getName()))
           		CastleTeam team = TeamManager.getNoTeam();
        	else if(inATeam.whichTeam(player.getName()).equalsIgnoreCase("team1"))
            		CastleTeam team = TeamManager.getTeam1();
            	else CastleTeam team = TeamManager.getTeam2();
			
		String message = ChatColor.translateAlternateColorCodes('&', config.getString("format")) ;
            	message = message.replaceAll("%teamcolor%", ChatColor.translateAlternateColorCodes('&', team.getColor()));
            	message = message.replaceAll("%teamname%", ChatColor.translateAlternateColorCodes('&', team.getName()));
            	message = message.replaceAll("%teamprefix%", ChatColor.translateAlternateColorCodes('&', team.getPrefix()));
            	message = message.replaceAll("%player%", ChatColor.translateAlternateColorCodes('&', player.getName()));
               	if(config.getBoolean("allowColors") || player.hasPermission(config.getString("colorsPermission")) || player.isOp())
               		message = message.replaceAll("%message%", ChatColor.translateAlternateColorCodes('&', event.getMessage()));
               	else
			message = message.replaceAll("%message%", event.getMessage());
            	Bukkit.broadcastMessage(message);
        	} 		
    	}

    }

}
