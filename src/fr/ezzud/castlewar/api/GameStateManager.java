package fr.ezzud.castlewar.api;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
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
import fr.ezzud.castlewar.methods.RandomUtil;
import fr.ezzud.castlewar.methods.configManager;
import fr.ezzud.castlewar.methods.inATeam;
import net.md_5.bungee.api.ChatColor;

public class GameStateManager {
	public static boolean GameState;
	public Main plugin = Main.getInstance();
	public static String team1King;
	public static String team2King;
	public static Player king1Player;
	public static Player king2Player;
	public static boolean getGameState() {
		return GameState;
	}
	public static String getTeam1King() {
		return team1King;
	}
	public static String getTeam2King() {
		return team2King;
	}
	@SuppressWarnings("deprecation")
	public void startGame() {
		YamlConfiguration kits = Main.kits;
		YamlConfiguration team = Main.teams;
		YamlConfiguration kitData = Main.data;
		GameState = true;
		Bukkit.broadcastMessage("Game Started!");
		Object[] array = Bukkit.getOnlinePlayers().toArray();
		new TeamManager();
		for(int i = 0; i < array.length; i++) {
			Player player = (Player) array[i];
			player.getInventory().clear();
			player.getActivePotionEffects().clear();
			Bukkit.getLogger().info("Choosing team of " + player.getName());
			List<String> team1 = CastleTeam.getMembers("team1");
			List<String> team2 = CastleTeam.getMembers("team2");
			
			if(inATeam.checkTeam(player.getName()) == false) {
				
				if(team1.size() - 1 > team2.size() - 1) {
		    		List<String> lst = team.getStringList("team2"); 
		    		lst.add(player.getName());
		    		team.set("team2", lst);
		    		try {
		    			team.save(new File("plugins/CastleWars/teams.yml"));
		    		} catch (IOException e1) {
		    			e1.printStackTrace();
		    		}
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam2().getColor() + player.getName() + "&r"));
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam2().getColor() + player.getName() + "&r"));
	    			player.sendMessage("You joined the team 2");
	    			
	    			
				} else if(team1.size() - 1 < team2.size() - 1) {
		    		List<String> lst = team.getStringList("team1"); 
		    		lst.add(player.getName());
		    		team.set("team1", lst);
		    		try {
		    			team.save(new File("plugins/CastleWars/teams.yml"));
		    		} catch (IOException e1) {
		    			e1.printStackTrace();
		    		}
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getColor() + player.getName() + "&r"));
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getColor() + player.getName() + "&r"));
	    			player.sendMessage("You joined the team 1");	
	    			
	    			
				} else {
		    		List<String> lst = team.getStringList("team1"); 
		    		lst.add(player.getName());
		    		team.set("team1", lst);
		    		try {
		    			team.save(new File("plugins/CastleWars/teams.yml"));
		    		} catch (IOException e1) {
		    			e1.printStackTrace();
		    		}
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getColor() + player.getName() + "&r"));
					player.setPlayerListName(ChatColor.translateAlternateColorCodes('&', TeamManager.getTeam1().getColor() + player.getName() + "&r"));
	    			player.sendMessage("You joined the team 1");	
	    			
	    			
				}
				
			}
			player.setGameMode(GameMode.SURVIVAL);
		}
		Main.teams = configManager.getTeams();
		team1King = RandomUtil.RandomPlayer(Main.teams.getStringList("team1"));
		team2King = RandomUtil.RandomPlayer(Main.teams.getStringList("team2"));
		king1Player = Bukkit.getPlayer(team1King);
		king2Player = Bukkit.getPlayer(team2King);
		for(int i = 0; i < array.length; i++) {
			Player player = (Player) array[i];
			String king_rank = plugin.getConfig().getString("king_rank");
			String soldier_rank = plugin.getConfig().getString("soldier_rank");
			ConfigurationSection team1Config = plugin.getConfig().getConfigurationSection("team1");
			ConfigurationSection team2Config = plugin.getConfig().getConfigurationSection("team2");
			
			if(player.getName().equalsIgnoreCase(team1King)) {
				player.sendMessage("You are the king 1!");
				if(plugin.getConfig().getString("rankType").equalsIgnoreCase("prefix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', king_rank + player.getDisplayName()));
				} else if(plugin.getConfig().getString("rankType").equalsIgnoreCase("suffix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + king_rank ));
				}
				String[] coords = team1Config.getString("king_spawnpoint").split(",");
				Location loc = new Location(Bukkit.getWorld(coords[5]), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				player.teleport(loc);
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
				
				
			} else if(player.getName().equalsIgnoreCase(team2King)) {
				player.sendMessage("You are the king 2!");
				if(plugin.getConfig().getString("rankType").equalsIgnoreCase("prefix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', king_rank + player.getDisplayName()));
				} else if(plugin.getConfig().getString("rankType").equalsIgnoreCase("suffix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + king_rank ));
				}
				String[] coords = team2Config.getString("king_spawnpoint").split(",");
				Location loc = new Location(Bukkit.getWorld(coords[5]), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
				player.teleport(loc);
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
	           
			} else {
				if(plugin.getConfig().getString("rankType").equalsIgnoreCase("prefix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', soldier_rank + player.getDisplayName()));
				} else if(plugin.getConfig().getString("rankType").equalsIgnoreCase("suffix")) {
					player.setDisplayName(ChatColor.translateAlternateColorCodes('&', player.getDisplayName() + soldier_rank ));
				}	
				if(inATeam.whichTeam(player.getName()).equalsIgnoreCase("team1")) {
					String[] coords = team1Config.getString("soldier_spawnpoint").split(",");
					Location loc = new Location(Bukkit.getWorld(coords[5]), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
					player.teleport(loc);
				} else if(inATeam.whichTeam(player.getName()).equalsIgnoreCase("team2")) {
					String[] coords = team2Config.getString("soldier_spawnpoint").split(",");
					Location loc = new Location(Bukkit.getWorld(coords[5]), Double.parseDouble(coords[0]), Double.parseDouble(coords[1]), Double.parseDouble(coords[2]), Float.parseFloat(coords[3]), Float.parseFloat(coords[3]));
					player.teleport(loc);
				}


				
				if(kitData.getKeys(false).contains(String.valueOf(player.getUniqueId())) == false) {
					kitData.set(String.valueOf(player.getUniqueId()), plugin.getConfig().getString("default_kit"));
	    			try {
						kitData.save(new File("plugins/CastleWars/data.yml"));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
	    			Main.data = configManager.getData();
				}
				
				
				
				ConfigurationSection kingKit = kits.getConfigurationSection("kits." + kitData.getString(String.valueOf(player.getUniqueId())));
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
		
		
	}
}
