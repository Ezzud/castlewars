package fr.ezzud.castlewar.events;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.CastlePlayer;
import fr.ezzud.castlewar.api.CastleTeam;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.api.TeamManager;
import fr.ezzud.castlewar.api.events.CWendEvent;
import fr.ezzud.castlewar.methods.inATeam;
import fr.ezzud.castlewar.methods.messagesFormatter;
import net.md_5.bungee.api.ChatColor;

public class Leave implements Listener {
    Main plugin;
    public Leave(Main instance) {
        plugin = instance;
    }	
    YamlConfiguration teams = Main.teams;
    YamlConfiguration messages = Main.messages;
    static Scoreboard board = Main.board;
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
    	if(plugin.getConfig().getConfigurationSection("joinLeaveMessages").getBoolean("enabled") == true) {
    		e.setQuitMessage(null);
    	}
    	Player player = e.getPlayer();
    	if(GameStateManager.GameState == false) {
        	if(plugin.getConfig().getConfigurationSection("joinLeaveMessages").getBoolean("enabled") == true) {
        		String joinmsg = messagesFormatter.formatJoinMessage(plugin.getConfig().getConfigurationSection("joinLeaveMessages").getString("leave"), player, Bukkit.getOnlinePlayers().size() - 1);
        		Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', joinmsg));
        	}
    	}
    	
    	if(inATeam.checkTeam(player.getName()) == true) {
    		if(GameStateManager.GameState == true) {
        		if(inATeam.isKing(player.getName()) == true) {
        			GameStateManager.createParticles = false;
        			new GameStateManager().stopGame();
        			CWendEvent event = new CWendEvent(new CastleTeam("team1"), new CastleTeam("team2"), new CastlePlayer(GameStateManager.getTeam1King()), new CastlePlayer(GameStateManager.getTeam2King()), "leave");
					Bukkit.getPluginManager().callEvent(event);	
        			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("events.death.king").getString("leave"))));
        		}  			
    		} else {
        		if(inATeam.checkSpecificTeam(player.getName(), "team1") == true) {
        	    	TeamManager.removeMemberFromTeam(player, "team1");
        		}
        		if(inATeam.checkSpecificTeam(player.getName(), "team2") == true) {
        			TeamManager.removeMemberFromTeam(player, "team2");
        		}
    		}

    	}
    }
}
