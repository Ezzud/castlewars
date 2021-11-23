package fr.ezzud.castlewar.events;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
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
import net.md_5.bungee.api.ChatColor;

public class Join implements Listener {
    Main plugin;
    public Join(Main instance) {
        plugin = instance;
    }	

    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
    	e.setJoinMessage(null);
        	if(GameStateManager.getGameState() == false) {
        		Player player = e.getPlayer();
        		String[] itemInfo = plugin.getConfig().getString("teamChooseItem").split(",");
        		String[] coordsStr = plugin.getConfig().getString("lobby_spawnpoint").split(",");
        		Location coords = new Location(Bukkit.getWorld(coordsStr[5]), Double.parseDouble(coordsStr[0]), Double.parseDouble(coordsStr[1]), Double.parseDouble(coordsStr[2]), Float.parseFloat(coordsStr[3]), Float.parseFloat(coordsStr[4]));
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
        		final ItemStack item = new ItemStack(Material.valueOf(itemInfo[0]), 1, Byte.parseByte(itemInfo[1]));
                final ItemMeta meta = item.getItemMeta();
                
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemInfo[2]));
                item.setItemMeta(meta);
        		player.getInventory().setItem(0, item);
        		
        		if(Bukkit.getOnlinePlayers().size() >= plugin.getConfig().getInt("minPlayers")) {
  		    	  CountdownTimer timer = new CountdownTimer(plugin,
		    		        60,
		    		        () ->  {
		    		        	Bukkit.broadcastMessage("Starting in 60...");
		    		        },
		    		        () -> {    
		    		        	Bukkit.broadcastMessage("Started"); 
		    		        	new GameStateManager().startGame();
		    		        	for(Player p : Bukkit.getOnlinePlayers()) {
		    		        		p.setLevel(0);
		    		        	}		    		        	
		    		        },
		    		        (t) -> {
		    		        	if(Bukkit.getOnlinePlayers().size() < plugin.getConfig().getInt("minPlayers")) {
		    		        		CountdownTimer.cancelTimer();
		    		        		Bukkit.broadcastMessage("Stopped");
			    		        	for(Player p : Bukkit.getOnlinePlayers()) {
			    		        		p.setLevel(0);
			    		        	}
		    		        	}
		    		        	if(Bukkit.getOnlinePlayers().size() == plugin.getConfig().getInt("maxPlayers")) {
		    		        		t.reduceTimer(10);
		    		        	}
		    		        	for(Player p : Bukkit.getOnlinePlayers()) {
		    		        		p.setLevel(t.getSecondsLeft());
		    		        	}
		    		        }

		    		);
		    	  timer.scheduleTimer();
        		}
        	}
        	 
           
         
       }
}