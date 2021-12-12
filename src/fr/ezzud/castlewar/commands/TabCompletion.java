package fr.ezzud.castlewar.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import fr.ezzud.castlewar.api.GameStateManager;

public class TabCompletion implements TabCompleter{
    @Override
    public List<String> onTabComplete (CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("castlewar") && args.length < 2){
            if(sender instanceof Player){
                Player player = (Player) sender;

                List<String> list = new ArrayList<>();
                list.add("help");
                list.add("kings");
                list.add("kits");
                if(GameStateManager.GameState == false) {
                	list.add("menu");
                }

                if(player.hasPermission("castlewar.admin.reload")) {
                    list.add("reload");
                    list.add("setspawn");
                    list.add("setlobby");
                    list.add("start");
                    list.add("stop");
                }
                return list;
            }
            
        } else {
            	switch(args[0].toLowerCase()) {
        			case "reload":
        				return null;
        			case "help":
        				return null;
        			case "kings":
        				return null;
        			case "stop":
        				return null;
        			case "kits":
        				return null;
        			case "menu":
        				return null;
        			case "setspawn":
        				if(args.length < 3) {
            				List<String> setSpawnlist = new ArrayList<>();
            				setSpawnlist.add("team1");
            				setSpawnlist.add("team2");
            				setSpawnlist.add("spectators");
            				return setSpawnlist;     
        				} else if(args.length > 3) {
        					return null;
        				} else {
        					if(args[1].equalsIgnoreCase("spectators")) {
        						return null;
        					} else {
                				List<String> setSpawnlist = new ArrayList<>();
                				setSpawnlist.add("king");
                				setSpawnlist.add("soldier");
                				return setSpawnlist;	
        					}

        				}
        			case "setlobby":
        				return null;
        			default:
        				return null;
            	}        		
        	

        }
        return null;
    }
}