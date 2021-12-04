package fr.ezzud.castlewar.events;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;

import fr.ezzud.castlewar.Main;

public class Spawn implements Listener {
	
    Main plugin;
    public Spawn(Main instance) {
        plugin = instance;
    }
    

	   @EventHandler
	   public void onSpawn(EntitySpawnEvent event) {
		   if(event.isCancelled() == true) return;
		   ConfigurationSection config = plugin.getConfig().getConfigurationSection("gameRules");
		   if(config.getBoolean("mobSpawning") == false) {
			  switch(event.getEntityType()) {
			  	case ZOMBIE:
			  		event.setCancelled(true);
			  		break;
			  	case SKELETON:
			  		event.setCancelled(true);
			  		break;
			  	case SLIME:
			  		event.setCancelled(true);
			  		break;
			  	case ENDERMAN:
			  		event.setCancelled(true);
			  		break;
			  	case PILLAGER:
			  		event.setCancelled(true);
			  		break;
			  	case CREEPER:
			  		event.setCancelled(true);
			  		break;
			  	case SPIDER:
			  		event.setCancelled(true);
			  		break;
			  	case VEX:
			  		event.setCancelled(true);
			  		break;
			  	case EVOKER:
			  		event.setCancelled(true);
			  		break;
			  	case ENDERMITE:
			  		event.setCancelled(true);
			  		break;
			  	case BLAZE:
			  		event.setCancelled(true);
			  		break;
			  	case WITHER_SKELETON:
			  		event.setCancelled(true);
			  		break;
			  	case ZOGLIN:
			  		event.setCancelled(true);
			  		break;
			  	case ZOMBIE_VILLAGER:
			  		event.setCancelled(true);
			  		break;
			  	case STRAY:
			  		event.setCancelled(true);
			  		break;
			  	case VINDICATOR:
			  		event.setCancelled(true);
			  		break;
			  	case DROWNED: 
			  		event.setCancelled(true);
			  		break;
			  	case PIGLIN:
			  		event.setCancelled(true);
			  		break;
			  	case MAGMA_CUBE:
			  		event.setCancelled(true);
			  		break;
			  	case PHANTOM:
			  		event.setCancelled(true);
			  		break;
			  	case PIGLIN_BRUTE:
			  		event.setCancelled(true);
			  		break;
			  	case SILVERFISH:
			  		event.setCancelled(true);
			  		break;
			  	case HUSK:
			  		event.setCancelled(true);
			  		break;
			  	case HOGLIN:
			  		event.setCancelled(true);
			  		break;
			  	case WITCH:
			  		event.setCancelled(true);
			  		break;
			  	default:
			  		break;
				 
			   }
			   
		   }
		   if(config.getBoolean("animalSpawning") == false) {
			  switch(event.getEntityType()) {
			  	case SHEEP:
			  		event.setCancelled(true);
			  		break;
			  	case COW:
			  		event.setCancelled(true);
			  		break;
			  	case BAT:
			  		event.setCancelled(true);
			  		break;
			  	case PIG:
			  		event.setCancelled(true);
			  		break;
			  	case CHICKEN:
			  		event.setCancelled(true);
			  		break;
			  	case OCELOT:
			  		event.setCancelled(true);
			  		break;
			  	case WOLF:
			  		event.setCancelled(true);
			  		break;
			  	case SQUID:
			  		event.setCancelled(true);
			  		break;
			  	case CAT:
			  		event.setCancelled(true);
			  		break;
			  	case RABBIT:
			  		event.setCancelled(true);
			  		break;
			  	case HORSE:
			  		event.setCancelled(true);
			  		break;
			  	case SKELETON_HORSE:
			  		event.setCancelled(true);
			  		break;
			  	case DONKEY:
			  		event.setCancelled(true);
			  		break;
			  	case ZOMBIE_HORSE:
			  		event.setCancelled(true);
			  		break;
			  	case VILLAGER:
			  		event.setCancelled(true);
			  		break;
			  	case IRON_GOLEM:
			  		event.setCancelled(true);
			  		break;
			  	case MULE:
			  		event.setCancelled(true);
			  		break;
			  	case COD:
			  		event.setCancelled(true);
			  		break;
			  	case DOLPHIN:
			  		event.setCancelled(true);
			  		break;
			  	case FOX:
			  		event.setCancelled(true);
			  		break;
			  	case BEE:
			  		event.setCancelled(true);
			  		break;
			  	case SALMON:
			  		event.setCancelled(true);
			  		break;
			  	case PUFFERFISH:
			  		event.setCancelled(true);
			  		break;
			  	case POLAR_BEAR:
			  		event.setCancelled(true);
			  		break;
			  	case TROPICAL_FISH:
			  		event.setCancelled(true);
			  		break;
			  	case STRIDER:
			  		event.setCancelled(true);
			  		break;
			  	case LLAMA:
			  		event.setCancelled(true);
			  		break;
			  	case WANDERING_TRADER:
			  		event.setCancelled(true);
			  		break;
			  	default:
			  		break;
				 
			   }
			   
		   }
	   }
}