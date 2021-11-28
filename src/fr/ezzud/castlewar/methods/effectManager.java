package fr.ezzud.castlewar.methods;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;

public class effectManager {
	Main plugin = Main.getInstance();
	ConfigurationSection config = plugin.getConfig().getConfigurationSection("kingConfig");
	public void createSpiralAroundPlayer(Player player) {
		if(config.getBoolean("particlesEnabled") == true) {
	        new BukkitRunnable(){
	            public void run(){
	                Location loc = player.getLocation();
	                loc.setY(loc.getY() + 2);
	                World world = loc.getWorld();
	                world.spawnParticle(Particle.valueOf(config.getString("particles")), loc, 1);
	                
	                if(GameStateManager.createParticles == false){
	                    this.cancel(); // Stop spawning particles.
	                }
	            }
	        }.runTaskTimer(plugin, 0, 5);
		}
    }
	
	public void applyGlowing(Player player) {
		if(config.getBoolean("glowingEnabled") == true) {
			String a = plugin.getServer().getClass().getPackage().getName();
			String version = a.substring(a.lastIndexOf('.') + 1);
			if(!version.equalsIgnoreCase("v1_8_R1")){
				player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 999999*20, 0, true));
			}		
		}

	}
}
