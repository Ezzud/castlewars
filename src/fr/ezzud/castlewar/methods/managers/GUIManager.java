package fr.ezzud.castlewar.methods.managers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;

import fr.ezzud.castlewar.Main;
import fr.ezzud.castlewar.methods.inATeam;
import fr.ezzud.castlewar.methods.messagesFormatter;
import net.md_5.bungee.api.ChatColor;

public class GUIManager {
	private Player player;
	public Main plugin = Main.getInstance();
	public GUIManager(Player player) {
		this.player = player;
	}
	YamlConfiguration guis = Main.guis;
	YamlConfiguration data = Main.data;
	YamlConfiguration kits = Main.kits;
	YamlConfiguration messages = Main.messages;

	public void initializeKitsGUI(Inventory inv) {
		ConfigurationSection chooseItem = guis.getConfigurationSection("chooseKit.chooseItems");
    	List<Integer> usedPosition = new ArrayList<Integer>();
        for(int i = 0; i < chooseItem.getKeys(false).size(); i++) {
        	ConfigurationSection itemSec = chooseItem.getConfigurationSection(String.valueOf(i));
        	usedPosition.add(itemSec.getInt("position"));
        	String[] kitInfo = itemSec.getString("item").split(",");
        	List<String> kitDesc = new ArrayList<String>();
        	String permission = kits.getConfigurationSection("kits." + itemSec.getString("kit")).getString("permission");
        	if(data.getString(String.valueOf(player.getUniqueId())) == null) {
				data.set(String.valueOf(player.getUniqueId()), plugin.getConfig().getString("default_kit"));
    			try {
					data.save(new File("plugins/CastleWars/data.yml"));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
    			Main.data = configManager.getData();
        	}
        	if (!player.hasPermission(permission) && !player.isOp()) {
        		kitDesc.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("gui.kits").getString("noPermission"))));
        	} else if(data.getString(String.valueOf(player.getUniqueId())).equalsIgnoreCase(itemSec.getString("kit"))) {
        		kitDesc.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("gui.kits").getString("alreadySelected"))));
        	} else {
        		kitDesc.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("gui.kits").getString("select"))));
        	}
        	
    		Iterator<String> kitLore = itemSec.getStringList("lore").iterator();
            while(kitLore.hasNext()) {
                String text = kitLore.next();
                text = ChatColor.translateAlternateColorCodes('&', text);
                kitDesc.add(text);
            }
            
        	inv.setItem(itemSec.getInt("position"), createGuiItem(Material.valueOf(kitInfo[0]), 1, Byte.parseByte(kitInfo[1]), ChatColor.translateAlternateColorCodes('&', kitInfo[2]), kitDesc));
        }

        for(int i = 0; i < guis.getConfigurationSection("chooseKit.items").getKeys(false).size(); i++) {
        	ConfigurationSection item = guis.getConfigurationSection("chooseKit.items." + String.valueOf(i));
        	String[] itemInfo = item.getString("item").split(",");
        	usedPosition.add(item.getInt("position"));
      	   	List<String> itemList = new ArrayList<String>();;
     		Iterator<String> itemIt = item.getStringList("lore").iterator();
            while(itemIt.hasNext()) {
                 String text = itemIt.next();
                 text = ChatColor.translateAlternateColorCodes('&', text);
                 itemList.add(text);
             }
             inv.setItem(item.getInt("position"), createGuiItem(Material.valueOf(itemInfo[0]), Integer.parseInt(itemInfo[2]), Byte.parseByte(itemInfo[1]), ChatColor.translateAlternateColorCodes('&', itemInfo[3]), itemList));

        }
        
        for(int i = 0; i < guis.getConfigurationSection("chooseKit").getInt("rows")*9; i++) {
        	if(usedPosition.contains(i) == false) {
            	String item = guis.getConfigurationSection("chooseKit").getString("replaceEmptyCaseBy");
            	String[] itemInfo = item.split(",");   
            	inv.setItem(i, createGuiItem(Material.valueOf(itemInfo[0]), 1, Byte.parseByte(itemInfo[1]), ChatColor.translateAlternateColorCodes('&', itemInfo[2]), null));
        	}

        }
	}
    public void initializeTeamGUI(Inventory inv) {
    	Scoreboard board = Main.board;
    	ConfigurationSection team1Item = guis.getConfigurationSection("chooseTeam.chooseItems.team1");
    	ConfigurationSection team2Item = guis.getConfigurationSection("chooseTeam.chooseItems.team2");
    	ConfigurationSection noneItem = guis.getConfigurationSection("chooseTeam.chooseItems.none");
    	ConfigurationSection items = guis.getConfigurationSection("chooseTeam.items");
   	   int maxPlayer = plugin.getConfig().getInt("maxPlayers");
   	   if(maxPlayer % 2 == 1) {
   		   maxPlayer++;
   	   }   
   	   maxPlayer = maxPlayer/2;
    	List<Integer> usedPosition = new ArrayList<Integer>();
    	usedPosition.add(team1Item.getInt("position"));
    	usedPosition.add(team2Item.getInt("position"));
    	usedPosition.add(noneItem.getInt("position"));
    	
    	String[] team1Info = team1Item.getString("item").split(",");
    	   List<String> team1List = new ArrayList<String>();
    	   List<String> team1Team = new ArrayList<>();
    	   for(String pl : board.getTeam("team1").getEntries()) {
    		   team1Team.add(pl);
    	   }
     	   List<String> team11team = new ArrayList<String>();
    	   if(inATeam.checkSpecificTeam(player.getName(), "team1") == true) {
    		   team1List.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("gui.team").getString("alreadyInTeam"))));
    	   } else if(team1Team.size() - 1 >= maxPlayer) {
    		   team1List.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("gui.team").getString("full"))));
    	   } else {
    		   team1List.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("gui.team").getString("join")))); 
    	   }
     	   
     	   for(int i = 0; i < maxPlayer + 1; i++) {
     		   team11team.add("none");
     	   }
     	   for(int i = 0; i < team1Team.size(); i++) {
     		   team11team.set(i, team1Team.get(i));
     	   }
     	   for(String i : team11team) {
     		   if(!i.equalsIgnoreCase("team1")) {
     	 		   if(i == "none") {
     	 			  team1List.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("gui.team").getString("teamListFormat").replaceAll("%player%", " "))));  
     	 		   } else {
     	 			  team1List.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("gui.team").getString("teamListFormat").replaceAll("%player%", i))));   
     	 		   }			   
     		   } 		   
     	   }
		   Iterator<?> team1It = team1Item.getList("lore").iterator();
           while(team1It.hasNext()) {
               String text = (String) team1It.next();
               text = ChatColor.translateAlternateColorCodes('&', text);
               team1List.add(text);
           }
           
        inv.setItem(team1Item.getInt("position"), createGuiItem(Material.valueOf(team1Info[0]),1, Byte.parseByte(team1Info[1]), ChatColor.translateAlternateColorCodes('&', team1Info[2] + " &7(" + String.valueOf(team1Team.size() - 1)+ "/" + maxPlayer + ")"), team1List));


    	String[] team2Info = team2Item.getString("item").split(",");
 	   	List<String> team2List = new ArrayList<String>();
 	   List<String> team2Team = new ArrayList<>();
 	   for(String pl : board.getTeam("team2").getEntries()) {
 		   team2Team.add(pl);
 	   }
  	   List<String> team22team = new ArrayList<String>();
 	   if(inATeam.checkSpecificTeam(player.getName(), "team2") == true) {
		   team2List.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("gui.team").getString("alreadyInTeam"))));
	   } else if(team2Team.size() - 1 >= maxPlayer) {
		   team2List.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("gui.team").getString("full"))));
	   } else {
		   team2List.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("gui.team").getString("join")))); 
	   }

 	   for(int i = 0; i < maxPlayer + 1; i++) {
 		   team22team.add("none");
 	   }
 	   for(int i = 0; i < team2Team.size(); i++) {
 		   team22team.set(i, team2Team.get(i));
 	   }
 	   for(String i : team22team) {
 		   if(!i.equalsIgnoreCase("team2")) {
 	 		   if(i == "none") {
 	 			  team2List.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("gui.team").getString("teamListFormat").replaceAll("%player%", " ")))); 
 	 		   } else {
 	 			  team2List.add(ChatColor.translateAlternateColorCodes('&', messagesFormatter.formatMessage(messages.getConfigurationSection("gui.team").getString("teamListFormat").replaceAll("%player%", i))));  
 	 		   }			   
 		   } 		   
 	   }
		Iterator<String> team2It = team2Item.getStringList("lore").iterator();
        while(team2It.hasNext()) {
            String text = team2It.next();
            text = ChatColor.translateAlternateColorCodes('&', text);
            team2List.add(text);
        }
        inv.setItem(team2Item.getInt("position"), createGuiItem(Material.valueOf(team2Info[0]),1, Byte.parseByte(team2Info[1]), ChatColor.translateAlternateColorCodes('&', team2Info[2] + " &7(" + String.valueOf(team2Team.size() - 1)+ "/" + maxPlayer + ")"), team2List));

    	String[] noneInfo = noneItem.getString("item").split(",");
 	   List<String> noneList = new ArrayList<String>();;
		   Iterator<String> noneIt = noneItem.getStringList("lore").iterator();
        while(noneIt.hasNext()) {
            String text = noneIt.next();
            text = ChatColor.translateAlternateColorCodes('&', text);
            noneList.add(text);
        }
        
        inv.setItem(noneItem.getInt("position"), createGuiItem(Material.valueOf(noneInfo[0]),1, Byte.parseByte(noneInfo[1]), ChatColor.translateAlternateColorCodes('&', noneInfo[2]), noneList));
        for(int i = 0; i < items.getKeys(false).size(); i++) {
        	ConfigurationSection item = guis.getConfigurationSection("chooseTeam.items." + String.valueOf(i));
        	String[] itemInfo = item.getString("item").split(",");
        	usedPosition.add(item.getInt("position"));
      	   	List<String> itemList = new ArrayList<String>();;
     		Iterator<String> itemIt = item.getStringList("lore").iterator();
            while(itemIt.hasNext()) {
                 String text = itemIt.next();
                 text = ChatColor.translateAlternateColorCodes('&', text);
                 itemList.add(text);
             }
             inv.setItem(item.getInt("position"), createGuiItem(Material.valueOf(itemInfo[0]), Integer.parseInt(itemInfo[2]), Byte.parseByte(itemInfo[1]), ChatColor.translateAlternateColorCodes('&', itemInfo[3]), itemList));

        }
        
        for(int i = 0; i < guis.getConfigurationSection("chooseTeam").getInt("rows")*9; i++) {
        	if(usedPosition.contains(i) == false) {
            	String item = guis.getConfigurationSection("chooseTeam").getString("replaceEmptyCaseBy");
            	String[] itemInfo = item.split(",");   
            	inv.setItem(i, createGuiItem(Material.valueOf(itemInfo[0]), 1, Byte.parseByte(itemInfo[1]), ChatColor.translateAlternateColorCodes('&', itemInfo[2]), null));
        	}

        }
    }
    
    
    @SuppressWarnings("deprecation")
	protected ItemStack createGuiItem(final Material material, final Integer quantity ,final Byte data, final String name, final List<String> lore) {
        final ItemStack item = new ItemStack(material, quantity, data);
        final ItemMeta meta = item.getItemMeta();
        
        meta.setDisplayName(name);
        meta.setLore(lore);
        item.setItemMeta(meta);

        return item;
    }
    
    
    public void openInventory(final Player ent, Inventory inv) {
        ent.openInventory(inv);
    }
}
