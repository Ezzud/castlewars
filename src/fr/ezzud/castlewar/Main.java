package fr.ezzud.castlewar;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import fr.ezzud.castlewar.api.TeamManager;

public class Main extends JavaPlugin implements Listener {
	  private static Main plugin;
	  private static YamlConfiguration teams;
	   public static Main getInstance() {
		      return plugin;
	   }

	   public static FileConfiguration getTeamFile() {
		      return teams;
	   }


	   
	@Override
	public void onEnable() {
		plugin = this;
		this.saveDefaultConfig();
		YamlConfiguration config = null;
		File f = new File("plugins/CastleWars/teams.yml");
		if(!f.exists()) {
			this.saveResource("teams.yml", false);
		}
		InputStream is = this.getClass().getResourceAsStream("/teams.yml");
		Reader rd = new InputStreamReader(is);
		config = YamlConfiguration.loadConfiguration(rd);
		teams = config;
		Bukkit.getServer().getPluginManager().registerEvents(this, this);
	    Bukkit.getLogger().info("Plugin launched!");
	    TeamManager manager = new TeamManager();
	    Bukkit.getLogger().info(manager.getTeam1().getName());
	}
}
