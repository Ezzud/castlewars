package fr.ezzud.castlewar.events;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.commands.players.kitsCMD;
import fr.ezzud.castlewar.commands.players.teamCMD;
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
        	if(GameStateManager.getGameState() == true) return;
        	if(e.getPlayer().getItemInHand() == null) return;
        	String[] teamItemInfo = plugin.getConfig().getConfigurationSection("teamChooseItem").getString("item").split(",");
        	String[] kitItemInfo = plugin.getConfig().getConfigurationSection("kitChooseItem").getString("item").split(",");
        	if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', teamItemInfo[2]))) {
             if(e.getPlayer().getItemInHand().getType() == Material.valueOf(teamItemInfo[0])) {
            	 e.setCancelled(true);
            	 new teamCMD(e.getPlayer());
             }
        	 
           }
        	if (e.getPlayer().getItemInHand().getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', kitItemInfo[2]))) {
                if(e.getPlayer().getItemInHand().getType() == Material.valueOf(kitItemInfo[0])) {
               	 e.setCancelled(true);
               	 new kitsCMD(e.getPlayer());
                }
           	 
              }         
         }
       }
}