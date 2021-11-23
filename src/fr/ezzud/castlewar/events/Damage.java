package fr.ezzud.castlewar.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;


import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.methods.inATeam;

public class Damage implements Listener {
	
    Main plugin;
    public Damage(Main instance) {
        plugin = instance;
    }
    
	   @EventHandler
	   public void onEntityDamage(EntityDamageEvent event) {
		   if (event.getEntity() instanceof Player) {
			   if(GameStateManager.getGameState() == false) {
				   event.setCancelled(true);
			   }
		   }
	   }
	   @EventHandler
	   public void onPlayerDamage(EntityDamageByEntityEvent event) {
		   if(GameStateManager.getGameState() == false) return;
		   if(event.getEntity() instanceof Player) {
			   if(event.getDamager() instanceof Player) {
				   Player damager = (Player) event.getDamager();
				   Player victim = (Player) event.getEntity();
				   if(inATeam.whichTeam(victim.getName()).equals(inATeam.whichTeam(damager.getName()))) {
					   event.setCancelled(true);
				   }
			   }
		   }
	   }
	
}
