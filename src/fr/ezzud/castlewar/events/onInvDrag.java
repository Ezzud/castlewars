package fr.ezzud.castlewar.events;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.methods.configManager;
import net.md_5.bungee.api.ChatColor;

public class onInvDrag implements Listener {
    Main plugin;
	YamlConfiguration guis = configManager.getGUIs();
	ConfigurationSection chooseTeam = guis.getConfigurationSection("chooseTeam");
    public onInvDrag(Main instance) {
        plugin = instance;
    }
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
    	if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', chooseTeam.getString("title")))) {
          e.setCancelled(true);
        }
    	String[] itemInfo = plugin.getConfig().getString("teamChooseItem").split(",");
     if (e.getOldCursor().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', itemInfo[2]))) {
         if(e.getOldCursor().getType() == Material.valueOf(itemInfo[0])) {
        	 e.setCancelled(true);
        	 e.getWhoClicked().getItemOnCursor().setAmount(0);;
         }
    	 
     }   	
    }
}
