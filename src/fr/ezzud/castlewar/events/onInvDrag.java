package fr.ezzud.castlewar.events;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.methods.managers.configManager;
import net.md_5.bungee.api.ChatColor;

public class onInvDrag implements Listener {
    Main plugin;
	YamlConfiguration guis = configManager.getGUIs();
	ConfigurationSection chooseTeam = guis.getConfigurationSection("chooseTeam");
	ConfigurationSection chooseKit = guis.getConfigurationSection("chooseKit");
    public onInvDrag(Main instance) {
        plugin = instance;
    }
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
    	if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', chooseTeam.getString("title")))) {
    		e.setCancelled(true);
        }
    	if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', chooseKit.getString("title")))) {
            e.setCancelled(true);
        }
    	String[] teamItemInfo = plugin.getConfig().getConfigurationSection("teamChooseItem").getString("item").split(",");
    	String[] kitItemInfo = plugin.getConfig().getConfigurationSection("kitChooseItem").getString("item").split(",");
     if (e.getOldCursor().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', teamItemInfo[2]))) {
         if(e.getOldCursor().getType() == Material.valueOf(teamItemInfo[0])) {
        	 e.setCancelled(true);
        	 e.getWhoClicked().getItemOnCursor().setAmount(0);
         }
    	 
     }   
     if (e.getOldCursor().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', kitItemInfo[2]))) {
         if(e.getOldCursor().getType() == Material.valueOf(kitItemInfo[0])) {
        	 e.setCancelled(true);
        	 e.getWhoClicked().getItemOnCursor().setAmount(0);
         }
    	 
     } 
    }
}
