package fr.ezzud.castlewar.events;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Scoreboard;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.api.TeamManager;
import fr.ezzud.castlewar.methods.inATeam;
import net.md_5.bungee.api.ChatColor;

public class Leave implements Listener {
    Main plugin;
    public Leave(Main instance) {
        plugin = instance;
    }	
    YamlConfiguration teams = Main.teams;
    static Scoreboard board = Main.board;
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
    	e.setQuitMessage(null);
    	Player player = e.getPlayer();
    	if(inATeam.checkTeam(player.getName()) == true) {
    		if(GameStateManager.GameState == true) {
        		if(inATeam.isKing(player.getName()) == true) {
        			GameStateManager.createParticles = false;
        			new GameStateManager().stopGame();
        			Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', "&cGame stopped because a king left the game"));
        			new GameStateManager().checkStart();
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
