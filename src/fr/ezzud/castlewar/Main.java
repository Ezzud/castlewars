package fr.ezzud.castlewar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;

import fr.ezzud.castlewar.api.GameStateManager;
import fr.ezzud.castlewar.api.TeamManager;
import fr.ezzud.castlewar.commands.CommandHandler;
import fr.ezzud.castlewar.commands.TabCompletion;
import fr.ezzud.castlewar.events.Chat;
import fr.ezzud.castlewar.events.Damage;
import fr.ezzud.castlewar.events.Death;
import fr.ezzud.castlewar.events.Drop;
import fr.ezzud.castlewar.events.FoodLevel;
import fr.ezzud.castlewar.events.Interaction;
import fr.ezzud.castlewar.events.Join;
import fr.ezzud.castlewar.events.Leave;
import fr.ezzud.castlewar.events.PlaceBreak;
import fr.ezzud.castlewar.events.onInvClick;
import fr.ezzud.castlewar.events.onInvDrag;
import fr.ezzud.castlewar.methods.managers.ScoreboardManager;
import fr.ezzud.castlewar.methods.managers.configManager;
import fr.ezzud.castlewar.methods.managers.worldManager;

public class Main extends JavaPlugin implements Listener {
	  private static Main plugin;
	  public static YamlConfiguration teams;
	  public static YamlConfiguration kits;
	  public static YamlConfiguration guis;
	  public static YamlConfiguration data;
	  public static YamlConfiguration messages;
	  public static org.bukkit.scoreboard.ScoreboardManager manager;
	  public static Scoreboard board;
	  public static boolean starting;
	  public static fr.ezzud.castlewar.methods.managers.ScoreboardManager scoreboardManager;
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
	   public static YamlConfiguration getData() {
		      return data;
	   }
	   public static YamlConfiguration getMessages() {
		      return messages;
	   }
	   
	   
	@Override
	public void onEnable() {
		GameStateManager.GameState = false;
		GameStateManager.GameStateText = "waiting";
		GameStateManager.startDate = null;
		plugin = this;
		starting = false;
		manager = plugin.getServer().getScoreboardManager();
		board = manager.getMainScoreboard();
		this.saveDefaultConfig();
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&6[&eCastleWars&6] &bconfig.yml &aloaded"));
		configManager.saveKits();
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&6[&eCastleWars&6] &bkits.yml &aloaded"));
		kits = configManager.getKits();
		new TeamManager().initializeTeams();
		configManager.saveGUIs();
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&6[&eCastleWars&6] &bguis.yml &aloaded"));
		guis = configManager.getGUIs();
		configManager.saveData();
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&6[&eCastleWars&6] &bdata.yml &aloaded"));
		data = configManager.getData();
		configManager.saveMessages();
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&6[&eCastleWars&6] &bmessages.yml &aloaded"));
		messages = configManager.getMessages();
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);
		pm.registerEvents(new onInvClick(this), this);
		pm.registerEvents(new onInvDrag(this), this);
		pm.registerEvents(new Join(this), this);
		pm.registerEvents(new Leave(this), this);
		pm.registerEvents(new Interaction(this), this);
		pm.registerEvents(new Drop(this), this);
		pm.registerEvents(new Damage(this), this);
		pm.registerEvents(new FoodLevel(this), this);
		pm.registerEvents(new Death(this), this);
		pm.registerEvents(new Chat(this), this);
		pm.registerEvents(new PlaceBreak(this), this);
		this.getCommand("castlewar").setExecutor(new CommandHandler());
		this.getCommand("castlewar").setTabCompleter(new TabCompletion());
		File f = new File(plugin.getConfig().getString("game_world") + "-castlewar");
		if(!f.exists()) {
			worldManager.copyWorld(Bukkit.getWorld(plugin.getConfig().getString("game_world")), plugin.getConfig().getString("game_world") + "-castlewar");
		}
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&6[&eCastleWars&6] &aSuccessfully loaded plugin!"));
		if(Bukkit.getOnlinePlayers() != null && Bukkit.getOnlinePlayers().size() > 0) {
			new GameStateManager().stopGame();
		}
		scoreboardManager = new ScoreboardManager();
		this.startUpdateTask();
		}
	
	
	
	@Override
	public void onDisable() {
		File f = new File(plugin.getConfig().getString("game_world") + "-castlewar");
		if(f.exists()) {
			worldManager.unloadWorld(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"));
			Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "&6[&eCastleWars&6] &cSuccessfully unloaded plugin!"));
			try {
				Path path = Paths.get(plugin.getConfig().getString("game_world") + "-castlewar");
				worldManager.deleteDirectoryStream(path);
			} catch (IOException e) {
				e.printStackTrace();
			}		
		}

	}
	
	
	private void startUpdateTask() {
		Bukkit.getScheduler().runTaskTimer(this, () -> {
			scoreboardManager.getPlayerScoreboards().values().forEach((scoreboard) -> scoreboard.update());
		}, 0L, 20L); // Very fast, every tick.
	}
}
