package fr.ezzud.castlewar.events;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.methods.inATeam;

public class Leave implements Listener {
    Main plugin;
    public Leave(Main instance) {
        plugin = instance;
    }	
    YamlConfiguration teams = Main.teams;

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
    	e.setQuitMessage(null);
    	Player player = e.getPlayer();
    	if(inATeam.checkTeam(player.getName()) == true) {
    		if(inATeam.checkSpecificTeam(player.getName(), "team1") == true) {
    			List<String> list = teams.getStringList("team1");
    			list.remove(player.getName());
    			teams.set("team1", list);
    			try {
					teams.save(new File("plugins/CastleWars/teams.yml"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
    		}
    		if(inATeam.checkSpecificTeam(player.getName(), "team2") == true) {
    			List<String> list = teams.getStringList("team2");
    			list.remove(player.getName());
    			teams.set("team2", list);
    			try {
					teams.save(new File("plugins/CastleWars/teams.yml"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
    		}
    	}
    }
}
