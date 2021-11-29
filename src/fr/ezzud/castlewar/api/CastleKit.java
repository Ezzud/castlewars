package fr.ezzud.castlewar.api;

import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import fr.ezzud.castlewar.Main;

public class CastleKit {
	public YamlConfiguration data = Main.data;
	public YamlConfiguration kits = Main.kits;
	private ConfigurationSection items;
	private ConfigurationSection helmet;
	private ConfigurationSection armor;
	private List<String> effects;
	private String description;
	private String name;
	private ConfigurationSection chestplate;
	private ConfigurationSection leggings;
	private ConfigurationSection boots;
	
	public CastleKit(String name) {
		if(kits.getConfigurationSection("kits." + name) != null) {
			this.items = kits.getConfigurationSection("kits." + name).getConfigurationSection("items");
			this.name = kits.getConfigurationSection("kits." + name).getString("name");
			this.description = kits.getConfigurationSection("kits." + name).getString("description");
			this.effects = kits.getConfigurationSection("kits." + name).getStringList("effects");
			this.armor = kits.getConfigurationSection("kits." + name).getConfigurationSection("armor");
			this.helmet = this.armor.getConfigurationSection("helmet");
			this.chestplate = this.armor.getConfigurationSection("chestplate");
			this.leggings = this.armor.getConfigurationSection("leggings");
			this.boots = this.armor.getConfigurationSection("boots");			
		}	
	}
	
	public ConfigurationSection getItems() {
		return this.items;	
	}
	public String getName() {
		return this.name;	
	}
	public String getDescription() {
		return this.description;	
	}
	public List<String> getEffects() {
		return this.effects;	
	}
	public ConfigurationSection getArmor() {
		return this.armor;	
	}
	public ConfigurationSection getHelmet() {
		return this.helmet;	
	}
	public ConfigurationSection getChestplate() {
		return this.chestplate;	
	}
	public ConfigurationSection getLeggings() {
		return this.leggings;	
	}
	public ConfigurationSection getBoots() {
		return this.boots;	
	}
}
