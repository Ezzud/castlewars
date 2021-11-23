package fr.ezzud.castlewar;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.commands.CommandHandler;
import fr.ezzud.castlewar.events.Damage;
import fr.ezzud.castlewar.events.Drop;
import fr.ezzud.castlewar.events.FoodLevel;
import fr.ezzud.castlewar.events.Interaction;
import fr.ezzud.castlewar.events.Join;
import fr.ezzud.castlewar.events.Leave;
import fr.ezzud.castlewar.events.onInvClick;
import fr.ezzud.castlewar.events.onInvDrag;
import fr.ezzud.castlewar.methods.configManager;

public class Main extends JavaPlugin implements Listener {
	  private static Main plugin;
	  public static YamlConfiguration teams;
	  public static YamlConfiguration kits;
	  public static YamlConfiguration guis;
	   public static Main getInstance() {
		      return plugin;
	   }

	   public static YamlConfiguration getTeams() {
		      return teams;
	   }
	   public static YamlConfiguration getKits() {
		      return kits;
	   }
	   public static YamlConfiguration getGUIs() {
		      return guis;
	   }
	   
	@Override
	public void onEnable() {
		GameStateManager.GameState = false;
		plugin = this;
		this.saveDefaultConfig();
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&6[&eCastleWars&6] &bconfig.yml &aloaded"));
		configManager.saveKits();
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&6[&eCastleWars&6] &bkits.yml &aloaded"));
		kits = configManager.getKits();
		configManager.saveForceTeams();
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&6[&eCastleWars&6] &bteams.yml &aloaded"));
		teams = configManager.getTeams();
		configManager.saveGUIs();
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&6[&eCastleWars&6] &bguis.yml &aloaded"));
		guis = configManager.getGUIs();
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new onInvClick(this), this);
		pm.registerEvents(new onInvDrag(this), this);
		pm.registerEvents(new Join(this), this);
		pm.registerEvents(new Interaction(this), this);
		pm.registerEvents(new Drop(this), this);
		pm.registerEvents(new Damage(this), this);
		pm.registerEvents(new FoodLevel(this), this);
		pm.registerEvents(new Leave(this), this);
		this.getCommand("castlewar").setExecutor(new CommandHandler());
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&6[&eCastleWars&6] &aSuccessfully loaded plugin!"));
	}
}
