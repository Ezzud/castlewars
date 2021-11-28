package fr.ezzud.castlewar.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;

public class FoodLevel implements Listener {
	
    Main plugin;
    public FoodLevel(Main instance) {
        plugin = instance;
    }
    
	   @EventHandler
	   public void onFoodLoss(FoodLevelChangeEvent event) {
		   if (event.getEntity() instanceof Player) {
			   if(GameStateManager.getGameState() == false) {
				   event.setCancelled(true);
			   } else {
				   if(plugin.getConfig().getConfigurationSection("gameRules").getBoolean("foodLoss") == false) {
					   event.setCancelled(true);  
				   }
			   }
		   }
	   }   
}
