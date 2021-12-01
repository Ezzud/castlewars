package fr.ezzud.castlewar.events;

import org.bukkit.entity.Arrow;
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
		   if(event.isCancelled() == true) return;
		   if (event.getEntity() instanceof Player) {
			   if(GameStateManager.getGameState() == false) {
				   event.setCancelled(true);
			   }
		   }
	   }
	   @EventHandler
	   public void onPlayerDamage(EntityDamageByEntityEvent event) {
		   if(event.isCancelled() == true) return;
		   if(GameStateManager.getGameState() == false) return;
		   if(event.getEntity() instanceof Player) {
			   if(event.getDamager() instanceof Player) {
				   Player damager = (Player) event.getDamager();
				   Player victim = (Player) event.getEntity();
				   if(inATeam.whichTeam(victim.getName()).equals(inATeam.whichTeam(damager.getName()))) {
					   if(event.isCancelled() == false) {
						   event.setCancelled(true);  
					   }
					   
				   }
			   }
			   if(event.getDamager() instanceof Arrow) {
				   Arrow arrow = (Arrow) event.getDamager();
				   if(arrow.getShooter() instanceof Player) {
					   Player shooter = (Player) ((Arrow) arrow).getShooter();
					   Player target = (Player) event.getEntity();
					   if(inATeam.whichTeam(shooter.getName()).equals(inATeam.whichTeam(target.getName()))) {
						   if(event.isCancelled() == false) {
							   event.setCancelled(true);  
						   }
						   
					   }
				   }
			   }
		   }
		   

	   }
	
}
