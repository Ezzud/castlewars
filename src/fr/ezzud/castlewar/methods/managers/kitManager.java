package fr.ezzud.castlewar.methods.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import fr.ezzud.castlewar.Main;
import net.md_5.bungee.api.ChatColor;

public class kitManager {
	static Main plugin = Main.getInstance();
	YamlConfiguration kits = Main.kits;
	YamlConfiguration team = Main.teams;
	YamlConfiguration kitData = Main.data;
	
	@SuppressWarnings("deprecation")
	public void setKingKit(Player player) {
		ConfigurationSection kingKit = kits.getConfigurationSection("king");
		ConfigurationSection items = kingKit.getConfigurationSection("items");
		int itemNum = items.getKeys(false).size();
		for(int g = 0; g < itemNum; g++) {
         	    ConfigurationSection itemInfo = items.getConfigurationSection(String.valueOf(g));
         	   ItemStack item = new ItemStack(Material.getMaterial(itemInfo.getString("material").split(":")[0]), itemInfo.getInt("quantity"), Byte.parseByte(itemInfo.getString("material").split(":")[1]));	
             	ItemMeta meta = item.getItemMeta();
             	meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemInfo.getString("name")));
             	for(String enchant : itemInfo.getStringList("enchantments")) {
             		String[] enchantInfo = enchant.split(",");
             		meta.addEnchant(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]), true);
             	}
             	item.setItemMeta(meta);
         	    player.getInventory().addItem(item); 
         	    
		}
		for(String effect : kingKit.getStringList("effects")) {
			String[] effectA = effect.split(",");
			PotionEffect potion = new PotionEffect(PotionEffectType.getByName(effectA[0]), Integer.parseInt(effectA[1]) * 20, Integer.parseInt(effectA[2]), true);
			player.addPotionEffect(potion);
		}
		ConfigurationSection helmet = kingKit.getConfigurationSection("armor").getConfigurationSection("helmet");
		ConfigurationSection chestplate = kingKit.getConfigurationSection("armor").getConfigurationSection("chestplate");
		ConfigurationSection leggings = kingKit.getConfigurationSection("armor").getConfigurationSection("leggings");
		ConfigurationSection boots = kingKit.getConfigurationSection("armor").getConfigurationSection("boots");
  	   ItemStack helmetItem = new ItemStack(Material.getMaterial(helmet.getString("material").split(":")[0]), 1, Byte.parseByte(helmet.getString("material").split(":")[1]));		             	
  	   ItemMeta helmetMeta = helmetItem.getItemMeta();
  	   helmetMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', helmet.getString("name")));
       for(String enchant : helmet.getStringList("enchantments")) {
    	   String[] enchantInfo = enchant.split(",");
    	   helmetMeta.addEnchant(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]), true);
       }
       helmetItem.setItemMeta(helmetMeta);	
       player.getInventory().setHelmet(helmetItem);

  	   ItemStack chestplateItem = new ItemStack(Material.getMaterial(chestplate.getString("material").split(":")[0]), 1, Byte.parseByte(chestplate.getString("material").split(":")[1]));		             	
  	   ItemMeta chestplateMeta = chestplateItem.getItemMeta();
  	   chestplateMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', chestplate.getString("name")));
       for(String enchant : chestplate.getStringList("enchantments")) {
    	   String[] enchantInfo = enchant.split(",");
    	   chestplateMeta.addEnchant(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]), true);
       }
       chestplateItem.setItemMeta(chestplateMeta);	
       player.getInventory().setChestplate(chestplateItem);
       
  	   ItemStack leggingsItem = new ItemStack(Material.getMaterial(leggings.getString("material").split(":")[0]), 1, Byte.parseByte(leggings.getString("material").split(":")[1]));		             	
  	   ItemMeta leggingsMeta = leggingsItem.getItemMeta();
  	   leggingsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', leggings.getString("name")));
       for(String enchant : leggings.getStringList("enchantments")) {
    	   String[] enchantInfo = enchant.split(",");
    	   leggingsMeta.addEnchant(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]), true);
       }
       leggingsItem.setItemMeta(leggingsMeta);	
       player.getInventory().setLeggings(leggingsItem);
       
  	   ItemStack bootsItem = new ItemStack(Material.getMaterial(boots.getString("material").split(":")[0]), 1, Byte.parseByte(boots.getString("material").split(":")[1]));		             	
  	   ItemMeta bootsMeta = bootsItem.getItemMeta();
  	   bootsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', boots.getString("name")));
       for(String enchant : boots.getStringList("enchantments")) {
    	   String[] enchantInfo = enchant.split(",");
    	   bootsMeta.addEnchant(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]), true);
       }
       bootsItem.setItemMeta(bootsMeta);	
       player.getInventory().setBoots(bootsItem);				
	}
	
	
	@SuppressWarnings("deprecation")
	public void setPlayerKit(Player player) {
		String kitName = null;
		if(kitData.getString(String.valueOf(player.getUniqueId())) == null) {
			kitName = plugin.getConfig().getString("default_kit");
			kitData.set(String.valueOf(player.getUniqueId()), kitName);
			try {
				kitData.save(new File("plugins/CastleWars/data.yml"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Main.data = configManager.getData();
		} else {
			kitName = kitData.getString(String.valueOf(player.getUniqueId()));
		}
		if(kits.getConfigurationSection("kits." + kitName) == null) {
			kitData.set(String.valueOf(player.getUniqueId()), plugin.getConfig().getString("default_kit"));
			try {
				kitData.save(new File("plugins/CastleWars/data.yml"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			Main.data = configManager.getData();
		}
		ConfigurationSection kingKit = kits.getConfigurationSection("kits." + kitName);
		ConfigurationSection items = kingKit.getConfigurationSection("items");
		int itemNum = items.getKeys(false).size();
		for(int g = 0; g < itemNum; g++) {
         	    ConfigurationSection itemInfo = items.getConfigurationSection(String.valueOf(g));
         	   ItemStack item = new ItemStack(Material.getMaterial(itemInfo.getString("material").split(":")[0]), itemInfo.getInt("quantity"), Byte.parseByte(itemInfo.getString("material").split(":")[1]));		             	
         	   ItemMeta meta = item.getItemMeta();
             	meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', itemInfo.getString("name")));
             	for(String enchant : itemInfo.getStringList("enchantments")) {
             		String[] enchantInfo = enchant.split(",");
             		meta.addEnchant(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]), true);
             	}
             	item.setItemMeta(meta);
         	    player.getInventory().addItem(item); 	    				
		}
		for(String effect : kingKit.getStringList("effects")) {
			String[] effectA = effect.split(",");
			PotionEffect potion = new PotionEffect(PotionEffectType.getByName(effectA[0]), Integer.parseInt(effectA[1]) * 20, Integer.parseInt(effectA[2]), true);
			player.addPotionEffect(potion);
		}
		ConfigurationSection helmet = kingKit.getConfigurationSection("armor").getConfigurationSection("helmet");
		ConfigurationSection chestplate = kingKit.getConfigurationSection("armor").getConfigurationSection("chestplate");
		ConfigurationSection leggings = kingKit.getConfigurationSection("armor").getConfigurationSection("leggings");
		ConfigurationSection boots = kingKit.getConfigurationSection("armor").getConfigurationSection("boots");
  	   ItemStack helmetItem = new ItemStack(Material.getMaterial(helmet.getString("material").split(":")[0]), 1, Byte.parseByte(helmet.getString("material").split(":")[1]));		             	
  	   ItemMeta helmetMeta = helmetItem.getItemMeta();
  	   helmetMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', helmet.getString("name")));
       for(String enchant : helmet.getStringList("enchantments")) {
    	   String[] enchantInfo = enchant.split(",");
    	   helmetMeta.addEnchant(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]), true);
       }
       helmetItem.setItemMeta(helmetMeta);	
       player.getInventory().setHelmet(helmetItem);

  	   ItemStack chestplateItem = new ItemStack(Material.getMaterial(chestplate.getString("material").split(":")[0]), 1, Byte.parseByte(chestplate.getString("material").split(":")[1]));		             	
  	   ItemMeta chestplateMeta = chestplateItem.getItemMeta();
  	   chestplateMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', chestplate.getString("name")));
       for(String enchant : chestplate.getStringList("enchantments")) {
    	   String[] enchantInfo = enchant.split(",");
    	   chestplateMeta.addEnchant(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]), true);
       }
       chestplateItem.setItemMeta(chestplateMeta);	
       player.getInventory().setChestplate(chestplateItem);
       
  	   ItemStack leggingsItem = new ItemStack(Material.getMaterial(leggings.getString("material").split(":")[0]), 1, Byte.parseByte(leggings.getString("material").split(":")[1]));		             	
  	   ItemMeta leggingsMeta = leggingsItem.getItemMeta();
  	   leggingsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', leggings.getString("name")));
       for(String enchant : leggings.getStringList("enchantments")) {
    	   String[] enchantInfo = enchant.split(",");
    	   leggingsMeta.addEnchant(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]), true);
       }
       leggingsItem.setItemMeta(leggingsMeta);	
       player.getInventory().setLeggings(leggingsItem);
       
  	   ItemStack bootsItem = new ItemStack(Material.getMaterial(boots.getString("material").split(":")[0]), 1, Byte.parseByte(boots.getString("material").split(":")[1]));		             	
  	   ItemMeta bootsMeta = bootsItem.getItemMeta();
  	   bootsMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', boots.getString("name")));
       for(String enchant : boots.getStringList("enchantments")) {
    	   String[] enchantInfo = enchant.split(",");
    	   bootsMeta.addEnchant(Enchantment.getByName(enchantInfo[0]), Integer.parseInt(enchantInfo[1]), true);
       }
       bootsItem.setItemMeta(bootsMeta);	
       player.getInventory().setBoots(bootsItem);
	}
}
