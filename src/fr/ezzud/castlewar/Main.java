package fr.ezzud.castlewar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.InvalidConfigurationException;
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
import fr.ezzud.castlewar.events.Spawn;
import fr.ezzud.castlewar.events.VoidTP;
import fr.ezzud.castlewar.events.onInvClick;
import fr.ezzud.castlewar.events.onInvDrag;
import fr.ezzud.castlewar.methods.checkUpdate;
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
		configManager.saveMessages();
		messages = configManager.getMessages();
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', messages.getString("commands.prefix") + " &bmessages.yml &aloaded"));
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', messages.getString("commands.prefix") + " &bconfig.yml &aloaded"));
		configManager.saveKits();
		kits = configManager.getKits();
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', messages.getString("commands.prefix") + " &bkits.yml &aloaded"));
		configManager.saveGUIs();
		guis = configManager.getGUIs();
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', messages.getString("commands.prefix") + " &bguis.yml &aloaded"));
		configManager.saveData();
		data = configManager.getData();
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', messages.getString("commands.prefix") + " &bdata.yml &aloaded"));
		
		new TeamManager().initializeTeams();
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
		pm.registerEvents(new VoidTP(this), this);
		pm.registerEvents(new Spawn(this), this);
		this.getCommand("castlewar").setExecutor(new CommandHandler());
		this.getCommand("castlewar").setTabCompleter(new TabCompletion());
		File f = new File(plugin.getConfig().getString("game_world") + "-castlewar");
		if(!f.exists()) {
			if(!plugin.getConfig().getString("game_world").equalsIgnoreCase("world") && Bukkit.getWorld(plugin.getConfig().getString("game_world")) == null) {
				new WorldCreator(plugin.getConfig().getString("game_world")).createWorld();
			}
			worldManager.copyWorld(Bukkit.getWorld(plugin.getConfig().getString("game_world")), plugin.getConfig().getString("game_world") + "-castlewar");
			Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', messages.getString("commands.prefix") + " &aCopying new world"));
		}
		Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', messages.getString("commands.prefix") + " &aSuccessfully loaded plugin!"));
		if(Bukkit.getOnlinePlayers() != null && Bukkit.getOnlinePlayers().size() > 0) {
			new GameStateManager().stopGame();
		}
		scoreboardManager = new ScoreboardManager();
		this.startUpdateTask();
		this.checkVersion();
		
		}
	
	
	
	@Override
	public void onDisable() {
		File f = new File(plugin.getConfig().getString("game_world") + "-castlewar");
		if(f.exists()) {
			worldManager.unloadWorld(Bukkit.getWorld(plugin.getConfig().getString("game_world") + "-castlewar"));
			Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', messages.getString("commands.prefix") + " &cSuccessfully unloaded plugin!"));
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
	
	
	private void checkVersion() {
		if(this.getConfig().getBoolean("checkForUpdate") == false) return;
		String version = checkUpdate.getTextFromGithub("https://raw.githubusercontent.com/Ezzud/castlewars/main/plugin.yml");
		YamlConfiguration config = new YamlConfiguration();
		try {
			config.loadFromString(version);
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		}
		String lastVersion = config.getString("version");
		String actualVersion = this.getDescription().getVersion();
		if(lastVersion.equalsIgnoreCase(actualVersion)) {
			Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', messages.getString("commands.prefix") + " &aYou are using the latest version of CastleWars! (&e" + actualVersion + "&a)"));
		} else {
			Bukkit.getLogger().warning(ChatColor.translateAlternateColorCodes('&', messages.getString("commands.prefix") + " &cYou are using an outdated version of CastleWars! (&e" + actualVersion + "&c)"));
			Bukkit.getLogger().warning(ChatColor.translateAlternateColorCodes('&', messages.getString("commands.prefix") + " &cPlease update the plugin to the &a" + lastVersion + " &c!"));
		}
	}
}
