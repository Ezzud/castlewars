package fr.ezzud.castlewar.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.commands.players.GUI;
import net.md_5.bungee.api.ChatColor;

public class Interaction implements Listener {
    Main plugin;
    public Interaction(Main instance) {
        plugin = instance;
    }	

    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR) {
        	String[] itemInfo = plugin.getConfig().getString("teamChooseItem").split(",");
        	if(GameStateManager.getGameState() == true) return;
        	if(e.getPlayer().getItemInHand() == null) return;
         if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', itemInfo[2]))) {
             if(e.getPlayer().getItemInHand().getType() == Material.valueOf(itemInfo[0])) {
            	 e.setCancelled(true);
            	 new GUI(e.getPlayer());
             }
        	 
           }
         
         }
       }
}