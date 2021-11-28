package fr.ezzud.castlewar.methods;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.ezzud.castlewar.Main;


public class configManager{
	static Main plugin = Main.getInstance();
	
	public configManager() {
	}
			
	public static void saveKits() {
		File f = new File("plugins/CastleWars/kits.yml");
		if(!f.exists()) {
			plugin.saveResource("kits.yml", false);
		}
	}	
	
	public static void saveGUIs() {
		File f = new File("plugins/CastleWars/guis.yml");
		if(!f.exists()) {
			plugin.saveResource("guis.yml", false);
		}
	}
	
	public static void saveData() {
		File f = new File("plugins/CastleWars/data.yml");
		if(!f.exists()) {
			plugin.saveResource("data.yml", false);
		}
	}
	
	public static YamlConfiguration getKits() {
		saveKits();
		InputStream is = null;
		try {is = new FileInputStream("plugins/CastleWars/kits.yml");} catch (FileNotFoundException e) {e.printStackTrace();}
		Reader rd = new InputStreamReader(is);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(rd);
		return config;
	}
	
	
	public static YamlConfiguration getGUIs(){
		saveGUIs();
		InputStream is = null;
		try {is = new FileInputStream("plugins/CastleWars/guis.yml");} catch (FileNotFoundException e) {e.printStackTrace();}
		Reader rd = new InputStreamReader(is);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(rd);
		return config;
	}	
	
	public static YamlConfiguration getData(){
		saveData();
		InputStream is = null;
		try {is = new FileInputStream("plugins/CastleWars/data.yml");} catch (FileNotFoundException e) {e.printStackTrace();}
		Reader rd = new InputStreamReader(is);
		YamlConfiguration config = YamlConfiguration.loadConfiguration(rd);
		return config;
	}
	
}
