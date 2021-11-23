package fr.ezzud.castlewar.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import fr.ezzud.castlewar.Main;
import net.md_5.bungee.api.ChatColor;

public class Drop implements Listener {
    Main plugin;
    public Drop(Main instance) {
        plugin = instance;
    }	

    @EventHandler
    public void onPlayerInteract(PlayerDropItemEvent e) {
        	String[] itemInfo = plugin.getConfig().getConfigurationSection("kitChooseItem").getString("item").split(",");
         if (e.getItemDrop().getItemStack().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', itemInfo[2]))) {
             if(e.getItemDrop().getItemStack().getType() == Material.valueOf(itemInfo[0])) {
            	 e.setCancelled(true);
             }
        	 
           
         }
       }
}