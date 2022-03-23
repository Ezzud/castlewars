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
			  	case SKELETON:
			  	case SLIME:
			  	case ENDERMAN:
			  	case PILLAGER:
			  	case CREEPER:
			  	case SPIDER:
			  	case VEX:
			  	case EVOKER:
			  	case ENDERMITE:
			  	case BLAZE:
			  	case WITHER_SKELETON:
			  	case ZOGLIN:
			  	case ZOMBIE_VILLAGER:
			  	case STRAY:
			  	case VINDICATOR:
			  	case DROWNED: 
			  	case PIGLIN:
			  	case MAGMA_CUBE:
			  	case PHANTOM:
			  	case PIGLIN_BRUTE:
			  	case SILVERFISH:
			  	case HUSK:
			  	case HOGLIN:
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
			  	case COW:
			  	case BAT:
			  	case PIG:
			  	case CHICKEN:
			  	case OCELOT:
			  	case WOLF:
			  	case SQUID:
			  	case CAT:
			  	case RABBIT:
			  	case HORSE:
			  	case SKELETON_HORSE:
			  	case DONKEY:
			  	case ZOMBIE_HORSE:
			  	case VILLAGER:
			  	case IRON_GOLEM:
			  	case MULE:
			  	case COD:
			  	case DOLPHIN:
			  	case FOX:
			  	case BEE:
			  	case SALMON:
			  	case PUFFERFISH:
			  	case POLAR_BEAR:
			  	case TROPICAL_FISH:
			  	case STRIDER:
			  	case LLAMA:
			  	case WANDERING_TRADER:
			  		event.setCancelled(true);
			  		break;
			  	default:
			  		break;
				 
			   }
			   
		   }
	   }
}
