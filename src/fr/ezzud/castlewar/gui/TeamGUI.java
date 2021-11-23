package fr.ezzud.castlewar.gui;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.methods.GUIManager;
import fr.ezzud.castlewar.methods.configManager;
import net.md_5.bungee.api.ChatColor;

public class TeamGUI {
	static Main plugin = Main.getInstance();
	private final Inventory inv;
	private Inventory inventory;
	YamlConfiguration guis = configManager.getGUIs();
	ConfigurationSection chooseTeam = guis.getConfigurationSection("chooseTeam");
    public TeamGUI(Player player) {  
        inv = Bukkit.createInventory(null, chooseTeam.getInt("rows") * 9, ChatColor.translateAlternateColorCodes('&', chooseTeam.getString("title")));
        new GUIManager(player).initializeTeamGUI(inv);
        this.inventory = inv;
    }
    
    
    
    public Inventory getInventory() {
    	return this.inventory;
    }
    
}
