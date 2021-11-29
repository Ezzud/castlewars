package fr.ezzud.castlewar.events;

import org.bukkit.GameMode;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;

public class PlaceBreak implements Listener {
	
    Main plugin;
    public PlaceBreak(Main instance) {
        plugin = instance;
    }
    

	   @EventHandler
	   public void onBreak(BlockBreakEvent event) {
		   if(event.isCancelled() == true) return;
		   if(GameStateManager.getGameState() == false) return;
		   ConfigurationSection config = plugin.getConfig().getConfigurationSection("gameRules");
		   if(config.getBoolean("allowBreak") == false) {
			   if(event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
			   event.setCancelled(true);
		   }
	   }

	   @EventHandler
	   public void onPlace(BlockPlaceEvent event) {
		   if(event.isCancelled() == true) return;
		   if(GameStateManager.getGameState() == false) return;
		   ConfigurationSection config = plugin.getConfig().getConfigurationSection("gameRules");
		   if(config.getBoolean("allowBuild") == false) {
			   if(event.getPlayer().getGameMode() == GameMode.CREATIVE) return;
			   event.setCancelled(true);
		   }
	   }
	   
	   
	   @EventHandler
	   public void onExplode(EntityExplodeEvent event) {
		   if(event.isCancelled() == true) return;
		   if(GameStateManager.getGameState() == false) event.setCancelled(true);
		   ConfigurationSection config = plugin.getConfig().getConfigurationSection("gameRules");
		   if(config.getBoolean("explosions") == false) {
			   event.setCancelled(true);
		   }
	   }
	   
}
